package com.task.viewreposgithup.ui.list

import com.task.viewreposgithup.domain.model.SortOption

data class RepoListUiState(
    val searchQuery: String = "",
    val selectedLanguage: String? = null,
    val sortOption: SortOption = SortOption.NAME_ASC,
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false
)
