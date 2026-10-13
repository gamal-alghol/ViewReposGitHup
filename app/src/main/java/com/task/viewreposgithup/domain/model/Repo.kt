package com.task.viewreposgithup.domain.model


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
