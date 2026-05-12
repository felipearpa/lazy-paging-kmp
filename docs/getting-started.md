---
title: Getting Started
layout: default
nav_order: 2
---

# Getting Started
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Installation

Make sure Maven Central is declared in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

Add the dependency. In a Compose Multiplatform module you typically put it in `commonMain`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.felipearpa:lazy-paging:0.0.2")
            // Paging — bring your own version
            implementation("androidx.paging:paging-common:3.4.2")
            implementation("androidx.paging:paging-compose:3.4.2")
        }
    }
}
```

## Basic Usage

Feed any `LazyPagingItems<T>` into `LazyPagingColumn` and provide the slots you care about. `itemContent` is a `LazyListScope` lambda — the same scope you'd use inside a plain `LazyColumn`.

```kotlin
@Composable
fun ArticleList(pager: Pager<Int, Article>) {
    val articles = pager.flow.collectAsLazyPagingItems()

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
        emptyContent = {
            item { Text("No articles yet.") }
        },
        errorContent = { exception ->
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
}
```

## Adding Pull-to-Refresh

Swap `LazyPagingColumn` for `RefreshableLazyPagingColumn`. It shares the same API and adds a Material 3 `PullToRefreshBox` around the column. `refresh()` is called when the user pulls; the default spinner can be replaced via `refreshLoadingContent`.

```kotlin
RefreshableLazyPagingColumn(
    modifier = Modifier.fillMaxSize(),
    lazyPagingItems = articles,
    refreshLoadingContent = { CircularProgressIndicator(Modifier.padding(16.dp)) },
    loadingContent = { /* initial load */ },
    emptyContent = { /* empty */ },
    errorContent = { /* refresh error */ },
) {
    items(articles.itemCount, key = articles.itemKey { it.id }) { index ->
        articles[index]?.let { ArticleRow(it) }
    }
}
```

## Handling Append / Prepend

`loadingContent`, `emptyContent`, and `errorContent` describe **the whole list's** state — i.e. what the screen should look like when there's nothing useful to show. For inline append/prepend loading or errors (loading next page, failed to load previous page), drive them from `lazyPagingItems.loadState` inside your `itemContent`:

```kotlin
LazyPagingColumn(
    lazyPagingItems = articles,
    /* ... */
) {
    when (val prepend = articles.loadState.prepend) {
        is LoadState.Loading -> item { InlineLoader() }
        is LoadState.Error -> item { InlineRetry(prepend.error) { articles.retry() } }
        else -> Unit
    }

    items(articles.itemCount, key = articles.itemKey { it.id }) { index ->
        articles[index]?.let { ArticleRow(it) }
    }

    when (val append = articles.loadState.append) {
        is LoadState.Loading -> item { InlineLoader() }
        is LoadState.Error -> item { InlineRetry(append.error) { articles.retry() } }
        else -> Unit
    }
}
```

## When Each State Shows

`LazyPagingColumnState` is computed by `rememberLazyPagingColumnState` from the `refresh`, `prepend`, `append` load states and the item count. In plain terms:

| State | When |
|:------|:-----|
| `Loading` | Refresh is in progress and there are no items yet |
| `Content` | At least one item is available |
| `Empty` | Refresh finished, prepend and append both ended, and no items |
| `Error` | Refresh produced a `LoadState.Error` |

See [`LazyPagingColumnState`](/lazy-paging-kmp/api/lazy-paging-column-state) for full details.
