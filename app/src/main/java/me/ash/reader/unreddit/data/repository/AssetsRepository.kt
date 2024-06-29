package me.ash.reader.unreddit.data.repository

import android.content.Context
import me.ash.reader.R
import me.ash.reader.unreddit.data.model.ServiceExternal
import me.ash.reader.unreddit.di.DispatchersModule.IoDispatcher
import me.ash.reader.unreddit.di.NetworkModule.BasicMoshi
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import okio.use
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetsRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @BasicMoshi private val moshi: Moshi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getServiceInstances(): Result<List<ServiceExternal>> {
        val adapter = moshi.adapter<List<ServiceExternal>>(
            Types.newParameterizedType(List::class.java, ServiceExternal::class.java)
        )

        return runCatching {
            withContext(ioDispatcher) {
                appContext.resources.openRawResource(R.raw.instances).use { inputStream ->
                    inputStream.source().buffer().use { bufferedSource ->
                        adapter.fromJson(bufferedSource)
                    }
                } ?: emptyList()
            }
        }
    }
}
