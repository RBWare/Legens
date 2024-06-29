package me.ash.reader.unreddit.data.remote.datasource

import me.ash.reader.unreddit.data.model.Sorting
import me.ash.reader.unreddit.data.remote.api.reddit.model.Listing
import me.ash.reader.unreddit.data.remote.api.reddit.source.CurrentSource

class SearchPostDataSource(
    private val source: CurrentSource,
    query: String,
    sorting: Sorting
) : PostListDataSource(source, query, sorting) {

    override suspend fun getResponse(query: String, sorting: Sorting, after: String?): Listing {
        return source.searchPost(query, sorting.generalSorting, sorting.timeSorting, after)
    }
}
