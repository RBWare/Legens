package me.ash.reader.unreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import me.ash.reader.unreddit.data.local.RedditDatabase
import me.ash.reader.unreddit.data.model.Comment
import me.ash.reader.unreddit.data.model.Sorting
import me.ash.reader.unreddit.data.model.db.History
import me.ash.reader.unreddit.data.model.db.PostEntity
import me.ash.reader.unreddit.data.model.db.Profile
import me.ash.reader.unreddit.data.model.db.Subscription
import me.ash.reader.unreddit.data.remote.api.reddit.model.AboutChild
import me.ash.reader.unreddit.data.remote.api.reddit.model.AboutUserChild
import me.ash.reader.unreddit.data.remote.api.reddit.model.Child
import me.ash.reader.unreddit.data.remote.api.reddit.model.Listing
import me.ash.reader.unreddit.data.remote.api.reddit.model.MoreChildren
import me.ash.reader.unreddit.data.remote.api.reddit.source.CurrentSource
import me.ash.reader.unreddit.data.remote.datasource.CommentsDataSource
import me.ash.reader.unreddit.data.remote.datasource.SearchPostDataSource
import me.ash.reader.unreddit.data.remote.datasource.SearchSubredditDataSource
import me.ash.reader.unreddit.data.remote.datasource.SearchUserDataSource
import me.ash.reader.unreddit.data.remote.datasource.SmartPostListDataSource
import me.ash.reader.unreddit.data.remote.datasource.SubredditSearchPostDataSource
import me.ash.reader.unreddit.data.remote.datasource.UserPostsDataSource
import me.ash.reader.unreddit.di.DispatchersModule.DefaultDispatcher
import me.ash.reader.unreddit.di.DispatchersModule.MainImmediateDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostListRepository @Inject constructor(
    private val source: CurrentSource,
    private val redditDatabase: RedditDatabase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher
) {

    fun getPost(permalink: String, sorting: Sorting): Flow<List<Listing>> = flow {
        emit(source.getPost(permalink, sort = sorting.generalSorting))
    }

    fun getMoreChildren(children: String, linkId: String): Flow<MoreChildren> = flow {
        emit(source.getMoreChildren(children, linkId))
    }

    //region Subreddit

    fun getPosts(
        subreddit: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SmartPostListDataSource(
                source,
                listOf(subreddit),
                sorting,
                defaultDispatcher,
                mainImmediateDispatcher
            )
        }.flow
    }

    fun getPosts(
        subreddit: List<String>,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SmartPostListDataSource(
                source,
                subreddit,
                sorting,
                defaultDispatcher,
                mainImmediateDispatcher
            )
        }.flow
    }

    fun getSubredditInfo(subreddit: String): Flow<AboutChild> = flow {
        emit(source.getSubredditInfo(subreddit) as AboutChild)
    }

    //endregion

    //region Subscriptions

    fun getSubscriptions(profileId: Int): Flow<List<Subscription>> = redditDatabase
        .subscriptionDao().getSubscriptionsFromProfile(profileId).distinctUntilChanged()

    fun getSubscriptionsNames(profileId: Int): Flow<List<String>> {
        return redditDatabase.subscriptionDao().getSubscriptionsNamesFromProfile(profileId)
    }

    suspend fun subscribe(name: String, profileId: Int, icon: String? = null) {
        redditDatabase.subscriptionDao().insert(
            Subscription(name, System.currentTimeMillis(), icon, profileId)
        )
    }

    suspend fun unsubscribe(name: String, profileId: Int) {
        redditDatabase.subscriptionDao().deleteFromNameAndProfile(name, profileId)
    }

    //endregion

    //region User

    fun getUserPosts(
        user: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            UserPostsDataSource(source, user, sorting)
        }.flow
    }

    fun getUserComments(
        user: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            CommentsDataSource(source, user, sorting)
        }.flow
    }

    fun getUserInfo(user: String): Flow<AboutUserChild> = flow {
        emit(source.getUserInfo(user) as AboutUserChild)
    }

    //endregion

    //region Search

    fun searchPost(
        query: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SearchPostDataSource(source, query, sorting)
        }.flow
    }

    fun searchUser(
        query: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SearchUserDataSource(source, query, sorting)
        }.flow
    }

    fun searchSubreddit(
        query: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SearchSubredditDataSource(source, query, sorting)
        }.flow
    }

    fun searchInSubreddit(
        query: String,
        subreddit: String,
        sorting: Sorting,
        pageSize: Int = DEFAULT_LIMIT
    ): Flow<PagingData<Child>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            SubredditSearchPostDataSource(source, subreddit, query, sorting)
        }.flow
    }

    //endregion

    //region History

    fun getHistoryIds(profileId: Int): Flow<List<String>> {
        return redditDatabase.historyDao().getHistoryIdsFromProfile(profileId)
    }

    suspend fun insertPostInHistory(postId: String, profileId: Int) {
        redditDatabase.historyDao().upsert(History(postId, System.currentTimeMillis(), profileId))
    }

    //endregion

    //region Profile

    suspend fun addProfile(name: String) {
        redditDatabase.profileDao().insert(Profile(name = name))
    }

    suspend fun getProfile(id: Int): Profile {
        return redditDatabase.profileDao().getProfileFromId(id)
            ?: redditDatabase.profileDao().getFirstProfile()
    }

    fun getAllProfiles(): Flow<List<Profile>> {
        return redditDatabase.profileDao().getAllProfiles()
    }

    suspend fun deleteProfile(profileId: Int) {
        redditDatabase.profileDao().deleteFromId(profileId)
    }

    suspend fun updateProfile(profile: Profile) {
        redditDatabase.profileDao().update(profile)
    }

    //endregion

    //region Save

    suspend fun savePost(post: PostEntity, profileId: Int) {
        post.run {
            this.profileId = profileId
            this.time = System.currentTimeMillis()
            redditDatabase.postDao().upsert(this)
        }
    }

    suspend fun unsavePost(post: PostEntity, profileId: Int) {
        redditDatabase.postDao().deleteFromIdAndProfile(post.id, profileId)
    }

    fun getSavedPosts(profileId: Int): Flow<List<PostEntity>> {
        return redditDatabase.postDao().getSavedPostsFromProfile(profileId)
    }

    fun getSavedPostIds(profileId: Int): Flow<List<String>> {
        return redditDatabase.postDao().getSavedPostIdsFromProfile(profileId)
    }

    suspend fun saveComment(comment: Comment.CommentEntity, profileId: Int) {
        comment.run {
            this.profileId = profileId
            this.time = System.currentTimeMillis()
            redditDatabase.commentDao().upsert(comment)
        }
    }

    suspend fun unsaveComment(comment: Comment.CommentEntity, profileId: Int) {
        redditDatabase.commentDao().deleteFromIdAndProfile(comment.name, profileId)
    }

    fun getSavedComments(profileId: Int): Flow<List<Comment.CommentEntity>> {
        return redditDatabase.commentDao().getSavedCommentsFromProfile(profileId)
    }

    fun getSavedCommentIds(profileId: Int): Flow<List<String>> {
        return redditDatabase.commentDao().getSavedCommentIdsFromProfile(profileId)
    }

    //endregion

    companion object {
        private const val DEFAULT_LIMIT = 25
    }
}
