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
import com.example.triedandtested.data.model.Restaurant // Use Restaurant model
import com.example.triedandtested.ui.viewmodel.RecommendationsViewModel
import com.example.triedandtested.ui.viewmodel.RecommendationsUiState

@Composable
fun RecommendationsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecommendationsViewModel = viewModel()
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
                // Reuse RestaurantList or create a specific one if needed
                RecommendedRestaurantList(restaurants = uiState.recommendedRestaurants)
            }
        }
    }
}

@Composable
fun RecommendedRestaurantList(restaurants: List<Restaurant>, modifier: Modifier = Modifier) {
    if (restaurants.isEmpty()) {
        Text("No recommendations for you yet.")
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { // Add a header
                Text(
                    text = "Especially for you:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(restaurants) { restaurant ->
                // Reuse RestaurantItem or create a specific recommendation item card
                RestaurantItem(restaurant = restaurant)
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun RecommendationsScreenPreview() {
    RecommendationsScreenContentPreview(uiState = RecommendationsUiState(isLoading = true))
}

@Preview(showBackground = true, name = "Recommendations Screen with Data")
@Composable
fun RecommendationsScreenWithDataPreview() {
    val dummyRecommendations = listOf(
        Restaurant("5", "Hidden Gem Cafe", "99 Secret Ln", 40.7111, -74.0011),
        Restaurant("2", "Pizza Paradise", "456 Main Ave", 40.7580, -73.9855)
    )
    RecommendationsScreenContentPreview(uiState = RecommendationsUiState(recommendedRestaurants = dummyRecommendations))
}

@Preview(showBackground = true, name = "Recommendations Screen Error")
@Composable
fun RecommendationsScreenErrorPreview() {
    RecommendationsScreenContentPreview(uiState = RecommendationsUiState(error = "Failed to load"))
}

@Composable
fun RecommendationsScreenContentPreview(uiState: RecommendationsUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null -> Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            else -> RecommendedRestaurantList(restaurants = uiState.recommendedRestaurants)
        }
    }
} 