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
            sharedLib("library2", listOf(RELEASE))
        }
    }
    sourceSets["commonMain"].dependencies {
        api(project(":library"))
    }
}

// Workaround for https://youtrack.jetbrains.com/issue/KT-41887
// or the project won't even compile.
afterEvaluate {
    configurations.configureEach {
        if (name == "metadataCompileClasspath") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, project.objects.named("kotlin-metadata"))
            }
        }
    }
}
