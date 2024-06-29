package me.ash.reader.unreddit.data.local.mapper

import me.ash.reader.unreddit.data.model.db.SubredditEntity
import me.ash.reader.unreddit.data.remote.api.reddit.model.AboutData
import me.ash.reader.unreddit.di.DispatchersModule
import me.ash.reader.unreddit.util.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubredditMapper2 @Inject constructor(
    @DispatchersModule.DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : Mapper<AboutData, SubredditEntity>(defaultDispatcher) {

    private val htmlParser: HtmlParser = HtmlParser(defaultDispatcher)

    override suspend fun toEntity(from: AboutData): SubredditEntity {
        with(from) {
            return SubredditEntity(
                wikiEnabled ?: false,
                displayName,
                getHeader(),
                title,
                getPrimaryColor(),
                activeUserCount,
                getIcon(),
                subscribers,
                quarantine ?: false,
                htmlParser.separateHtmlBlocks(publicDescriptionHtml),
                getKeyColor(),
                getBackgroundColor(),
                over18 ?: false,
                htmlParser.separateHtmlBlocks(descriptionHtml),
                url,
                getTimeInMillis()
            )
        }
    }
}
