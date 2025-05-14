package pl.filipizydorczyk.passwordmanager.ui.composable

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pl.filipizydorczyk.passwordmanager.R

fun handleCopy(context: Context, text: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
    Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    label: String?,
    password: String?,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

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
                if(label != null && password != null) {
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
                    MainButton(text = "Copy", variant = MainButtonVariant.PRIMARY, onClick = { handleCopy(context, password) })
                    MainButton(text = "Cancel", variant = MainButtonVariant.SECONDARY, onClick = { onCancel() })
                }
                else {
                    Text(
                        text = "Corrupted password file",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().wrapContentSize(Alignment.Center)
                    )
                }
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
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordModalMissingDataClosed() {
    PasswordModal(
        isOpen = true,
        onDismiss = {},
        label = null,
        password = null,
        onEdit = {},
        onDelete = {},
        onCancel = {}
    )
}
