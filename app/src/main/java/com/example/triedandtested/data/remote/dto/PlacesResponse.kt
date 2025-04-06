package com.example.triedandtested.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponse(
    @SerialName("results") val results: List<PlaceDto>,
    @SerialName("status") val status: String,
    @SerialName("error_message") val errorMessage: String? = null,
    @SerialName("next_page_token") val nextPageToken: String? = null
) 