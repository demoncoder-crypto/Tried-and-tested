package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeometryDto(
    @SerialName("location") val location: LocationDto
) 