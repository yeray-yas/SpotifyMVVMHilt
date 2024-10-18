// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // AGP
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // HILT
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinAndroidKsp) apply false

    // COMPOSE
    alias(libs.plugins.compose.compiler) apply false

    // FIREBASE
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.crashlytics) apply false
}