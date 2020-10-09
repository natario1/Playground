import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    setCompileSdkVersion(29)
    defaultConfig {
        setMinSdkVersion(21)
        setTargetSdkVersion(29)
    }
}

dependencies {
    api(project(":library"))
}