package pl.filipizydorczyk.passwordmanager.ui.composable


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
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onKeyAdd: () -> Unit,
    isKeyUploaded: Boolean
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Settings", fontWeight = FontWeight.Medium, color = Color.Black)
                Text(
                    text = if (isKeyUploaded) "Key file is uploaded" else "No key file uploaded",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MainButton(text = "Upload a key", variant = MainButtonVariant.PRIMARY, onClick = { onKeyAdd() })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSettingsModalOpenWithKeyUploaded() {
    SettingsModal(
        isOpen = true,
        onDismiss = {},
        onKeyAdd = {},
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
        isKeyUploaded = false
    )
}