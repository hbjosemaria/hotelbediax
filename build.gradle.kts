// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    val hiltVersion = "2.51.1"
    id("com.google.dagger.hilt.android") version hiltVersion apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}