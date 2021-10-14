package playground

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.viewinterop.AndroidView
import playground.render.GlCubeRenderer
import kotlin.random.Random

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun PlaygroundItem(modifier: Modifier, index: Int, offset: Float) {
    val background = remember(index) {
        Color.hsl(Random.nextFloat() * 255F, 0.8F, 0.2F)
    }
    Box(modifier) {
        AndroidView(
            factory = { PlaygroundItemView(it, background) },
            modifier = Modifier.fillMaxSize(),
            update = { it.setOffset(offset) }
        )
    }
}

@SuppressLint("ViewConstructor")
class PlaygroundItemView(context: Context, background: Color) : GLSurfaceView(context) {
    private val renderer = GlCubeRenderer(floatArrayOf(background.red, background.green, background.blue))
    
    init {
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun setOffset(offset: Float) {
        renderer.setOffset(offset)
        requestRender()
    }
}