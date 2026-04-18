package com.felipearpa.ui.lazy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * A `LazyColumn` that swaps between loading / empty / error / content slots.
 * @param modifier The modifier to apply to this layout.
 * @param lazyPagingItems The `LazyPagingItems` to display.
 * @param lazyListState The state of the `LazyColumn`.
 * @param contentPadding The padding to apply to the content.
 * @param reverseLayout Whether the items should be reversed.
 * @param verticalArrangement The vertical arrangement of the items.
 * @param loadingContent The content to display when the items are loading.
 * @param refreshLoadingContent The content to display when the items are loading.
 * @param errorContent The content to display when the items have an error.
 * @param emptyContent The content to display when the items are empty.
 * @param itemContent The content to display for each item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Item : Any> RefreshableLazyPagingColumn(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<Item>,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingColumnState: LazyPagingColumnState = rememberLazyPagingColumnState(
        lazyPagingItems,
    ),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    loadingContent: LazyListScope.() -> Unit = {},
    refreshLoadingContent: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
        }
    },
    errorContent: LazyListScope.(Throwable) -> Unit = {},
    emptyContent: LazyListScope.() -> Unit = {},
    itemContent: LazyListScope.() -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshLoadState = lazyPagingItems.loadState.refresh

    val onRefresh = {
        isRefreshing = true
        lazyPagingItems.refresh()
    }

    LaunchedEffect(refreshLoadState, lazyPagingColumnState) {
        if (isRefreshing && refreshLoadState !is LoadState.Loading) {
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        modifier = modifier,
        state = pullToRefreshState,
        onRefresh = onRefresh,
    ) {
        LazyPagingColumn(
            modifier = Modifier.fillMaxSize(),
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            loadingContent = loadingContent,
            refreshLoadingContent = {
                val shouldShowRefreshIndicator = !isRefreshing
                        && refreshLoadState is LoadState.Loading
                        && lazyPagingColumnState !is LazyPagingColumnState.Loading
                if (shouldShowRefreshIndicator) {
                    refreshLoadingContent()
                }
            },
            errorContent = errorContent,
            emptyContent = emptyContent,
            itemContent = itemContent,
        )
    }
}
