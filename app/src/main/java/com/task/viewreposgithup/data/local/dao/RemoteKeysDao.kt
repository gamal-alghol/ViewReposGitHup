package com.task.viewreposgithup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.viewreposgithup.data.local.entity.RemoteKeysEntity

/*
 * Data Access Object for RemoteKeys.
 */
@Dao
interface RemoteKeysDao {
    /*
     * Inserts a list of remote keys into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    /*
     * Retrieves remote keys for a specific repository ID.
     */
    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun getRemoteKeysRepoId(repoId: Long): RemoteKeysEntity?

    /*
     * Clears all remote keys from the database.
     */
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
