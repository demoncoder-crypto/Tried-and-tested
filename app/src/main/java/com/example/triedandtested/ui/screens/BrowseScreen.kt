package com.example.triedandtested.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Needed for permission check potentially
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triedandtested.data.model.Restaurant
import com.example.triedandtested.ui.viewmodel.BrowseViewModel
import com.example.triedandtested.ui.viewmodel.BrowseUiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            if (fineLocationGranted || coarseLocationGranted) {
                viewModel.onPermissionGranted()
            } else {
                viewModel.onPermissionDenied()
            }
        }
    )

    LaunchedEffect(Unit) { // Relaunch if needed, e.g., after coming back to the screen
         if (!uiState.hasLocationPermission) {
            // Check permission status again in case it was granted in settings
             viewModel.checkLocationPermission()
         }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            !uiState.hasLocationPermission -> {
                 PermissionRequestScreen(
                     errorMessage = uiState.error,
                     onRequestPermission = {
                         permissionLauncher.launch(
                             arrayOf(
                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                 Manifest.permission.ACCESS_COARSE_LOCATION
                             )
                         )
                     }
                 )
            }
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null && uiState.restaurants.isEmpty() -> {
                // Show error only if there are no restaurants to display
                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                    // Optional: Add a retry button here that calls viewModel.observeLocation() or fetch
                }
            }
            else -> {
                // Show restaurant list (potentially with a stale error message if loading failed previously)
                 if (uiState.error != null) {
                    // Optionally show a non-blocking error message (e.g., in a Snackbar)
                    Text("Could not refresh data: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
                RestaurantList(restaurants = uiState.restaurants)
            }
        }
    }
}

@Composable
fun PermissionRequestScreen(errorMessage: String?, onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage ?: "Location permission is needed to find nearby restaurants.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = if (errorMessage != null) MaterialTheme.colorScheme.error else LocalContentColor.current
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>, modifier: Modifier = Modifier) {
    if (restaurants.isEmpty()) {
        Text("No restaurants found nearby.") // Updated message
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(restaurants) { restaurant ->
                RestaurantItem(restaurant = restaurant)
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f) // Allow name to take available space
                )
                // Display rating if available
                restaurant.rating?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107), // Amber color for star
                        modifier = Modifier.size(16.dp) // Adjust size
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(it), // Format rating to one decimal place
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.address, style = MaterialTheme.typography.bodySmall)
            // Add Lat/Lng for debugging temporarily
            // Text(text = "(${restaurant.latitude}, ${restaurant.longitude})", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrowseScreenPreview_Loading() {
     BrowseScreenContentPreview(uiState = BrowseUiState(isLoading = true, hasLocationPermission = true))
}

@Preview(showBackground = true, name = "Browse Screen with Data")
@Composable
fun BrowseScreenPreview_Data() {
    val dummyRestaurants = listOf(
        Restaurant("1", "The Gourmet Place", "123 Food St", 40.7128, -74.0060),
        Restaurant("2", "Pizza Paradise", "456 Main Ave", 40.7580, -73.9855)
    )
     BrowseScreenContentPreview(uiState = BrowseUiState(restaurants = dummyRestaurants, hasLocationPermission = true))
}

@Preview(showBackground = true, name = "Browse Screen Error")
@Composable
fun BrowseScreenPreview_Error() {
     BrowseScreenContentPreview(uiState = BrowseUiState(error = "Failed to load", hasLocationPermission = true))
}

@Preview(showBackground = true, name = "Browse Screen Needs Permission")
@Composable
fun BrowseScreenPreview_NeedsPermission() {
     BrowseScreenContentPreview(uiState = BrowseUiState(hasLocationPermission = false))
}

@Preview(showBackground = true, name = "Browse Screen Permission Denied")
@Composable
fun BrowseScreenPreview_PermissionDenied() {
     BrowseScreenContentPreview(uiState = BrowseUiState(hasLocationPermission = false, error = "Permission Required"))
}

@Composable
fun BrowseScreenContentPreview(uiState: BrowseUiState, modifier: Modifier = Modifier) {
     Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
         when {
             !uiState.hasLocationPermission -> {
                 PermissionRequestScreen(
                     errorMessage = uiState.error,
                     onRequestPermission = { /* No-op for preview */ }
                 )
             }
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null && uiState.restaurants.isEmpty() -> {
                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                     Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                 }
             }
            else -> RestaurantList(restaurants = uiState.restaurants)
        }
    }
} 