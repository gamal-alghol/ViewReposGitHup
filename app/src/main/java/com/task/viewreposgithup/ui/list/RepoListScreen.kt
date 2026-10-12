package com.task.viewreposgithup.ui.list

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.task.viewreposgithup.R
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.model.SortOption
import com.task.viewreposgithup.ui.components.ErrorStateView
import com.task.viewreposgithup.ui.components.LanguageFilterBottomSheet
import com.task.viewreposgithup.ui.components.RepoItemCard
import com.task.viewreposgithup.ui.components.ShimmerRepoLoading
import com.task.viewreposgithup.ui.components.SortBottomSheet


/**
 * Repositories List Screen displaying paginated list of Square Inc repositories,
 * with state hoisting and scope isolation to prevent recomposition during typing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    onRepoClick: (Long) -> Unit,
    viewModel: RepoListViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.repositoriesPagingFlow.collectAsLazyPagingItems()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        LanguageFilterBottomSheet(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { language ->
                viewModel.setLanguageFilter(language)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showSortSheet) {
        SortBottomSheet(
            selectedSort = sortOption,
            onSortSelected = { option ->
                viewModel.setSortOption(option)
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false }
        )
    }

    Scaffold(
        topBar = {
            RepoListTopAppBar(
                viewModel = viewModel,
                onRefresh = { lazyPagingItems.refresh() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                FilterAndSortSection(
                    selectedLanguage = selectedLanguage,
                    sortOption = sortOption,
                    onFilterClick = { showFilterSheet = true },
                    onSortClick = { showSortSheet = true },
                    onClearFilter = { viewModel.setLanguageFilter(null) }
                )

                val hasActiveFilter = selectedLanguage != null

                RepoListContent(
                    lazyPagingItems = lazyPagingItems,
                    hasActiveFilter = hasActiveFilter,
                    onRepoClick = onRepoClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepoListTopAppBar(
    viewModel: RepoListViewModel,
    onRefresh: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isSearchActive = true
        }
    }

    TopAppBar(
        title = {
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 52.dp),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.search_hint),
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.app_subtitle),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(onClick = {
                isSearchActive = !isSearchActive
                if (!isSearchActive) {
                    viewModel.setSearchQuery("")
                }
            }) {
                Icon(
                    imageVector = if (isSearchActive) Icons.Default.Clear else Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.refresh_list)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.shadow(4.dp)
    )
}

@Composable
private fun FilterAndSortSection(
    selectedLanguage: String?,
    sortOption: SortOption,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onClearFilter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssistChip(
            onClick = onFilterClick,
            modifier = Modifier.weight(1f),
            label = {
                Text(
                    text = selectedLanguage ?: stringResource(id = R.string.filter_language),
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (selectedLanguage != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = selectedLanguage?.let {
                {
                    IconButton(
                        onClick = onClearFilter,
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear_filter),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        )

        AssistChip(
            onClick = onSortClick,
            modifier = Modifier.weight(1f),
            label = {
                Text(
                    text = stringResource(id = sortOption.titleRes),
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
private fun RepoListContent(
    lazyPagingItems: LazyPagingItems<Repo>,
    hasActiveFilter: Boolean,
    onRepoClick: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        when (val refreshState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                ShimmerRepoLoading()
            }

            is LoadState.Error -> {
                ErrorStateView(
                    errorMessage = refreshState.error.localizedMessage
                        ?: stringResource(id = R.string.something_went_wrong),
                    onRetry = { lazyPagingItems.retry() }
                )
            }

            else -> {
                if (lazyPagingItems.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_repositories_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { repo -> repo.id }
                        ) { index ->
                            val repo = lazyPagingItems[index]
                            if (repo != null) {
                                RepoItemCard(
                                    repo = repo,
                                    onClick = { onRepoClick(repo.id) }
                                )
                            }
                        }

                        // Append Loading indicator at end of list for pagination only when not filtering/searching
                        if (lazyPagingItems.loadState.append is LoadState.Loading && !hasActiveFilter) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
val SortOption.titleRes: Int
    @StringRes get() = when (this) {
        SortOption.NAME_ASC -> R.string.sort_name_asc
        SortOption.STARS_DESC -> R.string.sort_stars_desc
        SortOption.UPDATED_DESC -> R.string.sort_date_desc
    }