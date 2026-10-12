package com.task.viewreposgithup.domain.model

/**
 * Domain model representing a GitHub Repository.
 *
 * @property id Unique identifier of the repository
 * @property name Repository name
 * @property stars Number of stargazers count
 * @property description Short description of the repository
 * @property language Main programming language used
 * @property isBookmarked Local bookmark state flag
 * @property htmlUrl Direct URL to the GitHub repository web page
 * @property ownerName GitHub login/name of the owner
 * @property ownerAvatarUrl Avatar URL of the repository owner
 * @property updatedAt Last updated timestamp
 */
data class Repo(
    val id: Long,
    val name: String,
    val stars: Int,
    val description: String?,
    val language: String?,
    val isBookmarked: Boolean = false,
    val htmlUrl: String? = null,
    val ownerName: String? = "square",
    val ownerAvatarUrl: String? = null,
    val updatedAt: String? = null
)
