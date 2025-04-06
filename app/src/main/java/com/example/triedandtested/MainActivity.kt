package com.example.triedandtested

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.triedandtested.ui.theme.TriedAndTestedTheme
import com.example.triedandtested.ui.screens.BrowseScreen
import com.example.triedandtested.ui.screens.RecommendationsScreen
import com.example.triedandtested.ui.screens.TrendingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriedAndTestedTheme {
                AppContent()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Browse : Screen("browse", "Browse", Icons.Default.Home)
    object Trending : Screen("trending", "Trending", Icons.Default.Star)
    object Recommendations : Screen("recommendations", "For You", Icons.Default.Info) // Example icon
}

val items = listOf(
    Screen.Browse,
    Screen.Trending,
    Screen.Recommendations
)

@Composable
fun AppContent() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Apply the padding to the container of the screen content
        val screenModifier = Modifier.padding(innerPadding).fillMaxSize()
        Box(modifier = Modifier.fillMaxSize()) { // Keep the outer Box to apply padding correctly
            when (selectedItem) {
                0 -> BrowseScreen(modifier = screenModifier)
                1 -> TrendingScreen(modifier = screenModifier)
                2 -> RecommendationsScreen(modifier = screenModifier)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TriedAndTestedTheme {
        AppContent()
    }
}