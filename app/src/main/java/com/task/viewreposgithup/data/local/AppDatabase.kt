package com.task.viewreposgithup.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.viewreposgithup.data.local.dao.RemoteKeysDao
import com.task.viewreposgithup.data.local.dao.RepoDao
import com.task.viewreposgithup.data.local.entity.RemoteKeysEntity
import com.task.viewreposgithup.data.local.entity.RepoEntity

/*
 * Main Room Database for the app.
 */
@Database(
    entities = [RepoEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    /*
     * Returns the Data Access Object for Repository entities.
     */
    abstract fun repoDao(): RepoDao

    /*
     * Returns the Data Access Object for RemoteKeys entities.
     */
    abstract fun remoteKeysDao(): RemoteKeysDao


}
