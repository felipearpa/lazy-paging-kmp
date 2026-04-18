package com.felipearpa.ui.lazy

import androidx.paging.LoadState

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
