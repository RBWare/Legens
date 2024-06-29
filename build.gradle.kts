buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.hilt) apply false
    kotlin("kapt") version "1.9.22"
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}