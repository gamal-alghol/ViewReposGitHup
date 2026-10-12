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

    // GitHub Programming Language Colors
    val LANGUAGE_COLORS: Map<String, Long> = mapOf(
        "Kotlin" to 0xFFA97BFF,
        "Java" to 0xFFB07219,
        "Swift" to 0xFFF05138,
        "TypeScript" to 0xFF3178C6,
        "JavaScript" to 0xFFF1E05A,
        "Python" to 0xFF3572A5,
        "C++" to 0xFFF34B7D,
        "C#" to 0xFF178600,
        "Ruby" to 0xFF701516,
        "Go" to 0xFF00ADD8,
        "Rust" to 0xFFDEA584,
        "HTML" to 0xFFE34C26,
        "CSS" to 0xFF563D7C,
        "Shell" to 0xFF89E051
    )
}
