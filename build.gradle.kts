buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:2.10.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.kotlin.compose) apply false

    id("com.android.library") version "8.1.0" apply false
//    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false

//    id("com.google.dagger.hilt.android") version "2.48" apply false
}