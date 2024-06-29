package me.ash.reader.unreddit.data.repository

import me.ash.reader.unreddit.data.remote.api.streamable.StreamableApi
import me.ash.reader.unreddit.data.remote.api.streamable.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamableRepository @Inject constructor(private val streamableApi: StreamableApi) {

    fun getVideo(shortcode: String): Flow<Video> = flow {
        emit(streamableApi.getVideo(shortcode))
    }
}
