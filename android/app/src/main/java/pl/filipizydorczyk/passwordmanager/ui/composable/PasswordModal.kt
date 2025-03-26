package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pl.filipizydorczyk.passwordmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    label: String,
    password: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onCancel: () -> Unit
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier.fillMaxWidth().height(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { onEdit() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_document_24),
                            contentDescription = "Edit",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_restore_from_trash_24),
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_moderator_24),
                    contentDescription = "Password",
                    tint = Color.Black
                )
                Text(text = label, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(text = password, fontWeight = FontWeight.Normal, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                MainButton(text = "Copy", variant = MainButtonVariant.PRIMARY, onClick = { onCopy() })
                MainButton(text = "Cancel", variant = MainButtonVariant.SECONDARY, onClick = { onCancel() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordModalOpen() {
    PasswordModal(
        isOpen = true,
        onDismiss = {},
        label = "Example Password",
        password = "mySecretPassword",
        onEdit = {},
        onDelete = {},
        onCopy = {},
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordModalClosed() {
    PasswordModal(
        isOpen = false,
        onDismiss = {},
        label = "Password",
        password = "mySecretPassword",
        onEdit = {},
        onDelete = {},
        onCopy = {},
        onCancel = {}
    )
}
