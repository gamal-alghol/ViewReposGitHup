package com.task.viewreposgithup.di

import com.task.viewreposgithup.data.repository.RepoRepositoryImpl
import com.task.viewreposgithup.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /*
     * Binds RepoRepositoryImpl implementation to RepoRepository interface.
     */
    @Binds
    @Singleton
    abstract fun bindRepoRepository(
        repoRepositoryImpl: RepoRepositoryImpl
    ): RepoRepository
}
