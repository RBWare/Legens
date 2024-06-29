package me.ash.reader.unreddit.data.local.mapper

import me.ash.reader.unreddit.data.model.Block
import me.ash.reader.unreddit.data.model.Comment.CommentEntity
import me.ash.reader.unreddit.data.model.SavedItem
import me.ash.reader.unreddit.data.model.db.PostEntity
import me.ash.reader.unreddit.di.DispatchersModule
import me.ash.reader.unreddit.util.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedMapper2 @Inject constructor(
    @DispatchersModule.DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : Mapper<Any, SavedItem>(defaultDispatcher) {

    private val htmlParser: HtmlParser = HtmlParser(defaultDispatcher)

    override suspend fun toEntity(from: Any): SavedItem {
        throw UnsupportedOperationException()
    }

    override suspend fun toEntities(from: List<Any>): List<SavedItem> {
        throw UnsupportedOperationException()
    }

    suspend fun dataToEntity(data: PostEntity): SavedItem = withContext(defaultDispatcher) {
        val redditText = htmlParser.separateHtmlBlocks(data.selfTextHtml)
        SavedItem.Post(
            data.apply {
                hasFlairs = isOver18 || isSpoiler || isOC || isStickied || isArchived || isLocked
                selfRedditText = redditText
                previewText = (redditText.blocks.firstOrNull()?.block as? Block.TextBlock)?.text
            }
        )
    }

    suspend fun dataToEntity(data: CommentEntity): SavedItem = withContext(defaultDispatcher) {
        SavedItem.Comment(
            data.apply {
                body = htmlParser.separateHtmlBlocks(bodyHtml)
            }
        )
    }

    suspend fun postsToEntities(data: List<PostEntity>): List<SavedItem> = withContext(
        defaultDispatcher
    ) {
        data.map { dataToEntity(it) }
    }

    suspend fun commentsToEntities(data: List<CommentEntity>): List<SavedItem> =
        withContext(defaultDispatcher) {
            data.map { dataToEntity(it) }
        }
}
