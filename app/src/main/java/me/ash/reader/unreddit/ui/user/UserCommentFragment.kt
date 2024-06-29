package me.ash.reader.unreddit.ui.user

import androidx.fragment.app.FragmentTransaction
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import me.ash.reader.R
import me.ash.reader.unreddit.data.model.Comment
import me.ash.reader.unreddit.ui.commentmenu.CommentMenuFragment
import me.ash.reader.unreddit.ui.common.fragment.PagingListFragment
import me.ash.reader.unreddit.ui.postdetails.PostDetailsFragment
import me.ash.reader.unreddit.util.extension.launchRepeat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserCommentFragment : PagingListFragment<UserCommentsAdapter, Comment>(),
    UserCommentsAdapter.CommentClickListener {

    override val viewModel: UserViewModel by hiltNavGraphViewModels(R.id.user)

    override val flow: Flow<PagingData<Comment>>
        get() = viewModel.commentDataFlow

    override val showItemDecoration: Boolean
        get() = true

    override fun bindViewModel() {
        super.bindViewModel()
        launchRepeat(Lifecycle.State.STARTED) {
            launch {
                viewModel.lastRefreshComment.collect {
                    setRefreshTime(it)
                }
            }
        }
    }

    override fun createPagingAdapter(): UserCommentsAdapter {
        return UserCommentsAdapter(requireContext(), this, this)
    }

    override fun onClick(comment: Comment.CommentEntity) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(
                R.id.fragment_container,
                PostDetailsFragment.newInstance(comment.permalink),
                PostDetailsFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onLongClick(comment: Comment.CommentEntity) {
        CommentMenuFragment.show(childFragmentManager, comment, CommentMenuFragment.MenuType.USER)
    }
}
