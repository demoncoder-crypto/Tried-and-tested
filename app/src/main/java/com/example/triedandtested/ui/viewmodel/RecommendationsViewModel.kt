package com.example.triedandtested.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triedandtested.data.model.Restaurant // Reuse Restaurant model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class RecommendationsUiState(
    val recommendedRestaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
    // Could add reasons for recommendation, etc.
)

class RecommendationsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationsUiState())
    val uiState: StateFlow<RecommendationsUiState> = _uiState

    init {
        fetchRecommendations()
    }

    private fun fetchRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // TODO: Replace with actual recommendation logic (ML model call?)
                delay(1800) // Simulate processing/network delay
                // Dummy data for recommended restaurants
                val dummyRecommendations = listOf(
                    Restaurant("5", "Hidden Gem Cafe", "99 Secret Ln", 40.7111, -74.0011),
                    Restaurant("2", "Pizza Paradise", "456 Main Ave", 40.7580, -73.9855) // Maybe recommended based on past likes
                )
                _uiState.value = _uiState.value.copy(recommendedRestaurants = dummyRecommendations, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load recommendations: ${e.message}")
            }
        }
    }
} 