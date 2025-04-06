package com.example.triedandtested.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triedandtested.data.model.FoodItem // Import the FoodItem data class
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay // Import delay

data class TrendingUiState(
    val trendingItems: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TrendingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState> = _uiState

    init {
        fetchTrendingItems()
    }

    private fun fetchTrendingItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // TODO: Replace with actual data fetching logic
                delay(1200) // Simulate network delay
                // Dummy data for trending food items
                val dummyTrendingItems = listOf(
                    FoodItem("f1", "Spicy Ramen", "1", popularityScore = 9.5),
                    FoodItem("f2", "Margherita Pizza", "2", popularityScore = 9.2),
                    FoodItem("f3", "Avocado Toast", "4", description = "Classic brunch item", popularityScore = 8.8)
                )
                _uiState.value = _uiState.value.copy(trendingItems = dummyTrendingItems, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load trending items: ${e.message}")
            }
        }
    }
} 