import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
}

android {
    setCompileSdkVersion(29)
    defaultConfig {
        setMinSdkVersion(21)
        setTargetSdkVersion(29)
    }
    sourceSets["main"].java.srcDirs("src/androidJvmMain/kotlin")
    sourceSets["main"].manifest.srcFile("src/androidJvmMain/AndroidManifest.xml")
}

fun KotlinMultiplatformExtension.newSourceSet(name: String, parent: KotlinSourceSet): KotlinSourceSet {
    return sourceSets.maybeCreate(name).apply {
        dependsOn(parent)
    }
}

fun KotlinMultiplatformExtension.androidNative(name: String = "androidNative", configure: KotlinNativeTarget.() -> Unit) {
    val androidNativeMain = newSourceSet("${name}Main", sourceSets["commonMain"])
    val androidNativeTest = newSourceSet("${name}Test", sourceSets["commonTest"])
    val androidNative32BitMain = newSourceSet("${name}32BitMain", androidNativeMain)
    val androidNative32BitTest = newSourceSet("${name}32BitTest", androidNativeTest)
    val androidNative64BitMain = newSourceSet("${name}64BitMain", androidNativeMain)
    val androidNative64BitTest = newSourceSet("${name}64BitTest", androidNativeTest)
    val targets32 = listOf(androidNativeX86(), androidNativeArm32())
    val targets64 = listOf(androidNativeX64(), androidNativeArm64())
    targets32.forEach {
        newSourceSet(it.compilations["main"].defaultSourceSet.name, androidNative32BitMain)
        newSourceSet(it.compilations["test"].defaultSourceSet.name, androidNative32BitTest)
        it.configure()
    }
    targets64.forEach {
        newSourceSet(it.compilations["main"].defaultSourceSet.name, androidNative64BitMain)
        newSourceSet(it.compilations["test"].defaultSourceSet.name, androidNative64BitTest)
        it.configure()
    }
}

kotlin {
    android("androidJvm") {
        // This enables the KMP android publication.
        publishLibraryVariants("release")
    }
    androidNative {
        binaries {
            sharedLib("bugs", listOf(RELEASE))
        }
    }
    sourceSets {
        getByName("androidJvmMain") {
            dependencies {
                // Sample dependency
                api("androidx.annotation:annotation:1.1.0")
            }
        }
    }
}
