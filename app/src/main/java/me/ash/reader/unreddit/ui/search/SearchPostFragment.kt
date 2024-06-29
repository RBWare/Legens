package me.ash.reader.unreddit.ui.search

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import me.ash.reader.R
import me.ash.reader.unreddit.data.model.db.PostEntity
import me.ash.reader.unreddit.data.repository.PostListRepository
import me.ash.reader.unreddit.ui.common.fragment.PagingListFragment
import me.ash.reader.unreddit.ui.postlist.PostListAdapter
import me.ash.reader.unreddit.util.extension.launchRepeat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchPostFragment : PagingListFragment<PostListAdapter, PostEntity>() {

    override val viewModel: SearchViewModel by hiltNavGraphViewModels(R.id.search)

    override val flow: Flow<PagingData<PostEntity>>
        get() = viewModel.postDataFlow

    override val showItemDecoration: Boolean
        get() = true

    @Inject
    lateinit var repository: PostListRepository

    override fun bindViewModel() {
        super.bindViewModel()
        launchRepeat(Lifecycle.State.STARTED) {
            launch {
                viewModel.contentPreferences.collect {
                    adapter.contentPreferences = it
                }
            }

            launch {
                viewModel.lastRefreshPost.collect {
                    setRefreshTime(it)
                }
            }
        }
    }

    override fun createPagingAdapter(): PostListAdapter {
        return PostListAdapter(repository, this, this)
    }
}
