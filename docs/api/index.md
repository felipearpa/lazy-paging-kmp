---
title: API Reference
layout: default
nav_order: 3
has_children: true
---

# API Reference

The library exposes two composables and one state type.

| Symbol | Description |
|:-------|:------------|
| [`LazyPagingColumn`](lazy-paging-column) | `LazyColumn` that renders loading / empty / error / content slots based on paging state |
| [`RefreshableLazyPagingColumn`](refreshable-lazy-paging-column) | `LazyPagingColumn` wrapped in a Material 3 `PullToRefreshBox` |
| [`LazyPagingColumnState`](lazy-paging-column-state) | Sealed interface: `Loading`, `Empty`, `Error(exception)`, `Content` |

A top-level `rememberLazyPagingColumnState(lazyPagingItems)` composable derives the state from a `LazyPagingItems<T>`. Both columns call it internally by default, so you rarely need it directly — but it's public so you can observe the state or override it.
