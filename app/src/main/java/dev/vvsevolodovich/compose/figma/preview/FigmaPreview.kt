package dev.vvsevolodovich.compose.figma.preview

import androidx.compose.foundation.layout.*
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

@Preview
@Composable
fun figmaPreviewPreview() {
    //FigmaPreview(fileId = "d51xtGzfqgz1LXyZPDnP1E", id = "1:405", token = "162186-4cca5a91-d2ba-4c17-838d-4ea63db2483d")
    FigmaPreview(fileId = "VU6toX6SDF75pYvhnoodez", id = "1:5", token = "305276-2689324b-e41d-4be5-a4b5-c0c3af1eb358") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button({},
                Modifier.padding(24.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color(0x00D37777),
                    )) {
                Text("TOUCH ME", color = Color.Black)
            }
        }
    }
}