# lazy-paging-kmp

A Kotlin Multiplatform library that wraps AndroidX Paging's `LazyPagingItems` in Compose Multiplatform lazy columns with built-in loading, empty, error, and pull-to-refresh states.

## Overview

`lazy-paging` provides two composables that consume a `LazyPagingItems<T>` and render the right UI for its current paging state:

- **`LazyPagingColumn`** — a `LazyColumn` that swaps between loading / empty / error / content slots.
- **`RefreshableLazyPagingColumn`** — the same, wrapped in a Material 3 `PullToRefreshBox`.

State resolution is exposed via the sealed `LazyPagingColumnState` and computed by `rememberLazyPagingColumnState`, so you only write UI for the state, not the paging-flag arithmetic.

## Platforms

- Android (JVM 17, minSdk 28, compileSdk 35)
- iOS Arm64
- iOS Simulator Arm64

## Installation

Add Maven Central in `settings.gradle.kts` if it isn't there:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

Then add the dependency (Compose Multiplatform module, typically in `commonMain`):

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.felipearpa:lazy-paging:0.0.1")
        }
    }
}
```

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

## Sample

A runnable sample lives in [`sample/`](sample) (shared KMP code) and [`sample-android/`](sample-android) (Android launcher). It exercises bidirectional paging, append/prepend errors, empty results, slow network, and initial-load failures.

## Documentation

Full documentation is available at the [project site](https://felipearpa.github.io/lazy-paging-kmp/).

## Contributing

Pull requests and issues are welcome.

## License

This project is licensed under the [MIT License](LICENSE).
