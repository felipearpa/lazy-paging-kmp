package com.felipearpa.ui.lazy

/**
 * Represents the various states of a paginated lazy column.
 *
 * This sealed interface defines the possible UI states during the lifecycle of
 * data fetching and presentation in a paginated list.
 */
sealed interface LazyPagingColumnState {
    /**
     * Represents the loading state of the lazy column.
     */
    data object Loading : LazyPagingColumnState

    /**
     * Represents the empty state of the lazy column.
     */
    data object Empty : LazyPagingColumnState

    /**
     * Represents the error state of the lazy column.
     *
     * @param exception The exception that caused the error.
     */
    data class Error(val exception: Throwable) : LazyPagingColumnState

    /**
     * Represents the content state of the lazy column.
     */
    data object Content : LazyPagingColumnState
}
