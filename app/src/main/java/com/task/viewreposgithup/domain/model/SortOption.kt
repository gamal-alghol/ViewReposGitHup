package com.task.viewreposgithup.domain.model

import com.task.viewreposgithup.R

/**
 * Sorting options for repositories list.
 */
enum class SortOption(val titleRes: Int, val dbSortKey: String) {
    NAME_ASC(R.string.sort_name_asc, "NAME"),
    STARS_DESC(R.string.sort_stars_desc, "STARS"),
    UPDATED_DESC(R.string.sort_date_desc, "DATE")
}
