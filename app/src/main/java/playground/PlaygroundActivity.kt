package playground

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager

class PlaygroundActivity : ComponentActivity() {

    enum class View { Menu, Pager, LazyList }

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var view by remember { mutableStateOf(View.Menu) }
            BackHandler(enabled = view != View.Menu) {
                view = View.Menu
            }
            when (view) {
                View.Menu -> {
                    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                        Text("SurfaceView inside LazyList bug")
                        Spacer(Modifier.height(56.dp))
                        Button(onClick = { view = View.Pager }) { Text("(1) Accompanist Pager") }
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { view = View.LazyList }) { Text("(2) Lazy Column") }
                    }
                }
                View.Pager -> {
                    VerticalPager(
                        count = 15,
                        modifier = Modifier.fillMaxSize(),
                        key = { it }
                    ) {
                        PlaygroundItem(Modifier.fillMaxSize(), it, currentPageOffset)
                    }
                }
                View.LazyList -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(15, { it }) {
                            PlaygroundItem(Modifier.fillParentMaxSize(), it, 0F)
                        }
                    }
                }
            }
        }
    }
}