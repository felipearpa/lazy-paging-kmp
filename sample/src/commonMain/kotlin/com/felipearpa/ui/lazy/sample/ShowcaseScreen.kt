package com.felipearpa.ui.lazy.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.felipearpa.ui.lazy.RefreshableLazyPagingColumn
import com.felipearpa.ui.lazy.sample.paging.SampleItem
import com.felipearpa.ui.lazy.sample.paging.Scenario
import com.felipearpa.ui.lazy.sample.paging.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseScreen() {
    var scenario by remember { mutableStateOf(Scenario.Bidirectional) }
    val flow = remember(scenario) { scenario.flow() }
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "stateful-lazy showcase") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            ScenarioPicker(
                selected = scenario,
                onScenarioSelected = { scenario = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )

            Text(
                text = scenario.summary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.size(12.dp))

            RefreshableLazyPagingColumn(
                modifier = Modifier.fillMaxSize(),
                lazyPagingItems = lazyPagingItems,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                loadingContent = { loadingItem() },
                errorContent = { exception -> errorItem(exception) { lazyPagingItems.retry() } },
                emptyContent = { emptyItem() },
            ) {
                prependLoadItem(lazyPagingItems)

                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id },
                ) { index ->
                    val item = lazyPagingItems[index]
                    if (item != null) ItemRow(item)
                }

                appendLoadItem(lazyPagingItems)
            }
        }
    }
}

@Composable
private fun ScenarioPicker(
    selected: Scenario,
    onScenarioSelected: (Scenario) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Scenario: ${selected.displayName}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Scenario.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onScenarioSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ItemRow(item: SampleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(18.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.id.toString(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.size(16.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

private fun LazyListScope.loadingItem() {
    item {
        Box(
            modifier = Modifier
                .fillParentMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

private fun LazyListScope.emptyItem() {
    item {
        Box(
            modifier = Modifier
                .fillParentMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Nothing to show yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun LazyListScope.errorItem(exception: Throwable, onRetry: () -> Unit) {
    item {
        Column(
            modifier = Modifier
                .fillParentMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = exception.message ?: "Unknown error",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.size(16.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

private fun LazyListScope.prependLoadItem(lazyPagingItems: LazyPagingItems<SampleItem>) {
    when (val prepend = lazyPagingItems.loadState.prepend) {
        is LoadState.Loading -> item { LoadingFooter(text = "Loading previous…") }
        is LoadState.Error -> item {
            RetryFooter(
                message = prepend.error.message ?: "Failed to load previous page",
                onRetry = { lazyPagingItems.retry() },
            )
        }

        is LoadState.NotLoading -> Unit
    }
}

private fun LazyListScope.appendLoadItem(lazyPagingItems: LazyPagingItems<SampleItem>) {
    when (val append = lazyPagingItems.loadState.append) {
        is LoadState.Loading -> item { LoadingFooter(text = "Loading more…") }
        is LoadState.Error -> item {
            RetryFooter(
                message = append.error.message ?: "Failed to load next page",
                onRetry = { lazyPagingItems.retry() },
            )
        }

        is LoadState.NotLoading -> Unit
    }
}

@Composable
private fun LoadingFooter(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
        Spacer(Modifier.size(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun RetryFooter(message: String, onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red,
        )
        Spacer(Modifier.size(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
