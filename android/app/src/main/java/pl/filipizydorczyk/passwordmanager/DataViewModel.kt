package pl.filipizydorczyk.passwordmanager

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.documentfile.provider.DocumentFile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataViewModel(private val context: Context) : ViewModel() {
    private val vaultKey = stringPreferencesKey("vault")

    private val _vaultValue = MutableStateFlow<String?>("")
    private val _isKeyUploaded = MutableStateFlow(false)
    private val _passFiles = MutableStateFlow<ArrayList<String>>(arrayListOf())

    val vault: StateFlow<String?> = _vaultValue
    val isKeyUploaded: StateFlow<Boolean> = _isKeyUploaded
    val passFiles: StateFlow<ArrayList<String>> = _passFiles

    init {
        viewModelScope.launch {
            val key = getPasswordKey();

            _isKeyUploaded.value = key != null;
            context.dataStore.data
                .map { preferences -> preferences[vaultKey] }
                .collect { value ->
                    _vaultValue.value = value
                    if(value != null) {
                        _passFiles.value = readFilesInDirectory(value)
                    }
                }

        }
    }

    fun updateVaultValue(value: String) {
        viewModelScope.launch {
            context.dataStore.updateData { preferences ->
                preferences.toMutablePreferences().apply {
                    this[vaultKey] = value
                }
            }
        }
    }

    fun uploadKeyFile(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.dataDir, "key")
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val key = getPasswordKey();
        _isKeyUploaded.value = key != null;
    }

    fun addPassword(name: String, password: String) {
        val treeUri = Uri.parse(_vaultValue.value)
        val documentFile = DocumentFile.fromTreeUri(context, treeUri)

        val keyByteArray = getPasswordKey()
        if(keyByteArray == null) {
            Toast.makeText(context, "Key file is not configured", Toast.LENGTH_LONG).show()
            return
        }

        val data = encryptString(password, keyByteArray)
        if(data == null) {
            Toast.makeText(context, "Password encrypt step failed", Toast.LENGTH_LONG).show()
            return
        }
        val jsonObject = data.toJson();

        if (documentFile != null && documentFile.canWrite()) {
            val newFile = documentFile.createFile("application/json", "$name.json")

            if (newFile != null) {
                val outputStream: OutputStream = context.contentResolver.openOutputStream(newFile.uri)!!
                outputStream.write(jsonObject.toByteArray())
                outputStream.close()
            } else {
                Toast.makeText(context, "Password with this name can not be created.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "You can't save password in this directory.", Toast.LENGTH_LONG).show()
        }

        _passFiles.value = readFilesInDirectory(_vaultValue.value!!)
    }

    fun updatePassword(name: String, password: String) {
        val treeUri = Uri.parse(_vaultValue.value)
        val documentFile = DocumentFile.fromTreeUri(context, treeUri)

        val keyByteArray = getPasswordKey()
        if(keyByteArray == null) {
            Toast.makeText(context, "Key file is not configured", Toast.LENGTH_LONG).show()
            return
        }

        val data = encryptString(password, keyByteArray)
        if(data == null) {
            Toast.makeText(context, "Password encrypt step failed", Toast.LENGTH_LONG).show()
            return
        }
        val jsonObject = data.toJson();

        if (documentFile != null && documentFile.canWrite()) {
            val existingFile = documentFile.findFile(name)
            if (existingFile != null) {
                val outputStream: OutputStream = context.contentResolver.openOutputStream(existingFile.uri)!!
                outputStream.write(jsonObject.toByteArray())
                outputStream.close()
            } else {
                Toast.makeText(context, "Password with this name does not exist.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "You can't save password in this directory.", Toast.LENGTH_LONG).show()
        }

        _passFiles.value = readFilesInDirectory(_vaultValue.value!!)
    }

    fun getPassword(name: String?): String? {
        if(name == null) {
            return null;
        }

        if(vault.value == null) {
            return null;
        }

        val keyByteArray = getPasswordKey()
        if(keyByteArray == null) {
            Toast.makeText(context, "Key file is not configured", Toast.LENGTH_LONG).show()
            return null;
        }

        val baseUri = Uri.parse(vault.value)
        val documentFile = DocumentFile.fromTreeUri(context, baseUri)
        val file = documentFile?.findFile(name) ?: return null

        return try {
            val uri = file.uri
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)

            val gson = Gson()
            val data = gson.fromJson(inputStream?.reader(), PasswordDataModel::class.java)
            inputStream?.close()



            decryptString(data.encryptedData, keyByteArray, data.iv);
        } catch (e: Exception) {
            e.printStackTrace();
            null;
        }
    }

    fun removePassword(name: String) {
        val treeUri = Uri.parse(_vaultValue.value)
        val documentFile = DocumentFile.fromTreeUri(context, treeUri)

        if (documentFile != null && documentFile.canWrite()) {
            val fileToRemove = documentFile.findFile(name)

            if (fileToRemove != null && documentFile.canWrite()) {
                fileToRemove.delete()
            } else {
                Toast.makeText(context, "Password with this name can not be deleted.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "You can't delete password in this directory.", Toast.LENGTH_LONG).show()
        }

        _passFiles.value = readFilesInDirectory(_vaultValue.value!!)
    }

    private fun getPasswordKey(): ByteArray? {
        val file = File(context.dataDir, "key")
        if (file.exists()) {
            return file.readBytes();
        }

        return null;
    }

    private fun readFilesInDirectory(uriString: String): ArrayList<String> {
        val contentResolver = context.contentResolver
        val uri = Uri.parse(uriString)
        val documentId = DocumentsContract.getTreeDocumentId(uri)

        if (documentId != null) {
            try {
                val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, documentId)
                val cursor = contentResolver.query(childrenUri, null, null, null, null)

                val files = arrayListOf<String>()

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val fileName = cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                        if(!fileName.startsWith(".")) {
                            files.add(fileName)
                        }
                    }
                    cursor.close()
                }

                return files;
            } catch (e: SecurityException) {
                Toast.makeText(context, "Security exception: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }

        return arrayListOf()
    }
}