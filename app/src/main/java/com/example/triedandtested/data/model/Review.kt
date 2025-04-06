package com.example.triedandtested.data.model

data class Review(
    val id: String,
    val restaurantId: String,
    val userId: String, // Or reviewer name
    val rating: Float,
    val text: String,
    val timestamp: Long,
    // Add fields like photos, specific dishes mentioned etc.
) 