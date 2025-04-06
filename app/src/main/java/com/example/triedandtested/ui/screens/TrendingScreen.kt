package com.example.triedandtested.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triedandtested.data.model.FoodItem
import com.example.triedandtested.ui.viewmodel.TrendingViewModel
import com.example.triedandtested.ui.viewmodel.TrendingUiState

@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                TrendingList(trendingItems = uiState.trendingItems)
            }
        }
    }
}

@Composable
fun TrendingList(trendingItems: List<FoodItem>, modifier: Modifier = Modifier) {
    if (trendingItems.isEmpty()) {
        Text("Nothing trending right now.")
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trendingItems) { item ->
                TrendingItem(item = item)
            }
        }
    }
}

@Composable
fun TrendingItem(item: FoodItem, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            // Could add restaurant info here if needed
            item.description?.let {
                 Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            item.popularityScore?.let {
                 Text(text = "Score: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Previews similar to BrowseScreen
@Preview(showBackground = true)
@Composable
fun TrendingScreenPreview() {
    TrendingScreenContentPreview(uiState = TrendingUiState(isLoading = true))
}

@Preview(showBackground = true, name = "Trending Screen with Data")
@Composable
fun TrendingScreenWithDataPreview() {
     val dummyTrendingItems = listOf(
        FoodItem("f1", "Spicy Ramen", "1", popularityScore = 9.5),
        FoodItem("f2", "Margherita Pizza", "2", popularityScore = 9.2)
    )
    TrendingScreenContentPreview(uiState = TrendingUiState(trendingItems = dummyTrendingItems))
}

@Preview(showBackground = true, name = "Trending Screen Error")
@Composable
fun TrendingScreenErrorPreview() {
    TrendingScreenContentPreview(uiState = TrendingUiState(error = "Failed to load"))
}

@Composable
fun TrendingScreenContentPreview(uiState: TrendingUiState, modifier: Modifier = Modifier) {
     Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null -> Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            else -> TrendingList(trendingItems = uiState.trendingItems)
        }
    }
} 