package me.ash.reader.unreddit.util.extension

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import me.ash.reader.R
//import me.ash.reader.unreddit.UnredditApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.ash.reader.infrastructure.android.AndroidApp

fun AppCompatActivity.launchRepeat(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            block()
        }
    }
}

val Activity.androidApp: AndroidApp
    get() = application as AndroidApp

val FragmentActivity.currentNavigationFragment: Fragment?
    get() = supportFragmentManager.findFragmentById(R.id.fragment_container)
        ?.childFragmentManager
        ?.fragments
        ?.firstOrNull()
