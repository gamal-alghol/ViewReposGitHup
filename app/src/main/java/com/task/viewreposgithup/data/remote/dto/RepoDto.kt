package com.task.viewreposgithup.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.task.viewreposgithup.domain.model.SortOption

/**
 * Data Transfer Object representing GitHub repository API response.
 */
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
fun SortOption.toDbSortKey(): String = when (this) {

    SortOption.NAME_ASC -> "NAME"

    SortOption.STARS_DESC -> "STARS"

    SortOption.UPDATED_DESC -> "DATE"

}