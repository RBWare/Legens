package me.ash.reader.unreddit.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import me.ash.reader.unreddit.data.model.db.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SubscriptionDao : BaseDao<Subscription> {

    @Query("DELETE FROM subscription WHERE name = :name AND profile_id = :profileId")
    abstract suspend fun deleteFromNameAndProfile(name: String, profileId: Int): Int

    @Query("DELETE FROM subscription WHERE profile_id = :profileId")
    abstract suspend fun deleteFromProfile(profileId: Int): Int

    @Query("SELECT * FROM subscription WHERE profile_id = :profileId")
    abstract fun getSubscriptionsFromProfile(profileId: Int): Flow<List<Subscription>>

    @Query("SELECT name FROM subscription WHERE profile_id = :profileId")
    abstract fun getSubscriptionsNamesFromProfile(profileId: Int): Flow<List<String>>
}
