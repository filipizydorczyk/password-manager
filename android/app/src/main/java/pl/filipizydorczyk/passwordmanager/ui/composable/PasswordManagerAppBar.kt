package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import pl.filipizydorczyk.passwordmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerAppBar(
    title: String = "Password Manager",
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(text = title, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
        },
        actions = {
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_settings_24), // Replace with your settings icon resource
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordManagerAppBar() {
    PasswordManagerAppBar(
        onSettingsClick = { /* No-op for preview */ }
    )
}
