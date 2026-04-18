---
title: Home
layout: home
nav_order: 1
---

# lazy-paging-kmp

A Kotlin Multiplatform Compose library for stateful, paged lazy columns backed by AndroidX Paging.
{: .fs-6 .fw-300 }

[Get Started](/lazy-paging-kmp/getting-started){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }
[View on GitHub](https://github.com/felipearpa/lazy-paging-kmp){: .btn .fs-5 .mb-4 .mb-md-0 }

---

## Overview

`lazy-paging` wraps `androidx.paging.compose.LazyPagingItems` in Compose Multiplatform lazy columns that already know how to show loading, empty, error, and content states — plus optional pull-to-refresh.

| Component | Description |
|:----------|:------------|
| [`LazyPagingColumn`](/lazy-paging-kmp/api/lazy-paging-column) | `LazyColumn` with loading / empty / error / content slots driven by paging state |
| [`RefreshableLazyPagingColumn`](/lazy-paging-kmp/api/refreshable-lazy-paging-column) | `LazyPagingColumn` inside a Material 3 `PullToRefreshBox` |
| [`LazyPagingColumnState`](/lazy-paging-kmp/api/lazy-paging-column-state) | Sealed state: `Loading`, `Empty`, `Error`, `Content` |

## Why

`LazyPagingItems` exposes three orthogonal load states (`refresh`, `prepend`, `append`) plus an item count. Turning that into an idiomatic Compose screen — one UI per *meaningful* state — usually means re-deriving the same booleans in every screen. `lazy-paging` does that derivation once and gives you a slot-based API: write the screen for each state, not the arithmetic.

## Platforms

- Android (JVM 17, minSdk 28, compileSdk 35)
- iOS Arm64
- iOS Simulator Arm64

## Quick Example

```kotlin
@Composable
fun ItemsScreen(pager: Pager<Int, Item>) {
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    RefreshableLazyPagingColumn(
        modifier = Modifier.fillMaxSize(),
        lazyPagingItems = lazyPagingItems,
        contentPadding = PaddingValues(16.dp),
        loadingContent = { item { CircularProgressIndicator() } },
        errorContent = { exception ->
            item {
                Column {
                    Text(exception.message ?: "Unknown error")
                    Button(onClick = { lazyPagingItems.retry() }) { Text("Retry") }
                }
            }
        },
        emptyContent = { item { Text("Nothing here yet.") } },
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id },
        ) { index ->
            lazyPagingItems[index]?.let { ItemRow(it) }
        }
    }
}
```
