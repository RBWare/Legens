package me.ash.reader.unreddit.ui.postmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import me.ash.reader.NavigationGraphDirections
import me.ash.reader.unreddit.data.model.db.PostEntity
import me.ash.reader.databinding.FragmentPostMenuBinding
import me.ash.reader.unreddit.util.extension.doAndDismiss
import me.ash.reader.unreddit.util.extension.openExternalLink
import me.ash.reader.unreddit.util.extension.parcelable
import me.ash.reader.unreddit.util.extension.serializable
import me.ash.reader.unreddit.util.extension.shareExternalLink
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PostMenuFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPostMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.serializable(BUNDLE_KEY_TYPE) ?: MenuType.GENERAL
        binding.type = type

        val post = arguments?.parcelable<PostEntity>(BUNDLE_KEY_POST)
        post?.let {
            binding.post = it
            initActions(it)
        }
    }

    private fun initActions(post: PostEntity) {
        with(binding) {
            buttonUser.setOnClickListener {
                doAndDismiss {
                    findNavController().navigate(NavigationGraphDirections.openUser(post.author))
                }
            }

            buttonSubreddit.setOnClickListener {
                val subreddit = post.subreddit.removePrefix("r/")
                doAndDismiss {
                    findNavController().navigate(NavigationGraphDirections.openSubreddit(subreddit))
                }
            }

            buttonOpen.setOnClickListener {
                doAndDismiss { openExternalLink(post.url) }
            }

            buttonShareLink.setOnClickListener {
                doAndDismiss { shareExternalLink(post.url) }
            }

            buttonSharePost.setOnClickListener {
                val url = "https://www.reddit.com${post.permalink}"
                doAndDismiss { shareExternalLink(url, post.title) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class MenuType {
        GENERAL, SUBREDDIT, USER
    }

    companion object {
        private const val TAG = "PostMenuFragment"

        private const val BUNDLE_KEY_POST = "BUNDLE_KEY_POST"
        private const val BUNDLE_KEY_TYPE = "BUNDLE_KEY_TYPE"

        fun show(
            fragmentManager: FragmentManager,
            post: PostEntity,
            type: MenuType = MenuType.GENERAL
        ) {
            PostMenuFragment().apply {
                arguments = bundleOf(
                    BUNDLE_KEY_POST to post,
                    BUNDLE_KEY_TYPE to type
                )
            }.show(fragmentManager, TAG)
        }
    }
}
