package com.example.triedandtested.data.repository

import com.example.triedandtested.data.model.Restaurant
import com.example.triedandtested.data.remote.PlacesApiService
import com.example.triedandtested.data.remote.dto.PlaceDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Hilt provides a single instance
class RestaurantRepositoryImpl @Inject constructor(
    private val placesApiService: PlacesApiService
    // Inject local data source here later (e.g., Room DAO)
) : RestaurantRepository {

    override fun getNearbyRestaurants(latitude: Double, longitude: Double): Flow<Result<List<Restaurant>>> = flow {
        try {
            val locationString = "$latitude,$longitude"
            val response = placesApiService.searchNearbyRestaurants(location = locationString)

            if (response.status == "OK") {
                val restaurants = response.results.map { it.toRestaurant() }
                emit(Result.Success(restaurants))
            } else {
                // Handle API errors (e.g., ZERO_RESULTS, OVER_QUERY_LIMIT)
                emit(Result.Error(Exception("Places API Error: ${response.status} - ${response.errorMessage ?: "Unknown error"}")))
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO) // Perform network call on IO thread
}

// Mapper function to convert DTO to domain model
private fun PlaceDto.toRestaurant(): Restaurant {
    return Restaurant(
        id = this.placeId,
        name = this.name,
        address = this.vicinity ?: "Address not available",
        latitude = this.geometry.location.lat,
        longitude = this.geometry.location.lng,
        rating = this.rating
        // Map other fields as needed (photo refs, etc.)
    )
} 