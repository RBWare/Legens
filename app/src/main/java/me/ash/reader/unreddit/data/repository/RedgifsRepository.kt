package me.ash.reader.unreddit.data.repository

import me.ash.reader.unreddit.data.remote.api.redgifs.RedgifsApi
import me.ash.reader.unreddit.data.remote.api.redgifs.RedgifsToken
import me.ash.reader.unreddit.data.remote.api.redgifs.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedgifsRepository @Inject constructor(
    private val redgifsApi: RedgifsApi,
    private val redgifsToken: RedgifsToken,
) {

    fun getRedgifsGif(id: String): Flow<Item> = flow {
        val authorization = redgifsToken.getAuthorization()
        emit(redgifsApi.getGif(authorization, id))
    }
}
