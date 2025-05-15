package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    initialValue: String?,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var password = remember { mutableStateOf("") }

    val textColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.Gray,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    LaunchedEffect(isOpen) {
        password.value = initialValue ?: ""
    }

    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = textColors,
                    label = { Text(text = "Password") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                MainButton(text = "Generate new password", variant = MainButtonVariant.PRIMARY, onClick = { password.value = generatePassword(14) })
                MainButton(text = "Save", variant = MainButtonVariant.PRIMARY, onClick = { onSave(password.value) })
                MainButton(text = "Cancel", variant = MainButtonVariant.SECONDARY, onClick = { onCancel() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditPasswordModalOpen() {
    EditPasswordModal(
        isOpen = true,
        onDismiss = {},
        onSave = { password ->
            // Handle save action in preview (no-op)
        },
        onCancel = {},
        initialValue = null
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditPasswordModalClosed() {
    EditPasswordModal(
        isOpen = false,
        onDismiss = {},
        onSave = { password ->
            // Handle save action in preview (no-op)
        },
        onCancel = {},
        initialValue = null
    )
}