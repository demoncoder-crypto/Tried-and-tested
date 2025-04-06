package com.example.triedandtested.data.model

data class FoodItem(
    val id: String,
    val name: String,
    val restaurantId: String,
    val description: String? = null,
    val popularityScore: Double? = null // Could be calculated based on mentions
) 