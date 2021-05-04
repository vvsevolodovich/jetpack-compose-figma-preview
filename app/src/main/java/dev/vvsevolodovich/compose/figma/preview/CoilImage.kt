package dev.vvsevolodovich.compose.figma.preview

import android.graphics.drawable.Drawable
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.size.Scale
import coil.target.Target
import kotlinx.coroutines.*

@Composable
fun CoilImage(
    model: Any,
    modifier: Modifier = Modifier,
    customize: ImageRequest.Builder.() -> Unit = {}
) {
    var width = 300
    var height = 300
    val image = remember<MutableState<ImageBitmap>> { mutableStateOf(ImageBitmap(width, height)) }

    val target = object : Target {
        override fun onSuccess(result: Drawable) {
            image.update(result)
        }
    }

    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(model)
        .size(width, height)
        .scale(Scale.FILL)
        .target(target)
        .apply { customize(this) }

    val requestDisposable = Coil.imageLoader(context).enqueue(request.build())

    Image(bitmap = image.value, modifier = modifier, contentDescription = "test", alpha = 0.4f)

    DisposableEffect(key1 = "dispose1", effect = {
        image.value = ImageBitmap(width, height)
        onDispose {
            requestDisposable.dispose()
        }
    })
}

internal fun MutableState<ImageBitmap>.update(
    drawable: Drawable,
    @Px width: Int? = null,
    @Px height: Int? = null
): Job? {
    value = drawable.toBitmap(
        width = width ?: drawable.intrinsicWidth,
        height = height ?: drawable.intrinsicHeight
    ).asImageBitmap()
    return null
}