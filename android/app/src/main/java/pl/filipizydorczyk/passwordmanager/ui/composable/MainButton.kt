package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import java.security.SecureRandom

enum class MainButtonVariant {
    PRIMARY,
    SECONDARY
}

@Composable
fun MainButton(text: String, variant: MainButtonVariant, onClick: () -> Unit) {
    val buttonColors = when (variant) {
        MainButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = Color(0, 32, 35),
            contentColor = Color.White
        )
        MainButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color(137, 31, 0),
            contentColor = Color.White
        )
    }

    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
        Text(text = text, fontWeight = FontWeight.Normal)
    }
}

@Composable
@Preview
fun MainButtonPrimaryPreview() {
    MainButton(text = "Test", variant = MainButtonVariant.PRIMARY, onClick = {})
}

@Composable
@Preview
fun MainButtonSecondaryPreview() {
    MainButton(text = "Test", variant = MainButtonVariant.SECONDARY, onClick = {})
}
