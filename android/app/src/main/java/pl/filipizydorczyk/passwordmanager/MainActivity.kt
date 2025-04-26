package pl.filipizydorczyk.passwordmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import pl.filipizydorczyk.passwordmanager.ui.composable.AddPasswordFloatingButton
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
    val vault by viewModel.vault.collectAsState(initial = null)
    val isKeyUploaded by viewModel.isKeyUploaded.collectAsState(initial = false)
    val passFiles by viewModel.passFiles.collectAsState(initial = arrayListOf())

    var query = remember { mutableStateOf("") }

    var openSettings = remember { mutableStateOf(false) }
    var selectedPass = remember { mutableStateOf<String?>(null) }
    var openNewPass = remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = { PasswordManagerAppBar(onSettingsClick = { openSettings.value = true }) },
        floatingActionButton = { AddPasswordFloatingButton( onClick = { openNewPass.value = true }) }
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
            onSave = { name, password -> {} },
            onCancel = { openNewPass.value = false }
        )
        PasswordModal(
            isOpen = selectedPass.value != null,
            onDismiss = { selectedPass.value = null },
            label = "Password",
            password = "aso232-dsa",
            onEdit = { /*TODO*/ },
            onDelete = { /*TODO*/ },
            onCancel = { selectedPass.value = null }
        )
    }
}