package playground

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.viewinterop.AndroidView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PlaygroundActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BuggyLayout() }
    }

    /** A SurfaceView that draws a color. Could be anything else - camera preview, video, ... */
    private class ColoredSurfaceView(context: Context, val color: Int) : GLSurfaceView(context) {
        init {
            setRenderer(object : Renderer {
                override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {}
                override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {}
                override fun onDrawFrame(gl: GL10) {
                    gl.glClearColor(
                        android.graphics.Color.red(color) / 255F,
                        android.graphics.Color.green(color) / 255F,
                        android.graphics.Color.blue(color) / 255F,
                        1F
                    )
                    gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
                }
            })
            renderMode = RENDERMODE_CONTINUOUSLY
        }
    }

    /**
     * Replacing this with a pure-compose node (like Box + Text) avoids the issue.
     * Also removing the SurfaceView avoids the issue.
     * Also removing the wrapper FrameLayout (= SurfaceView only) avoids the issue.
     */
    @Composable
    fun BuggyItem(initialColor: Color, modifier: Modifier = Modifier) {
        AndroidView(
            modifier = modifier,
            factory = {
                FrameLayout(it).apply {
                    addView(
                        ColoredSurfaceView(it, initialColor.toArgb()),
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        )
    }

    @Composable
    fun BuggyLayout() {
        Layout(
            content = {
                BuggyItem(Color.Red)
                BuggyItem(Color.Green)
            },
            modifier = Modifier.fillMaxSize(),
            measurePolicy = { measurables, constraints ->
                val w = constraints.maxWidth
                val h = constraints.maxHeight / 2
                val cc = Constraints(w, w, h, h)
                val pl = measurables.map { it.measure(cc) }
                layout(constraints.maxWidth, constraints.maxHeight) {
                    pl[0].placeWithLayer(0, 0)
                    // pl[1].placeWithLayer(0, h)
                }
            }
        )
    }

    /** SubcomposeLayout reproduces the issue as well. */
    @Composable
    fun BuggySubcomposeLayout() {
        SubcomposeLayout(Modifier.fillMaxSize()) { constraints ->
            val w = constraints.maxWidth
            val h = constraints.maxHeight / 2
            val cc = Constraints(w, w, h, h)
            val pl = listOf(
                subcompose(0) { BuggyItem(Color.Red) }.single().measure(cc),
                subcompose(1) { BuggyItem(Color.Green) }.single().measure(cc)
            )
            layout(constraints.maxWidth, constraints.maxHeight) {
                pl[0].placeWithLayer(0, 0)
                // pl[1].placeWithLayer(0, h)
            }
        }
    }
}