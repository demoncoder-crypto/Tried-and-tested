package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    @SerialName("place_id") val placeId: String,
    @SerialName("name") val name: String,
    @SerialName("vicinity") val vicinity: String? = null, // Address
    @SerialName("geometry") val geometry: GeometryDto,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("user_ratings_total") val userRatingsTotal: Int? = null,
    @SerialName("photos") val photos: List<PhotoDto>? = null,
    @SerialName("opening_hours") val openingHours: OpeningHoursDto? = null,
    @SerialName("types") val types: List<String>? = null // e.g., ["restaurant", "food", "point_of_interest"]
) 