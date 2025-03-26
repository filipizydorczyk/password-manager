package pl.filipizydorczyk.passwordmanager.ui.composable

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

@Composable
fun StringList(list: List<String>, onClick: (v: String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list.size) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(list[item]) },
                shape = RoundedCornerShape(25),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = Dp.Hairline),
            ) {
                Box( modifier = Modifier.padding(16.dp)) {
                    Text(text = list[item], style = MaterialTheme.typography.bodyLarge)
                }
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

@Composable
@Preview
fun StringListPreview() {
    val passwords = remember {
        mutableStateOf(
            arrayListOf(
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "discord",
                "imdb",
                "xd"
            )
        )
    }

    StringList(list = passwords.value, onClick = {})
}