package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.filipizydorczyk.passwordmanager.R

@Composable
fun SearchInput(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = "Search...") },
        shape = CutCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24), // Replace with your settings icon resource
                contentDescription = "Search icon",
                tint = Color.Black
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
@Preview
fun SearchInputPreview(){
    SearchInput(value = "", onValueChange = {})
}