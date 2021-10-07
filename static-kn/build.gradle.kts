import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

fun KotlinMultiplatformExtension.androidNative(
    config: KotlinNativeTarget.() -> Unit = {}
) {
    val androidNativeMain by sourceSets.creating {
        dependsOn(sourceSets.commonMain.get())
    }
    val wrapper: KotlinNativeTarget.() -> Unit = {
        compilations["main"].defaultSourceSet.dependsOn(androidNativeMain)
        config()
    }
    androidNativeX64(configure = wrapper)
    androidNativeX86(configure = wrapper)
    androidNativeArm32(configure = wrapper)
    androidNativeArm64(configure = wrapper)
}

kotlin {
    androidNative {
        binaries {
            staticLib(listOf(DEBUG)) {
                baseName = "playgroundKn"
            }
        }
    }
}

// failure:
// ./gradlew linkDebugStaticAndroidNativeArm32
// ./gradlew linkDebugStaticAndroidNativeArm64
// ./gradlew linkDebugStaticAndroidNativeX86
// ./gradlew linkDebugStaticAndroidNativeX64

