package com.task.viewreposgithup.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.util.AppConstants

/*
 * Room Database Entity for storing GitHub repositories locally.
 */
@Entity(tableName = "repositories")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val stargazersCount: Int,
    val description: String?,
    val language: String?,
    val isBookmarked: Boolean = false,
    val htmlUrl: String?,
    val ownerName: String?,
    val ownerAvatarUrl: String?,
    val updatedAt: String?
) {
    /*
     * Maps Room Entity to Domain Model.
     */
    fun toDomain(): Repo {
        return Repo(
            id = id,
            name = name,
            stars = stargazersCount,
            description = description,
            language = language,
            isBookmarked = isBookmarked,
            htmlUrl = htmlUrl,
            ownerName = ownerName ?: AppConstants.DEFAULT_ORG,
            ownerAvatarUrl = ownerAvatarUrl,
            updatedAt = updatedAt
        )
    }
}
