package dev.vvsevolodovich.compose.figma.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.vvsevolodovich.compose.figma.preview.R.mipmap.chevron_right

@Composable
fun FigmaPreview(fileId: String, id: String, token: String, content: @Composable () -> Unit) {
    val imageUrl = remember { mutableStateOf("") }
    FigmaClient(fileId, token).getImage(id) { s -> imageUrl.value = s ?: "" };

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        CoilImage(model = imageUrl.value, modifier = Modifier.fillMaxWidth(), width = this.maxWidth, height = this.maxHeight)
        content()
    }
}

@Composable
fun ComponentRow(figmaComponent: FigmaClient.FigmaComponent) {
    Column {
        Text(text = figmaComponent.name)
        Text(text = figmaComponent.nodeId)
        CoilImage(model = figmaComponent.thumbnailUrl)
    }
}

@Composable
fun FigmaSearchComponents(fileId: String, token: String) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val components = remember {
            mutableStateOf(arrayListOf<FigmaClient.FigmaComponent>())
        }
        val text = remember { mutableStateOf("") }

        FigmaClient(fileId, token).searchComponents(fileKey = fileId) {
            components.value.clear()
            it?.let { it1 -> components.value = ArrayList(it1) };
        }
        Column() {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Search by name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                items(components.value.filter { c -> text.value.isEmpty() || c.name.contains(text.value)}) { component ->
                    ComponentRow(component)
                }
            }
        }
    }
}

@Composable
fun searchPreview() {
    FigmaSearchComponents(fileId = "hXkcHwDksxm3I5WKEvrv5Y", token = "309755-a017a6d0-18cc-4e05-88c1-6e685d399a9a")
}

@Preview
@Composable
fun figmaPreviewPreview() {
    //FigmaPreview(fileId = "d51xtGzfqgz1LXyZPDnP1E", id = "1:405", token = "162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
    FigmaPreview(fileId = "hXkcHwDksxm3I5WKEvrv5Y", id = "23999:446491", token = "309755-a017a6d0-18cc-4e05-88c1-6e685d399a9a") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Title", fontSize = 17.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1", color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Image(painterResource(chevron_right), "content description")
            }
        }
    }
}