package pl.filipizydorczyk.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        setContent {
            PasswordManagerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {
    var query = remember { mutableStateOf("") }

    var openSettings = remember { mutableStateOf(false) }
    var selectedPass = remember { mutableStateOf<String?>(null) }
    var openNewPass = remember { mutableStateOf(false) }

    val tmppasswords = remember {
        mutableStateOf(
            arrayListOf(
                "discord",
                "imdb",
            )
        )
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
                        tmppasswords.value
                    } else {
                        tmppasswords.value.filter { it.contains(query.value, ignoreCase = true) }
                    },
                    onClick = { selectedPass.value = it } )
            }
        }

        SettingsModal(
            isOpen = openSettings.value,
            onDismiss = { openSettings.value = false },
            onKeyAdd = { /*TODO*/ },
            isKeyUploaded = true
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