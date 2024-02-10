package me.ash.reader.ui.page.settings.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.ash.reader.domain.service.OpmlService
import me.ash.reader.domain.service.RssService
import me.ash.reader.infrastructure.android.AndroidStringsHelper
import me.ash.reader.infrastructure.rss.RssHelper
import javax.inject.Inject

@HiltViewModel
class AddFilterViewModel @Inject constructor(
    private val androidStringsHelper: AndroidStringsHelper,
) : ViewModel() {

    private val _addFilterUiState = MutableStateFlow(AddFilterUiState())
    val addFilterUiState: StateFlow<AddFilterUiState> = _addFilterUiState.asStateFlow()

    fun showAddFilterDialog() {
        _addFilterUiState.update {
            it.copy(
                addFilterDialogVisible = true,
            )
        }
    }

    fun hideAddFilterDialog() {
        _addFilterUiState.update {
            it.copy(
                addFilterDialogVisible = false,
            )
        }
    }
}

data class AddFilterUiState(
    val addFilterDialogVisible: Boolean = false,
)
