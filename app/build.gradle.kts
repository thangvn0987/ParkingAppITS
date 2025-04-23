plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.android")
//    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")

//    id("com.google.dagger.hilt.android")      // ✅ Kích hoạt Hilt plugin
//    kotlin("kapt")
}

android {
    namespace = "com.example.parkingappits"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.parkingappits"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey: String = project.findProperty("MAPS_API_KEY") as String? ?: ""
        buildConfigField("String", "MAPS_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    buildFeatures{
        buildConfig = true
    }

}

dependencies {

    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.osmdroid:osmdroid-android:6.1.16")
    implementation("org.osmdroid:osmdroid-wms:6.1.16")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")




//chi duong
//    implementation("com.google.dagger:hilt-android:2.48")
//    kapt("com.google.dagger:hilt-compiler:2.48")
//
//    // ✅ Hilt with Jetpack Compose ViewModel
//    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
//    // Dành cho Navigation Compose + Hilt
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.google.maps.android:maps-compose:2.11.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.accompanist:accompanist-permissions:0.31.5-beta")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.5")



//  nav
    implementation("androidx.navigation:navigation-compose:2.5.3")

//  nguon khac
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.json:json:20210307")


//

    implementation("com.google.android.gms:play-services-location:21.0.1")
//    api gg map
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")
//
    implementation("com.google.accompanist:accompanist-swiperefresh:0.26.2-beta")
    implementation("com.google.android.gms:play-services-location:21.0.1")
//

    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
//
    implementation("com.google.firebase:firebase-auth-ktx:22.1.0")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
//
    implementation("com.google.android.gms:play-services-auth:20.7.0")
//
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material:material:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.activity:activity-compose:1.8.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Google Sign-In dependencies
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-identity:18.0.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Preferences DataStore (alternative to SharedPreferences)
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Accompanist for system UI controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.0-alpha")

    // Firebase for Google Sign-In
    implementation("com.google.firebase:firebase-auth:22.1.1")

    // Material Design
    implementation("androidx.compose.material3:material3:1.2.0-alpha05")
//

    implementation("androidx.compose.ui:ui:1.5.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.compose.ui:ui:1.5.2")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
apply(plugin = "com.google.gms.google-services")
//apply(plugin = "com.google.firebase.crashlytics")