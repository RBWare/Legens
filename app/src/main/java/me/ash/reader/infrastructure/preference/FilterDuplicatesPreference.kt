package me.ash.reader.infrastructure.preference

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.ash.reader.ui.ext.dataStore
import me.ash.reader.ui.ext.put
import me.ash.reader.ui.ext.DataStoreKey
import me.ash.reader.ui.ext.DataStoreKey.Companion.filterDuplicates

sealed class FilterDuplicatesPreference(val value: Boolean) : Preference() {
    object ON : FilterDuplicatesPreference(true)
    object OFF : FilterDuplicatesPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(
                DataStoreKey.Companion.filterDuplicates,
                value
            )
        }
    }

    companion object {

        val default = OFF
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[filterDuplicates]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun FilterDuplicatesPreference.not(): FilterDuplicatesPreference =
    when (value) {
        true -> FilterDuplicatesPreference.OFF
        false -> FilterDuplicatesPreference.ON
    }
