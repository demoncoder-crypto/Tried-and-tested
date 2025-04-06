package com.example.triedandtested.data.remote

import com.example.triedandtested.BuildConfig // Import BuildConfig for API Key
import com.example.triedandtested.data.remote.dto.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    // Example: Nearby Search request
    // https://developers.google.com/maps/documentation/places/web-service/search-nearby
    @GET("place/nearbysearch/json")
    suspend fun searchNearbyRestaurants(
        @Query("location") location: String, // Format: "latitude,longitude"
        @Query("radius") radius: Int = 10000, // Radius in meters (e.g., 10km)
        @Query("type") type: String = "restaurant",
        @Query("key") apiKey: String = BuildConfig.PLACES_API_KEY
    ): PlacesResponse

    // Add other API calls here as needed (e.g., Place Details, Text Search)
} 