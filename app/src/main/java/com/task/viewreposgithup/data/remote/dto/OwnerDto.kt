package com.task.viewreposgithup.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for repository owner info from GitHub API.
 */
data class OwnerDto(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
)
