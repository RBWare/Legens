package me.ash.reader.unreddit.util.extension

import androidx.core.view.isVisible
import me.ash.reader.R
import me.ash.reader.databinding.IncludePostMetricsBinding

fun IncludePostMetricsBinding.setRatio(ratio: Int) {
    ratio.takeUnless { it == -1 }?.let {
        textPostRatio.run {
            isVisible = true
            text = context.getString(R.string.post_ratio, it)
        }
    } ?: run {
        textPostRatio.isVisible = false
    }
}
