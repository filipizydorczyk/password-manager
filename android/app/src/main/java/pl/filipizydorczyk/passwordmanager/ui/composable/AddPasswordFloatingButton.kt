package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddPasswordFloatingButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        containerColor = Color(0, 32, 35)
    ) {
        Text(text = "+", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddPasswordFloatingButton() {
    AddPasswordFloatingButton(
        onClick = { /* No-op for preview */ }
    )
}