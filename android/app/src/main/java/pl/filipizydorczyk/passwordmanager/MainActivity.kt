package pl.filipizydorczyk.passwordmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import pl.filipizydorczyk.passwordmanager.ui.composable.AddPasswordFloatingButton
import pl.filipizydorczyk.passwordmanager.ui.composable.AlertDialogAction
import pl.filipizydorczyk.passwordmanager.ui.composable.EditPasswordModal
import pl.filipizydorczyk.passwordmanager.ui.composable.NewPasswordModal
import pl.filipizydorczyk.passwordmanager.ui.composable.PasswordManagerAppBar
import pl.filipizydorczyk.passwordmanager.ui.composable.PasswordModal
import pl.filipizydorczyk.passwordmanager.ui.composable.SearchInput
import pl.filipizydorczyk.passwordmanager.ui.composable.SettingsModal
import pl.filipizydorczyk.passwordmanager.ui.composable.StringList
import pl.filipizydorczyk.passwordmanager.ui.theme.PasswordManagerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataViewModel = DataViewModel(applicationContext)

        setContent {
            PasswordManagerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainView(dataViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(viewModel: DataViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val vault by viewModel.vault.collectAsState(initial = null)
    val isKeyUploaded by viewModel.isKeyUploaded.collectAsState(initial = false)
    val passFiles by viewModel.passFiles.collectAsState(initial = arrayListOf())

    val query = remember { mutableStateOf("") }

    val openSettings = remember { mutableStateOf(false) }
    val selectedPass = remember { mutableStateOf<String?>(null) }
    val openNewPass = remember { mutableStateOf(false) }
    val editPass = remember { mutableStateOf<String?>(null) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val currentPass = remember {
        derivedStateOf { viewModel.getPassword(selectedPass.value) }
    }

    fun handleVaultChange(value: Uri?) {
        value?.let { safeUri ->
            context.contentResolver.takePersistableUriPermission(safeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            viewModel.updateVaultValue(safeUri.toString());
            Toast.makeText(context, "Set vault to $value", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleKeyChange(uri: Uri?) {
        if(uri != null) {
            viewModel.uploadKeyFile(uri);
            Toast.makeText(context, "File uploaded", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Couldn't read selected file", Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePassEdit(name: String?, newPass: String) {
        val editedPassName = selectedPass.value
        if(name != null) {
            viewModel.updatePassword(name, newPass)
            editPass.value = null
            selectedPass.value = null
            selectedPass.value = editedPassName
        }
    }

    LaunchedEffect(openSettings.value, selectedPass.value, openNewPass.value, editPass.value, openAlertDialog.value) {
        focusManager.clearFocus()
    }


    Scaffold(
        topBar = { PasswordManagerAppBar(onSettingsClick = { openSettings.value = true }) },
        floatingActionButton = { AddPasswordFloatingButton( onClick = { openNewPass.value = true } ) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SearchInput(value = query.value, onValueChange = { query.value = it })
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                StringList(
                    list = if (query.value.isEmpty()) {
                        passFiles
                    } else {
                        passFiles.filter { it.contains(query.value, ignoreCase = true) }
                    },
                    onClick = { selectedPass.value = it } )
            }
        }

        SettingsModal(
            isOpen = openSettings.value,
            onDismiss = { openSettings.value = false },
            onKeyAdd = { uri -> handleKeyChange(uri) },
            onVaultAdd = { uri -> handleVaultChange(uri) },
            vault = vault,
            isKeyUploaded = isKeyUploaded
        )
        NewPasswordModal(
            isOpen = openNewPass.value,
            onDismiss = { openNewPass.value = false },
            onSave = { name, password -> viewModel.addPassword(name, password); openNewPass.value = false },
            onCancel = { openNewPass.value = false }
        )
        PasswordModal(
            isOpen = selectedPass.value != null,
            onDismiss = { selectedPass.value = null },
            label = selectedPass.value,
            password = currentPass.value,
            onEdit = { editPass.value = currentPass.value },
            onDelete = { openAlertDialog.value = true },
            onCancel = { selectedPass.value = null }
        )
        EditPasswordModal(
            isOpen = editPass.value != null,
            onDismiss = { editPass.value = null },
            initialValue = editPass.value,
            onSave = { newPass -> handlePassEdit(selectedPass.value, newPass) },
            onCancel = {editPass.value = null}
        )
        AlertDialogAction(
            open = openAlertDialog.value,
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = { viewModel.removePassword(selectedPass.value!!); selectedPass.value = null; openAlertDialog.value = false },
            dialogTitle = "Are you sure?",
            dialogText = "This action will remove the file with no possibility to roll back",
            icon = Icons.Default.Info
        )

    }
}