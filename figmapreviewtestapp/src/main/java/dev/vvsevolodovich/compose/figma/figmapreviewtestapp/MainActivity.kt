package dev.vvsevolodovich.compose.figma.figmapreviewtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dev.vvsevolodovich.compose.figma.figmapreviewtestapp.ui.theme.ComposeFigmaPreviewTheme
import dev.vvsevolodovich.compose.figma.preview.figmaPreviewPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeFigmaPreviewTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    figmaPreviewPreview()
                }
            }
        }
    }
}