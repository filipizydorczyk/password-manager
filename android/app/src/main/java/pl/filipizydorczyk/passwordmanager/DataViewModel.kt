package pl.filipizydorczyk.passwordmanager

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataViewModel(private val context: Context) : ViewModel() {
    private val vaultKey = stringPreferencesKey("vault")

    private val _vaultValue = MutableStateFlow<String?>("")
    private val _isKeyUploaded = MutableStateFlow<Boolean>(false)

    val vault: StateFlow<String?> = _vaultValue
    val isKeyUploaded: StateFlow<Boolean> = _isKeyUploaded

    init {
        viewModelScope.launch {
            val key = getPasswordKey();

            _isKeyUploaded.value = key != null;
            context.dataStore.data
                .map { preferences -> preferences[vaultKey] }
                .collect { value ->
                    _vaultValue.value = value
//                    val vaultDir = File(_vaultValue.value)
//                    if (vaultDir.exists() && vaultDir.isDirectory) {
//                        val files = vaultDir.listFiles()?.map { it.name } ?: emptyList()
//                        Toast.makeText(context, "Vault files: ${files.joinToString()}", Toast.LENGTH_LONG).show()
//                    } else {
//                        _vaultFiles.value = emptyList()
//                    }

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

    fun getPasswordKey(): ByteArray?{
        val file = File(context.dataDir, "key")
        if (file.exists()) {
            return file.readBytes();
        }

        return null;
    }
}
