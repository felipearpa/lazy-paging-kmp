package com.felipearpa.ui.lazy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.paging.compose.LazyPagingItems

/**
 * Resolves the [LazyPagingColumnState] for a given [LazyPagingItems].
 *
 * The state starts as [LazyPagingColumnState.Loading] while there are no items, so a column shown
 * before its first page renders its loading content from the first frame instead of an empty
 * content area (see [initialLazyPagingColumnState]).
 * @param lazyPagingItems The [LazyPagingItems] to resolve the state for.
 * @return The [LazyPagingColumnState] for the given [LazyPagingItems].
 */
@Composable
fun <Item : Any> rememberLazyPagingColumnState(lazyPagingItems: LazyPagingItems<Item>): LazyPagingColumnState {
    val lazyPagingColumnState by produceState<LazyPagingColumnState>(
        initialValue = initialLazyPagingColumnState(itemCount = lazyPagingItems.itemCount),
        lazyPagingItems.loadState.refresh,
    ) {
        value = resolveLazyPagingColumnState(
            refresh = lazyPagingItems.loadState.refresh,
            prepend = lazyPagingItems.loadState.prepend,
            append = lazyPagingItems.loadState.append,
            itemCount = lazyPagingItems.itemCount,
            current = value,
        )
    }

    return lazyPagingColumnState
}
