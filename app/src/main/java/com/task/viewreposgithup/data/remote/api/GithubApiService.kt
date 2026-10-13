package com.task.viewreposgithup.data.remote.api

import com.task.viewreposgithup.data.remote.dto.RepoDto
import com.task.viewreposgithup.util.AppConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
 * Retrofit REST API interface for GitHub API.
 */
interface GithubApiService {

    /*
     * Retrieves public repositories for an organization.
     * REST endpoint: https://api.github.com/orgs/square/repos
     *
     * @param org Organization name (e.g. "square")
     * @param page Page number for pagination (starts at 1)
     * @param perPage Items count per page (default: 20)
     */
    @GET("orgs/{org}/repos")
    suspend fun getOrgRepositories(
        @Path("org") org: String = AppConstants.DEFAULT_ORG,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppConstants.PAGE_SIZE,
        @Query("sort") sort: String = "full_name",
        @Query("direction") direction: String = "asc"
    ): List<RepoDto>
}
