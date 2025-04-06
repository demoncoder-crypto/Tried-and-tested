package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("lat") val lat: Double,
    @SerialName("lng") val lng: Double
) 