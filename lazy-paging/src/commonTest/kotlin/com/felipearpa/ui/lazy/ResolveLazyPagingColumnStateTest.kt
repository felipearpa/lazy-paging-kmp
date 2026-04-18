package com.felipearpa.ui.lazy

import androidx.paging.LoadState
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ResolveLazyPagingColumnStateTest {
    @Test
    fun `given refresh is Error when resolved then returns Error`() {
        val throwable = RuntimeException("boom")

        val result = resolveLazyPagingColumnState(
            refresh = LoadState.Error(throwable),
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 0,
            current = LazyPagingColumnState.Content,
        )

        result shouldBe LazyPagingColumnState.Error(throwable)
    }

    @Test
    fun `given no items and both ends reached when refresh is NotLoading then returns Empty`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.NotLoading(endOfPaginationReached = true),
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true),
            itemCount = 0,
            current = LazyPagingColumnState.Loading,
        )

        result shouldBe LazyPagingColumnState.Empty
    }

    @Test
    fun `given items present when refresh is NotLoading then returns Content`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.NotLoading(endOfPaginationReached = false),
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 5,
            current = LazyPagingColumnState.Loading,
        )

        result shouldBe LazyPagingColumnState.Content
    }

    @Test
    fun `given no items and prepend still paging when refresh is NotLoading then returns Loading`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.NotLoading(endOfPaginationReached = false),
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = true),
            itemCount = 0,
            current = LazyPagingColumnState.Loading,
        )

        result shouldBe LazyPagingColumnState.Loading
    }

    @Test
    fun `given no items and append still paging when refresh is NotLoading then returnsLoading`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.NotLoading(endOfPaginationReached = false),
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 0,
            current = LazyPagingColumnState.Loading,
        )

        result shouldBe LazyPagingColumnState.Loading
    }

    @Test
    fun `given current is Content when refresh is Loading then preservesContent`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.Loading,
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 10,
            current = LazyPagingColumnState.Content,
        )

        result shouldBe LazyPagingColumnState.Content
    }

    @Test
    fun `given current is Empty when refresh is Loading then preserves Empty`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.Loading,
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true),
            itemCount = 0,
            current = LazyPagingColumnState.Empty,
        )

        result shouldBe LazyPagingColumnState.Empty
    }

    @Test
    fun `given current is Error when refresh is Loading then preserves Error`() {
        val throwable = IllegalStateException("previous failure")

        val result = resolveLazyPagingColumnState(
            refresh = LoadState.Loading,
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 0,
            current = LazyPagingColumnState.Error(throwable),
        )

        result shouldBe LazyPagingColumnState.Error(throwable)
    }

    @Test
    fun `given current is Loading when refresh is Loading then stays Loading`() {
        val result = resolveLazyPagingColumnState(
            refresh = LoadState.Loading,
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 0,
            current = LazyPagingColumnState.Loading,
        )

        result shouldBe LazyPagingColumnState.Loading
    }
}
