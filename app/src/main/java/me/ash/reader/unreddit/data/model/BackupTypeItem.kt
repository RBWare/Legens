package me.ash.reader.unreddit.data.model

import me.ash.reader.unreddit.data.model.backup.BackupType

data class BackupTypeItem(
    val type: BackupType,

    var selected: Boolean = false
)
