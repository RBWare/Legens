package me.ash.reader.unreddit.data.remote.api.reddit.source

import me.ash.reader.unreddit.data.model.Sort
import me.ash.reader.unreddit.data.model.TimeSorting
import me.ash.reader.unreddit.data.remote.api.reddit.RedditApi
import me.ash.reader.unreddit.data.remote.api.reddit.model.AboutChildJsonAdapter
import me.ash.reader.unreddit.data.remote.api.reddit.model.AboutUserChildJsonAdapter
import me.ash.reader.unreddit.data.remote.api.reddit.model.Child
import me.ash.reader.unreddit.data.remote.api.reddit.model.Listing
import me.ash.reader.unreddit.data.remote.api.reddit.model.ListingJsonAdapter
import me.ash.reader.unreddit.data.remote.api.reddit.model.MoreChildren
import me.ash.reader.unreddit.data.remote.api.reddit.model.MoreChildrenJsonAdapter
import me.ash.reader.unreddit.di.DispatchersModule.IoDispatcher
import me.ash.reader.unreddit.di.NetworkModule.RedditMoshi
import me.ash.reader.unreddit.di.NetworkModule.RedditOfficial
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditSource @Inject constructor(
    @RedditOfficial private val redditApi: RedditApi,
    @RedditMoshi moshi: Moshi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseRedditSource {

    private val listingAdapter = ListingJsonAdapter(moshi)
    private val listListingJsonAdapter = moshi.adapter<List<Listing>>(
        Types.newParameterizedType(List::class.java, Listing::class.java)
    )
    private val aboutChildAdapter = AboutChildJsonAdapter(moshi)
    private val aboutUserChildAdapter = AboutUserChildJsonAdapter(moshi)
    private val moreChildrenAdapter = MoreChildrenJsonAdapter(moshi)

    override suspend fun getSubreddit(
        subreddit: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.getSubreddit(subreddit, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun getSubredditInfo(subreddit: String): Child = withContext(ioDispatcher) {
        val response = redditApi.getSubredditInfo(subreddit)
        aboutChildAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun searchInSubreddit(
        subreddit: String,
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.searchInSubreddit(subreddit, query, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun getPost(permalink: String, limit: Int?, sort: Sort): List<Listing> =
        withContext(ioDispatcher) {
            val response = redditApi.getPost(permalink, limit, sort)
            listListingJsonAdapter.fromJson(response.source()) ?: throw IOException()
        }

    override suspend fun getMoreChildren(children: String, linkId: String): MoreChildren =
        withContext(ioDispatcher) {
            val response = redditApi.getMoreChildren(children, linkId)
            moreChildrenAdapter.fromJson(response.source()) ?: throw IOException()
        }

    override suspend fun getUserInfo(user: String): Child = withContext(ioDispatcher) {
        val response = redditApi.getUserInfo(user)
        aboutUserChildAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun getUserPosts(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.getUserPosts(user, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun getUserComments(
        user: String,
        sort: Sort,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.getUserComments(user, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun searchPost(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.searchPost(query, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun searchUser(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.searchUser(query, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }

    override suspend fun searchSubreddit(
        query: String,
        sort: Sort?,
        timeSorting: TimeSorting?,
        after: String?
    ): Listing = withContext(ioDispatcher) {
        val response = redditApi.searchSubreddit(query, sort, timeSorting, after)
        listingAdapter.fromJson(response.source()) ?: throw IOException()
    }
}
