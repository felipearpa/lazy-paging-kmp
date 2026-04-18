---
title: LazyPagingColumn
layout: default
parent: API Reference
nav_order: 1
---

# LazyPagingColumn

A `LazyColumn` that switches between dedicated slots based on the current [`LazyPagingColumnState`](lazy-paging-column-state).

## Signature

```kotlin
@Composable
fun <Item : Any> LazyPagingColumn(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<Item>,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingColumnState: LazyPagingColumnState = rememberLazyPagingColumnState(lazyPagingItems),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    loadingContent: LazyListScope.() -> Unit = {},
    refreshLoadingContent: @Composable () -> Unit = {},
    errorContent: LazyListScope.(Throwable) -> Unit = {},
    emptyContent: LazyListScope.() -> Unit = {},
    itemContent: LazyListScope.() -> Unit,
)
```

## Parameters

| Parameter | Description |
|:----------|:------------|
| `lazyPagingItems` | The `LazyPagingItems<Item>` driving the column. |
| `lazyListState` | Scroll state for the underlying `LazyColumn`. |
| `lazyPagingColumnState` | Override for the derived state. Defaults to `rememberLazyPagingColumnState(lazyPagingItems)`. |
| `contentPadding`, `reverseLayout`, `verticalArrangement` | Forwarded to `LazyColumn`. |
| `loadingContent` | Rendered when the derived state is `Loading` (initial load, no items yet). |
| `refreshLoadingContent` | Composable shown **above** the list when `refresh` is `LoadState.Loading` while the column is already in `Content` or `Empty`. Empty by default. |
| `errorContent` | Rendered when the derived state is `Error`. Receives the refresh `Throwable`. |
| `emptyContent` | Rendered when the derived state is `Empty`. |
| `itemContent` | Rendered when the derived state is `Content`. Plain `LazyListScope` — use `items(...)`, `item { ... }`, etc. |

## Behavior

- Only one of `loadingContent`, `emptyContent`, `errorContent`, or `itemContent` is active at a time — whichever matches the current `LazyPagingColumnState`.
- `refreshLoadingContent` is orthogonal: it renders alongside `itemContent` (or `emptyContent`) when a refresh kicks off while data is already on screen. Perfect for a thin linear indicator at the top of the list.
- Inline append / prepend state is *not* handled by these slots — drive that from `lazyPagingItems.loadState.append` / `.prepend` inside `itemContent`. See [Getting Started](/lazy-paging-kmp/getting-started#handling-append--prepend).

## Example

```kotlin
LazyPagingColumn(
    modifier = Modifier.fillMaxSize(),
    lazyPagingItems = articles,
    contentPadding = PaddingValues(16.dp),
    loadingContent = {
        item {
            Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    },
    emptyContent = { item { Text("No articles yet.") } },
    errorContent = { exception ->
        item {
            Column {
                Text(exception.message ?: "Something went wrong")
                Button(onClick = { articles.retry() }) { Text("Retry") }
            }
        }
    },
) {
    items(
        count = articles.itemCount,
        key = articles.itemKey { it.id },
    ) { index ->
        articles[index]?.let { ArticleRow(it) }
    }
}
```
