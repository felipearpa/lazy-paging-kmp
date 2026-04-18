# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.0.1] - 2026-04-18
### Initial Release
- **Introduced** `LazyPagingColumn`, a Compose Multiplatform `LazyColumn` that consumes `LazyPagingItems<T>` and renders dedicated slots for loading, empty, error, and content states.
- **Introduced** `RefreshableLazyPagingColumn`, wrapping `LazyPagingColumn` in a Material 3 `PullToRefreshBox` with a configurable refresh indicator.
- **Introduced** `LazyPagingColumnState` (sealed interface) with `Loading`, `Empty`, `Error(exception)`, and `Content` cases.
- **Introduced** `rememberLazyPagingColumnState`, which derives `LazyPagingColumnState` from the refresh / prepend / append load states and the current item count.
- **Provided** Kotlin Multiplatform support targeting Android, iOS arm64, and iOS simulator arm64.
