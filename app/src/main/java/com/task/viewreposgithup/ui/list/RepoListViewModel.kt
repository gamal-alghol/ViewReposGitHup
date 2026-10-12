package com.task.viewreposgithup.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.task.viewreposgithup.data.remote.dto.toDbSortKey
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
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Repositories List Screen.
 * Manages paged repositories data, language filtering, search query, sorting, and bookmark state updates.
 */
@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage: StateFlow<String?> = _selectedLanguage.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.NAME_ASC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val repositoriesPagingFlow: Flow<PagingData<Repo>> = combine(
        _searchQuery
            .debounce(300)
            .distinctUntilChanged(),
        _selectedLanguage,
        _sortOption
    ) { query, language, sort ->
        Triple(query, language, sort)
    }.flatMapLatest { (query, language, sort) ->
        getRepositoriesUseCase(
            language = language,
            searchQuery = query,
            sortBy = sort.toDbSortKey()
        )
    }.cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLanguageFilter(language: String?) {
        _selectedLanguage.value = language
    }

    fun setSortOption(sort: SortOption) {
        _sortOption.value = sort
    }

    fun toggleBookmark(repo: Repo) {
        viewModelScope.launch {
            toggleBookmarkUseCase(repo.id, !repo.isBookmarked)
        }
    }
}
