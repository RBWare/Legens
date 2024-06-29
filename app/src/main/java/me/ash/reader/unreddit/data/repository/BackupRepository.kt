package me.ash.reader.unreddit.data.repository

import android.net.Uri
import me.ash.reader.unreddit.data.local.backup.BackupManager
import me.ash.reader.unreddit.data.local.backup.RedditBackupManager
import me.ash.reader.unreddit.data.local.backup.StealthBackupManager
import me.ash.reader.unreddit.data.model.Resource
import me.ash.reader.unreddit.data.model.backup.BackupType
import me.ash.reader.unreddit.data.model.backup.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupRepository @Inject constructor(
    private val stealthBackupManager: StealthBackupManager,
    private val redditBackupManager: RedditBackupManager
) {

    fun importProfiles(uri: Uri, type: BackupType): Flow<Resource<List<Profile>>> = flow {
        emit(Resource.Loading<List<Profile>>())

        val backupManager = getBackupManager(type)
        backupManager
            .import(uri)
            .onSuccess { profiles ->
                emit(Resource.Success(profiles))
            }
            .onFailure { e ->
                emit(Resource.Error<List<Profile>>(throwable = e))
            }
    }

    fun exportProfiles(uri: Uri, type: BackupType): Flow<Resource<List<Profile>>> = flow {
        emit(Resource.Loading<List<Profile>>())

        val backupManager = getBackupManager(type)
        backupManager
            .export(uri)
            .onSuccess { profiles ->
                emit(Resource.Success(profiles))
            }
            .onFailure { e ->
                emit(Resource.Error<List<Profile>>(throwable = e))
            }
    }

    private fun getBackupManager(type: BackupType): BackupManager {
        return when (type) {
            BackupType.STEALTH -> stealthBackupManager
            BackupType.REDDIT -> redditBackupManager
        }
    }
}
