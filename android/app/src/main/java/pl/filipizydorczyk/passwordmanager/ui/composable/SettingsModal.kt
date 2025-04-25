package pl.filipizydorczyk.passwordmanager.ui.composable


import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    isOpen: Boolean,
    vault: String?,
    onDismiss: () -> Unit,
    onKeyAdd: (uri: Uri?) -> Unit,
    onVaultAdd: (uri: Uri?) -> Unit,
    isKeyUploaded: Boolean
) {
    val keyLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri -> onKeyAdd(uri) }
    val vaultLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri -> onVaultAdd(uri) }

    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Settings", fontWeight = FontWeight.Medium, color = Color.Black)
                Text(
                    text = if (isKeyUploaded) "Key file is uploaded" else "No key file uploaded",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                Text(
                    text = if(vault != null) "Vault:...${vault.takeLast(20)}" else "No vault selected",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MainButton(
                        text = "Upload a key",
                        variant = MainButtonVariant.PRIMARY,
                        onClick = { keyLauncher.launch("*/*") }
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MainButton(
                        text = "Select a vault",
                        variant = MainButtonVariant.PRIMARY,
                        onClick = { vaultLauncher.launch(null) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsModalOpenWithEverythingUploaded() {
    SettingsModal(
        isOpen = true,
        onDismiss = {},
        onKeyAdd = {},
        onVaultAdd = {},
        vault = "/test/vault",
        isKeyUploaded = true
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsModalOpenWithKeyUploaded() {
    SettingsModal(
        isOpen = true,
        onDismiss = {},
        onKeyAdd = {},
        onVaultAdd = {},
        vault = null,
        isKeyUploaded = true
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsModalOpenWithoutKeyUploaded() {
    SettingsModal(
        isOpen = true,
        onDismiss = {},
        onKeyAdd = {},
        onVaultAdd = {},
        vault = null,
        isKeyUploaded = false
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsModalClosed() {
    SettingsModal(
        isOpen = false,
        onDismiss = {},
        onKeyAdd = {},
        onVaultAdd = {},
        vault = null,
        isKeyUploaded = false
    )
}