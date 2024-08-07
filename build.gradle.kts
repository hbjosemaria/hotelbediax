plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}