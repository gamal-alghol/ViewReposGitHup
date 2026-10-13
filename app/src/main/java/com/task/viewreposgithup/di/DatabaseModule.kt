package com.task.viewreposgithup.di

import android.content.Context
import androidx.room.Room
import com.task.viewreposgithup.data.local.AppDatabase
import com.task.viewreposgithup.data.local.dao.RemoteKeysDao
import com.task.viewreposgithup.data.local.dao.RepoDao
import com.task.viewreposgithup.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /*
     * Provides singleton instance of AppDatabase.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    /*
     * Provides RepoDao instance from AppDatabase.
     */
    @Provides
    fun provideRepoDao(database: AppDatabase): RepoDao {
        return database.repoDao()
    }

    /*
     * Provides RemoteKeysDao instance from AppDatabase.
     */
    @Provides
    fun provideRemoteKeysDao(database: AppDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}
