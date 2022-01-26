package dev.vvsevolodovich.compose.figma.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FigmaPreview(fileId: String, id: String, token: String, content: @Composable () -> Unit) {
    val imageUrl = remember { mutableStateOf("") }
    FigmaClient(fileId, token).getImage(id) { s -> imageUrl.value = s ?: "" };

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        content()
        CoilImage(model = imageUrl.value, modifier = Modifier.fillMaxWidth(), width = this.maxWidth, height = this.maxHeight)
    }
}

@Composable
fun ComponentRow(figmaComponent: FigmaClient.FigmaComponent) {
    Column {
        Text(text = figmaComponent.name)
        CoilImage(model = figmaComponent.thumbnailUrl)
    }
}

@Composable
fun FigmaSearchComponents(fileId: String, token: String) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val components = remember {
            mutableStateOf(arrayListOf<FigmaClient.FigmaComponent>())
        }
        FigmaClient(fileId, token).searchComponents(fileKey = fileId) {
            components.value.clear()
            it?.let { it1 -> components.value = ArrayList(it1) };
        }
        LazyColumn {
            items(components.value) { component ->
                ComponentRow(component)
            }
        }
        //CoilImage(model = imageUrl.value, modifier = Modifier.fillMaxWidth(), width = this.maxWidth, height = this.maxHeight)
    }
}

@Preview
@Composable
fun searchPreview() {
    FigmaSearchComponents(fileId = "hXkcHwDksxm3I5WKEvrv5Y", token = "309755-a017a6d0-18cc-4e05-88c1-6e685d399a9a")
}

//@Preview
@Composable
fun figmaPreviewPreview() {
    //FigmaPreview(fileId = "d51xtGzfqgz1LXyZPDnP1E", id = "1:405", token = "162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
    FigmaPreview(fileId = "hXkcHwDksxm3I5WKEvrv5Y", id = "27075:473072", token = "309755-a017a6d0-18cc-4e05-88c1-6e685d399a9a") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button({},
                Modifier.padding(24.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color(0xD37777),
                    )) {
                Text("TOUCH ME", color = Color.Black)
            }
        }
    }
}