package dev.vvsevolodovich.compose.figma.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FigmaPreview(fileId: String, id: String, token: String, content: @Composable () -> Unit) {
    val imageUrl = remember { mutableStateOf("") }
    FigmaClient(fileId, token).getImage(id) { s -> imageUrl.value = s ?: "" };

    Box(modifier = Modifier.fillMaxWidth()) {
        // "https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg"
        content()
        CoilImage(model = imageUrl.value)
    }
}

@Preview
@Composable
fun figmaPreviewPreview() {
    //FigmaPreview(fileId = "d51xtGzfqgz1LXyZPDnP1E", id = "1:405", token = "162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
    FigmaPreview(fileId = "0ig8L2HER9Or5uC8ReDwVz", id = "329:3A0", token = "162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d") {
        Text("Hello")
    }
}