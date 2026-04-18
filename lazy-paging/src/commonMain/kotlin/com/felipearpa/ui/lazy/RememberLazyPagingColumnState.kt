package com.felipearpa.ui.lazy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.paging.compose.LazyPagingItems

/**
 * Resolves the [LazyPagingColumnState] for a given [LazyPagingItems].
 * @param lazyPagingItems The [LazyPagingItems] to resolve the state for.
 * @return The [LazyPagingColumnState] for the given [LazyPagingItems].
 */
@Composable
fun <Item : Any> rememberLazyPagingColumnState(lazyPagingItems: LazyPagingItems<Item>): LazyPagingColumnState {
    val lazyPagingColumnState by produceState<LazyPagingColumnState>(
        initialValue = LazyPagingColumnState.Content,
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
