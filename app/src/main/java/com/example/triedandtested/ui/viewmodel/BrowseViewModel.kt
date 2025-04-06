package com.example.triedandtested.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triedandtested.data.model.Restaurant
import com.example.triedandtested.data.repository.RestaurantRepository
import com.example.triedandtested.data.repository.Result
import com.example.triedandtested.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasLocationPermission: Boolean = false,
    val userLocation: Location? = null
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState

    init {
        checkLocationPermission()
        if (locationService.hasLocationPermission()) {
            observeLocation()
        }
    }

    fun checkLocationPermission() {
        val hasPermission = locationService.hasLocationPermission()
        _uiState.value = _uiState.value.copy(hasLocationPermission = hasPermission)
        if (!hasPermission) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Location permission required.")
        }
    }

    fun onPermissionGranted() {
        _uiState.value = _uiState.value.copy(hasLocationPermission = true, error = null)
        observeLocation()
    }

    fun onPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = false,
            isLoading = false,
            error = "Location permission is required to find nearby restaurants."
        )
    }

    private fun observeLocation() {
        viewModelScope.launch {
            locationService.requestLocationUpdates()
                .distinctUntilChanged()
                .onStart { _uiState.value = _uiState.value.copy(isLoading = true) }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to get location: ${e.message}")
                }
                .collect { location ->
                    if (location != null) {
                        _uiState.value = _uiState.value.copy(userLocation = location)
                        fetchNearbyRestaurants(location.latitude, location.longitude)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = "Unable to retrieve location.")
                    }
                }
        }
    }

    private fun fetchNearbyRestaurants(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repository.getNearbyRestaurants(latitude, longitude)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load restaurants: ${e.message}")
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(isLoading = false, restaurants = result.data, error = null)
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(isLoading = false, error = "Error loading restaurants: ${result.exception.message}")
                        }
                    }
                }
        }
    }

    // Functions to handle user actions like searching, filtering etc. can be added here
} 