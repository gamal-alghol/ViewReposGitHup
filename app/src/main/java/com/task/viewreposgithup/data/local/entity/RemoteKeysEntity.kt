package com.task.viewreposgithup.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entity for RemoteKeys used by Room Paging 3 RemoteMediator.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val repoId: Long,
    val prevPage: Int?,
    val nextPage: Int?
)
