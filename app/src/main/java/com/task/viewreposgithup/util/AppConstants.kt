package com.task.viewreposgithup.util

import com.task.viewreposgithup.BuildConfig


object AppConstants {
    const val BASE_URL: String = BuildConfig.BASE_URL
    const val GITHUB_TOKEN: String = BuildConfig.GITHUB_TOKEN
    const val DEFAULT_ORG: String = BuildConfig.DEFAULT_ORG
    const val PAGE_SIZE: Int = 30
    const val PREFETCH_DISTANCE: Int = 3
    const val INITIAL_LOAD_SIZE: Int = 30
    const val FLOW_TIMEOUT_MILLIS: Long = 5000L
    const val DATABASE_NAME: String = "square_repos_db"


}
