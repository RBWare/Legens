package me.ash.reader.unreddit.ui.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import me.ash.reader.unreddit.ui.common.PostDividerItemDecoration

class PostRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        addItemDecoration(PostDividerItemDecoration(context))
        isVerticalScrollBarEnabled = false
    }
}
