package playground.library

import android.app.Activity
import android.os.Bundle
import android.util.Log

class LibraryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = DataClass(0)
        Log.e("LibraryActivity", "Got data $data")
    }
}
