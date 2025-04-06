package com.example.triedandtested.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LocationService {
    fun requestLocationUpdates(): Flow<Location?>
    fun hasLocationPermission(): Boolean
}

class LocationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationClient: FusedLocationProviderClient
) : LocationService {

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Flow<Location?> = callbackFlow {
        if (!hasLocationPermission()) {
            trySend(null) // Send null if no permission
            close(SecurityException("Missing location permission"))
            return@callbackFlow
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L) // Update every 10 seconds
            .setMinUpdateIntervalMillis(5000L) // Minimum interval 5 seconds
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Send the new location
                    launch { send(location) }
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e) // Close the flow with exception on failure
        }

        // Clean up when the flow is cancelled/closed
        awaitClose { 
            locationClient.removeLocationUpdates(locationCallback) 
        }
    }

    override fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
} 