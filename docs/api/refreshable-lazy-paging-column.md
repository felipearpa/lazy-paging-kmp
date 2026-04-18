---
title: RefreshableLazyPagingColumn
layout: default
parent: API Reference
nav_order: 2
---

# RefreshableLazyPagingColumn

`LazyPagingColumn` wrapped in a Material 3 `PullToRefreshBox`. Pulling down calls `lazyPagingItems.refresh()`.

## Signature

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Item : Any> RefreshableLazyPagingColumn(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<Item>,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingColumnState: LazyPagingColumnState = rememberLazyPagingColumnState(lazyPagingItems),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    loadingContent: LazyListScope.() -> Unit = {},
    refreshLoadingContent: @Composable () -> Unit = { CircularProgressIndicator(Modifier.padding(16.dp)) },
    errorContent: LazyListScope.(Throwable) -> Unit = {},
    emptyContent: LazyListScope.() -> Unit = {},
    itemContent: LazyListScope.() -> Unit,
)
```

## Differences from `LazyPagingColumn`

- Wraps the column in `PullToRefreshBox`. The user-driven pull gesture is handled for you — `refresh()` is invoked when the pull completes.
- `refreshLoadingContent` has a default (a padded `CircularProgressIndicator`) instead of being empty. It's shown above the list while a refresh is in progress and data is already visible, but **only** when the user did not initiate the refresh via pull (the pull gesture shows its own indicator via `PullToRefreshBox`).
- Opts in to `ExperimentalMaterial3Api` because `PullToRefreshBox` is experimental.

Every other parameter behaves exactly like its counterpart in [`LazyPagingColumn`](lazy-paging-column).

## Example

```kotlin
RefreshableLazyPagingColumn(
    modifier = Modifier.fillMaxSize(),
    lazyPagingItems = articles,
    contentPadding = PaddingValues(16.dp),
    loadingContent = { item { CircularProgressIndicator() } },
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
    items(articles.itemCount, key = articles.itemKey { it.id }) { index ->
        articles[index]?.let { ArticleRow(it) }
    }
}
```
