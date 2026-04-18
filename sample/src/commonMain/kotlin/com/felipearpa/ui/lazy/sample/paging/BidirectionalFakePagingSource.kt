package com.felipearpa.ui.lazy.sample.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

data class SampleItem(
    val id: Int,
    val label: String,
)

class BidirectionalFakePagingSource(
    private val totalItems: Int,
    private val pageSize: Int,
    private val loadDelayMs: Long,
    private val refreshDelayMs: Long,
    private val emptyResult: Boolean,
    private val failOnRefresh: Boolean,
    private val failOnAppendPage: Int?,
    private val failOnPrependPage: Int?,
) : PagingSource<Int, SampleItem>() {

    override fun getRefreshKey(state: PagingState<Int, SampleItem>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor) ?: return null
        return closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SampleItem> {
        val pageIndex = params.key ?: 0

        when (params) {
            is LoadParams.Refresh -> delay(refreshDelayMs)
            is LoadParams.Append, is LoadParams.Prepend -> delay(loadDelayMs)
        }

        if (params is LoadParams.Refresh && failOnRefresh) {
            return LoadResult.Error(RuntimeException("Refresh failed (simulated)"))
        }
        if (params is LoadParams.Append && failOnAppendPage == pageIndex) {
            return LoadResult.Error(RuntimeException("Append failed on page $pageIndex (simulated)"))
        }
        if (params is LoadParams.Prepend && failOnPrependPage == pageIndex) {
            return LoadResult.Error(RuntimeException("Prepend failed on page $pageIndex (simulated)"))
        }

        if (emptyResult) {
            return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }

        val totalPages = (totalItems + pageSize - 1) / pageSize
        if (pageIndex !in 0..<totalPages) {
            return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }

        val startIndex = pageIndex * pageSize
        val endIndex = minOf(startIndex + pageSize, totalItems)
        val items = (startIndex until endIndex).map { index ->
            SampleItem(id = index, label = "Item #$index")
        }

        return LoadResult.Page(
            data = items,
            prevKey = if (pageIndex > 0) pageIndex - 1 else null,
            nextKey = if (pageIndex + 1 < totalPages) pageIndex + 1 else null,
        )
    }
}
