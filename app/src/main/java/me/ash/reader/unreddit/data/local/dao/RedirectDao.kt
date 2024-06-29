package me.ash.reader.unreddit.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import me.ash.reader.unreddit.data.model.db.Redirect
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RedirectDao : BaseDao<Redirect> {

    @Query("SELECT * FROM redirect")
    abstract fun getAllRedirects(): Flow<List<Redirect>>
}
