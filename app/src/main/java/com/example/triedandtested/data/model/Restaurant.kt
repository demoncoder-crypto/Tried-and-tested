package com.example.triedandtested.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double? = null // Add nullable rating field
    // Add other relevant fields like cuisine type, price range etc.
) 