package me.ash.reader.unreddit.ui.subscriptions

import me.ash.reader.unreddit.data.model.db.Subscription
import me.ash.reader.unreddit.data.repository.PostListRepository
import me.ash.reader.unreddit.data.repository.PreferencesRepository
import me.ash.reader.unreddit.di.DispatchersModule.DefaultDispatcher
import me.ash.reader.unreddit.ui.base.BaseViewModel
import me.ash.reader.unreddit.util.extension.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository,
    repository: PostListRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel(preferencesRepository, repository) {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    val filteredSubscriptions: Flow<List<Subscription>> = combine(
        subscriptions,
        _searchQuery
    ) { subscriptions, searchQuery ->
        subscriptions.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }.flowOn(defaultDispatcher)

    fun setSearchQuery(query: String) {
        _searchQuery.updateValue(query)
    }
}
