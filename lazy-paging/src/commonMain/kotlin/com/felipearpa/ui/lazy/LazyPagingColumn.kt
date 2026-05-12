package com.felipearpa.ui.lazy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
 * @param prependLoadingContent Content shown above the items while a prepend page is loading.
 * Intended for `enablePlaceholders = false`, where the list ends at the last loaded row and there
 * is no placeholder to indicate further loading; with placeholders enabled, leave this empty.
 * @param appendLoadingContent Content shown below the items while an append page is loading.
 * Intended for `enablePlaceholders = false`, where the list ends at the last loaded row and there
 * is no placeholder to indicate further loading; with placeholders enabled, leave this empty.
 * @param prependErrorContent Content shown above the items when a prepend page fails.
 * Intended for `enablePlaceholders = false`; with placeholders enabled, surface the error in the
 * placeholder row instead and leave this empty.
 * @param appendErrorContent Content shown below the items when an append page fails.
 * Intended for `enablePlaceholders = false`; with placeholders enabled, surface the error in the
 * placeholder row instead and leave this empty.
 * @param itemContent The content to display for each item.
 */
@Composable
fun <Item : Any> LazyPagingColumn(
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
    refreshLoadingContent: @Composable () -> Unit = {},
    errorContent: LazyListScope.(Throwable) -> Unit = {},
    emptyContent: LazyListScope.() -> Unit = {},
    prependLoadingContent: LazyListScope.() -> Unit = {},
    appendLoadingContent: LazyListScope.() -> Unit = {},
    prependErrorContent: LazyListScope.(Throwable) -> Unit = {},
    appendErrorContent: LazyListScope.(Throwable) -> Unit = {},
    itemContent: LazyListScope.() -> Unit,
) {
    val shouldShowRefreshIndicator =
        lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingColumnState !is LazyPagingColumnState.Loading

    if (shouldShowRefreshIndicator) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            refreshLoadingContent()
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
    ) {
        when (lazyPagingColumnState) {
            LazyPagingColumnState.Loading -> loadingContent()
            LazyPagingColumnState.Empty -> emptyContent()
            is LazyPagingColumnState.Error -> errorContent(lazyPagingColumnState.exception)
            LazyPagingColumnState.Content -> {
                when (val prepend = lazyPagingItems.loadState.prepend) {
                    is LoadState.Loading -> prependLoadingContent()
                    is LoadState.Error -> prependErrorContent(prepend.error)
                    is LoadState.NotLoading -> Unit
                }
                itemContent()
                when (val append = lazyPagingItems.loadState.append) {
                    is LoadState.Loading -> appendLoadingContent()
                    is LoadState.Error -> appendErrorContent(append.error)
                    is LoadState.NotLoading -> Unit
                }
            }
        }
    }
}
