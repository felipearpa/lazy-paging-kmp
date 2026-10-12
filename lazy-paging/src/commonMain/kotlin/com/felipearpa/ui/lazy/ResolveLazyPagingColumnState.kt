package com.felipearpa.ui.lazy

import androidx.paging.LoadState

/**
 * The state before any load state has been resolved. Without items it is [LazyPagingColumnState.Loading]:
 * resolution keeps the current state while a refresh loads, so starting from
 * [LazyPagingColumnState.Content] would show an empty content area, and the refresh indicator,
 * until the first page arrives.
 */
internal fun initialLazyPagingColumnState(itemCount: Int): LazyPagingColumnState =
    if (itemCount > 0) LazyPagingColumnState.Content else LazyPagingColumnState.Loading

internal fun resolveLazyPagingColumnState(
    refresh: LoadState,
    prepend: LoadState,
    append: LoadState,
    itemCount: Int,
    current: LazyPagingColumnState,
): LazyPagingColumnState = when (refresh) {
    is LoadState.Error -> LazyPagingColumnState.Error(refresh.error)

    is LoadState.NotLoading ->
        if (prepend.endOfPaginationReached && append.endOfPaginationReached && itemCount == 0) {
            LazyPagingColumnState.Empty
        } else if (itemCount > 0) {
            LazyPagingColumnState.Content
        } else {
            LazyPagingColumnState.Loading
        }

    is LoadState.Loading ->
        if (current !is LazyPagingColumnState.Loading) current
        else LazyPagingColumnState.Loading
}
