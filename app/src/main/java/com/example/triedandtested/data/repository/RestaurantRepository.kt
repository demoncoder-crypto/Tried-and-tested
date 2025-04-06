package com.example.triedandtested.data.repository

import com.example.triedandtested.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

// Simple Result wrapper for handling success/error states
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

interface RestaurantRepository {
    // Function to get nearby restaurants, returning a Flow of Results
    // Using Flow allows emitting loading states, and potentially updates later
    fun getNearbyRestaurants(latitude: Double, longitude: Double): Flow<Result<List<Restaurant>>>

    // Add functions for other data operations (e.g., getRestaurantDetails, getTrendingItems)
} 