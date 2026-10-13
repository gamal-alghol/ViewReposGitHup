package com.task.viewreposgithup.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.task.viewreposgithup.data.local.dao.toDbSortKey
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.model.SortOption
import com.task.viewreposgithup.domain.usecase.GetRepositoriesUseCase
import com.task.viewreposgithup.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
  ViewModel for the Repositories List Screen.
  Manages paged repositories data, language filtering, search query, sorting, and bookmark state updates.
 */



@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(RepoListUiState())
    val uiState: StateFlow<RepoListUiState> = _uiState.asStateFlow()


        @OptIn(ExperimentalCoroutinesApi::class)
        val repositoriesPagingFlow: Flow<PagingData<Repo>> = _uiState
            .map { state -> Triple(state.searchQuery, state.selectedLanguage, state.sortOption) }
            .distinctUntilChanged()
            .flatMapLatest { (query, language, sort) ->
                getRepositoriesUseCase(
                    language = language,
                    searchQuery = query,
                    sortBy = sort.toDbSortKey()
                )
            }.cachedIn(viewModelScope)


         // Sets the search query for filtering repositories.

        fun setSearchQuery(query: String) {
            _uiState.update { it.copy(searchQuery = query) }
        }


        // Sets the language filter for repositories.

        fun setLanguageFilter(language: String?) {
            _uiState.update { it.copy(selectedLanguage = language) }
        }


        // Sets the sort option for repositories.

        fun setSortOption(sort: SortOption) {
            _uiState.update { it.copy(sortOption = sort) }
        }


         // Sets visibility of language filter bottom sheet.

        fun setFilterSheetVisible(isVisible: Boolean) {
            _uiState.update { it.copy(isFilterSheetVisible = isVisible) }
        }

        // Sets visibility of sort bottom sheet.

        fun setSortSheetVisible(isVisible: Boolean) {
            _uiState.update { it.copy(isSortSheetVisible = isVisible) }
        }

}
