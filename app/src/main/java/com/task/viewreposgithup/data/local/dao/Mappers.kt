package com.task.viewreposgithup.data.local.dao

import com.task.viewreposgithup.domain.model.SortOption

/*
 * Maps SortOption domain enum to database sort key string.
 */
fun SortOption.toDbSortKey(): String = when (this) {

    SortOption.NAME_ASC -> "NAME"

    SortOption.STARS_DESC -> "STARS"

    SortOption.UPDATED_DESC -> "DATE"

}