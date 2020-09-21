import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kotlin-multiplatform")
}

fun KotlinMultiplatformExtension.newSourceSet(name: String, parent: KotlinSourceSet): KotlinSourceSet {
    return sourceSets.maybeCreate(name).apply {
        dependsOn(parent)
    }
}

fun KotlinMultiplatformExtension.androidNativeOld(name: String = "androidNative", configure: KotlinNativeTarget.() -> Unit) {
    val androidNativeMain = newSourceSet("${name}Main", sourceSets["commonMain"])
    val androidNative32BitMain = newSourceSet("${name}32BitMain", androidNativeMain)
    val androidNative64BitMain = newSourceSet("${name}64BitMain", androidNativeMain)
    val targets32 = listOf(androidNativeX86(), androidNativeArm32())
    val targets64 = listOf(androidNativeX64(), androidNativeArm64())
    targets32.forEach {
        newSourceSet(it.compilations["main"].defaultSourceSet.name, androidNative32BitMain)
        it.configure()
    }
    targets64.forEach {
        newSourceSet(it.compilations["main"].defaultSourceSet.name, androidNative64BitMain)
        it.configure()
    }
}

fun KotlinMultiplatformExtension.androidNative(name: String = "androidNative", configure: KotlinNativeTarget.() -> Unit) {
    val androidNativeMain = newSourceSet("${name}Main", sourceSets["commonMain"])
    val targets = listOf(androidNativeX86(), androidNativeArm32(), androidNativeX64(), androidNativeArm64())
    targets.forEach {
        newSourceSet(it.compilations["main"].defaultSourceSet.name, androidNativeMain)
        it.configure()
    }
}

kotlin {
    androidNative {
        binaries {
            sharedLib("library", listOf(RELEASE))
        }
    }
    sourceSets["commonMain"].dependencies {
        api("com.otaliastudios.opengl:egloo-multiplatform:0.5.3")
    }
}
