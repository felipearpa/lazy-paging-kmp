package com.felipearpa.ui.lazy.sample.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

enum class Scenario(
    val displayName: String,
    val summary: String,
) {
    Bidirectional(
        displayName = "Bidirectional",
        summary = "Starts at page 10 of 20. Scroll up to prepend, down to append.",
    ),
    ForwardOnly(
        displayName = "Forward only",
        summary = "Starts at page 0. Scroll down to append more pages.",
    ),
    Empty(
        displayName = "Empty result",
        summary = "Source returns zero items — shows the Empty state.",
    ),
    InitialError(
        displayName = "Initial load error",
        summary = "Refresh fails — shows the Error state.",
    ),
    AppendError(
        displayName = "Append error at page 2",
        summary = "Pages 0–1 load; page 2 errors. Tests inline append failure handling.",
    ),
    PrependError(
        displayName = "Prepend error at page 9",
        summary = "Starts at page 10; scrolling up triggers a prepend failure at page 9.",
    ),
    Slow(
        displayName = "Slow network",
        summary = "Long delays on refresh and page loads to inspect loading states.",
    ),
}

private const val PAGE_SIZE = 20
private const val TOTAL_ITEMS = 400
private const val MIDDLE_PAGE = 10

fun Scenario.pager(): Pager<Int, SampleItem> {
    val config = PagingConfig(
        pageSize = PAGE_SIZE,
        prefetchDistance = PAGE_SIZE / 2,
        enablePlaceholders = false,
        initialLoadSize = PAGE_SIZE,
    )

    return Pager(
        config = config,
        initialKey = initialKey(),
        pagingSourceFactory = {
            BidirectionalFakePagingSource(
                totalItems = if (this == Scenario.Empty) 0 else TOTAL_ITEMS,
                pageSize = PAGE_SIZE,
                loadDelayMs = if (this == Scenario.Slow) 2_000L else 600L,
                refreshDelayMs = if (this == Scenario.Slow) 3_000L else 900L,
                emptyResult = this == Scenario.Empty,
                failOnRefresh = this == Scenario.InitialError,
                failOnAppendPage = if (this == Scenario.AppendError) 2 else null,
                failOnPrependPage = if (this == Scenario.PrependError) 9 else null,
            )
        },
    )
}

fun Scenario.flow(): Flow<PagingData<SampleItem>> = pager().flow

private fun Scenario.initialKey(): Int = when (this) {
    Scenario.Bidirectional, Scenario.PrependError -> MIDDLE_PAGE
    else -> 0
}
