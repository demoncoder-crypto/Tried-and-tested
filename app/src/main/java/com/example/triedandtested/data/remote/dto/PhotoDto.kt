package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("photo_reference") val photoReference: String,
    @SerialName("height") val height: Int,
    @SerialName("width") val width: Int
    // Note: To get the actual image URL, you need another API call using the photoReference
) 