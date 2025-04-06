package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpeningHoursDto(
    @SerialName("open_now") val openNow: Boolean? = null
    // Can add periods for detailed hours if needed
) 