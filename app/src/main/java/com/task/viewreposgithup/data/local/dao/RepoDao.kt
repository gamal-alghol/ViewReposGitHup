package com.task.viewreposgithup.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.viewreposgithup.data.local.entity.RepoEntity
import kotlinx.coroutines.flow.Flow

/*
 * Data Access Object for Repository entity.
 */
@Dao
interface RepoDao {

    /*
     * Retrieves filtered and sorted repositories as a PagingSource based on language, search query, and sort criteria.
     */
    @Query("""
        SELECT * FROM repositories 
        WHERE (:language IS NULL OR language = :language)
        AND (:searchQuery = '' OR name LIKE '%' || :searchQuery || '%' OR (description IS NOT NULL AND description LIKE '%' || :searchQuery || '%'))
        ORDER BY 
        CASE WHEN :sortBy = 'STARS' THEN stargazersCount END DESC,
        CASE WHEN :sortBy = 'DATE' THEN updatedAt END DESC,
        CASE WHEN :sortBy = 'NAME' THEN name END COLLATE NOCASE ASC
    """)
    fun getFilteredRepositories(
        language: String?,
        searchQuery: String,
        sortBy: String
    ): PagingSource<Int, RepoEntity>

    /*
     * Retrieves all repositories paged ordered by name.
     */
    @Query("""
    SELECT * FROM repositories
    ORDER BY name COLLATE NOCASE ASC, id ASC
""")
    fun getRepositoriesPaged(): PagingSource<Int, RepoEntity>

    /*
     * Reactively observes a single repository entity by its ID.
     */
    @Query("SELECT * FROM repositories WHERE id = :repoId")
    fun getRepositoryById(repoId: Long): Flow<RepoEntity?>

    /*
     * Retrieves a single repository entity by its ID synchronously.
     */
    @Query("SELECT * FROM repositories WHERE id = :repoId")
    suspend fun getRepositoryByIdSync(repoId: Long): RepoEntity?

    /*
     * Returns the total count of repositories in the local database.
     */
    @Query("SELECT COUNT(*) FROM repositories")
    suspend fun getCount(): Int

    /*
     * Inserts a list of repository entities into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<RepoEntity>)

    /*
     * Updates the bookmark state of a specific repository.
     */
    @Query("UPDATE repositories SET isBookmarked = :isBookmarked WHERE id = :repoId")
    suspend fun updateBookmarkState(repoId: Long, isBookmarked: Boolean)

    /*
     * Clears all non-bookmarked repositories from the database.
     */
    @Query("DELETE FROM repositories WHERE isBookmarked = 0")
    suspend fun clearNonBookmarked()
}
