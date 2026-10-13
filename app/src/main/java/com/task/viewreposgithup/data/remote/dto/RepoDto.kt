package com.task.viewreposgithup.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.task.viewreposgithup.domain.model.SortOption

/*
 * Data Transfer Object representing GitHub repository API response.
 */

@Keep
data class RepoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("owner") val owner: OwnerDto?,
    @SerializedName("updated_at") val updatedAt: String?
)
