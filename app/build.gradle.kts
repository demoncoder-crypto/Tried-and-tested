import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

// Read API key from local.properties
// val localProperties = Properties()
// val localPropertiesFile = rootProject.file("local.properties")
// if (localPropertiesFile.exists()) {
//     localProperties.load(FileInputStream(localPropertiesFile))
// }

// Directly use the provided API key
// val hardcodedApiKey = "ge-b397f7e9b2df528b" // Previous invalid key
val hardcodedApiKey = "YOUR_NEW_VALID_API_KEY_HERE" // <-- REPLACE THIS WITH YOUR ACTUAL NEW KEY

android {
    namespace = "com.example.triedandtested"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.triedandtested"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Define BuildConfig field for API Key using the hardcoded value
        // buildConfigField("String", "PLACES_API_KEY", "\"${localProperties.getProperty("PLACES_API_KEY") ?: ""}\"")
        buildConfigField("String", "PLACES_API_KEY", "\"$hardcodedApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Also define for release builds using the hardcoded value
            // buildConfigField("String", "PLACES_API_KEY", "\"${localProperties.getProperty("PLACES_API_KEY") ?: ""}\"")
             buildConfigField("String", "PLACES_API_KEY", "\"$hardcodedApiKey\"")
        }
         debug { // Recommended to define for debug builds too using the hardcoded value
            // buildConfigField("String", "PLACES_API_KEY", "\"${localProperties.getProperty("PLACES_API_KEY") ?: ""}\"")
             buildConfigField("String", "PLACES_API_KEY", "\"$hardcodedApiKey\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true // Enable BuildConfig generation
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt Navigation Compose (optional but useful for Compose navigation)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization) // Retrofit Kotlinx Serialization Converter
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor) // Optional: for logging network calls

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Location Services
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt testing dependencies (if needed later)
    // testImplementation(libs.hilt.android.testing)
    // kaptTest(libs.hilt.compiler)
    // androidTestImplementation(libs.hilt.android.testing)
    // kaptAndroidTest(libs.hilt.compiler)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}