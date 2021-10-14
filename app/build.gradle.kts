plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        applicationId = "play.ground"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.4"
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.3.1")

    implementation("androidx.compose.ui:ui:1.0.4")
    implementation("androidx.compose.ui:ui-tooling:1.0.4")
    implementation("androidx.compose.foundation:foundation:1.0.4")
    implementation("androidx.compose.material:material:1.0.4")
    implementation("androidx.compose.material:material-icons-core:1.0.4")
    implementation("androidx.compose.material:material-icons-extended:1.0.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.0.4")

    implementation("com.google.accompanist:accompanist-insets:0.19.0")
    implementation("com.google.accompanist:accompanist-pager:0.19.0")
}