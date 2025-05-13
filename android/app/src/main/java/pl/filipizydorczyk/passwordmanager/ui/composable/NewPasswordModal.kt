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
import java.security.SecureRandom

fun generatePassword(length: Int): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    val password = (1..length).map { characters[random.nextInt(characters.length)] }.joinToString("")
    return password
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    val textColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.Gray,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    LaunchedEffect(isOpen) {
        name.value = ""
        password.value = ""
    }

    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = textColors,
                    label = { Text(text = "Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                MainButton(text = "Save", variant = MainButtonVariant.PRIMARY, onClick = { onSave(name.value, password.value) })
                MainButton(text = "Cancel", variant = MainButtonVariant.SECONDARY, onClick = { onCancel() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewPasswordModalOpen() {
    NewPasswordModal(
        isOpen = true,
        onDismiss = {},
        onSave = { name, password ->
            // Handle save action in preview (no-op)
        },
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewPasswordModalClosed() {
    NewPasswordModal(
        isOpen = false,
        onDismiss = {},
        onSave = { name, password ->
            // Handle save action in preview (no-op)
        },
        onCancel = {}
    )
}