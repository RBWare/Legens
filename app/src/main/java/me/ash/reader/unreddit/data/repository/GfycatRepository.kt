package me.ash.reader.unreddit.data.repository

import me.ash.reader.unreddit.data.remote.api.gfycat.GfycatApi
import me.ash.reader.unreddit.data.remote.api.gfycat.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GfycatRepository @Inject constructor(
    private val gfycatApi: GfycatApi
) {

    fun getGfycatGif(id: String): Flow<Item> = flow {
        emit(gfycatApi.getGif(id))
    }
}
