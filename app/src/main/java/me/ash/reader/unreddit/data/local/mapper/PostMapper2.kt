package me.ash.reader.unreddit.data.local.mapper

import me.ash.reader.unreddit.data.model.Award
import me.ash.reader.unreddit.data.model.Block
import me.ash.reader.unreddit.data.model.Flair
import me.ash.reader.unreddit.data.model.PosterType
import me.ash.reader.unreddit.data.model.Sort
import me.ash.reader.unreddit.data.model.Sorting
import me.ash.reader.unreddit.data.model.db.PostEntity
import me.ash.reader.unreddit.data.remote.api.reddit.model.PostData
import me.ash.reader.unreddit.di.DispatchersModule.DefaultDispatcher
import me.ash.reader.unreddit.util.HtmlParser
import me.ash.reader.unreddit.util.extension.formatNumber
import me.ash.reader.unreddit.util.extension.toMillis
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.round

@Singleton
class PostMapper2 @Inject constructor(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : Mapper<PostData, PostEntity>(defaultDispatcher) {

    private val htmlParser: HtmlParser = HtmlParser(defaultDispatcher)

    override suspend fun toEntity(from: PostData): PostEntity {
        with(from) {
            val redditText = htmlParser.separateHtmlBlocks(selfTextHtml)
            val flair = Flair.fromData(linkFlairRichText, flair)
            return PostEntity(
                name,
                prefixedSubreddit,
                title,
                ratio?.run { round(ratio * 100).toInt() } ?: -1,
                totalAwards,
                isOC,
                flair,
                Flair.fromData(authorFlairRichText, authorFlair),
                isOver18 || isSpoiler || isOC || !flair.isEmpty() || isStickied || isArchived || isLocked,
                score.formatNumber(),
                postType,
                domain,
                isSelf,
                crossposts?.firstOrNull()?.let { toEntity(it) },
                selfTextHtml,
                Sorting(Sort.fromName(suggestedSort)),
                redditText,
                isOver18,
                previewUrl,
                (redditText.blocks.getOrNull(0)?.block as? Block.TextBlock)?.text,
                awardings.sortedByDescending { it.count }.map { Award(it.count, it.getIcon()) },
                isSpoiler,
                isArchived,
                isLocked,
                PosterType.fromDistinguished(distinguished),
                author,
                commentsNumber.formatNumber(),
                permalink,
                isStickied,
                url,
                created.toMillis(),
                mediaType,
                mediaUrl,
                gallery,
                seen = false,
                saved = false,
                crosspostScrap = crosspost
            )
        }
    }
}
