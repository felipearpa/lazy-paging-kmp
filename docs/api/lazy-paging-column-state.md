---
title: LazyPagingColumnState
layout: default
parent: API Reference
nav_order: 3
---

# LazyPagingColumnState

Sealed interface representing the four screen-level states a paged column can be in.

```kotlin
sealed interface LazyPagingColumnState {
    data object Loading : LazyPagingColumnState
    data object Empty : LazyPagingColumnState
    data class Error(val exception: Throwable) : LazyPagingColumnState
    data object Content : LazyPagingColumnState
}
```

| State | Meaning |
|:------|:--------|
| `Loading` | Initial refresh in progress and no items yet. |
| `Content` | At least one item is available — render the list. |
| `Empty` | Refresh finished, prepend and append both reached end of pagination, and `itemCount == 0`. |
| `Error(exception)` | Refresh produced `LoadState.Error`. Carries the thrown `Throwable`. |

## rememberLazyPagingColumnState

```kotlin
@Composable
fun <Item : Any> rememberLazyPagingColumnState(
    lazyPagingItems: LazyPagingItems<Item>,
): LazyPagingColumnState
```

Returns a `LazyPagingColumnState` derived from the refresh / prepend / append load states and the item count. Both [`LazyPagingColumn`](lazy-paging-column) and [`RefreshableLazyPagingColumn`](refreshable-lazy-paging-column) use it by default; call it yourself when you want to observe the state outside the column (for example to drive a title or an analytics event).

## Resolution Rules

Given `refresh`, `prepend`, and `append` from `lazyPagingItems.loadState`, plus `itemCount`:

| Condition | Resulting state |
|:----------|:----------------|
| `refresh is LoadState.Error` | `Error(refresh.error)` |
| `refresh` not loading, `prepend` & `append` ended, `itemCount == 0` | `Empty` |
| `refresh` not loading, `itemCount > 0` | `Content` |
| `refresh` not loading, `itemCount == 0` | `Loading` |
| `refresh is LoadState.Loading` | keep the current state (falls back to `Loading` if there isn't one) |

The last rule is what keeps the list on screen during a background refresh instead of flashing back to a spinner — the transient `Content` → `Loading` → `Content` cycle is collapsed into a single `Content`, and the refresh indicator is shown via `refreshLoadingContent` instead.
