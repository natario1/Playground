import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

val repoDir = File(rootDir, "maven")
allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(repoDir)
    }
}

subprojects {
    // ./gradlew library:clean library:publishAllPublicationsToFooRepository
    // ./gradlew library2:clean library2:publishAllPublicationsToFooRepository
    group = "dev.natario.playground.kmp-bug"
    version = "0.1.0"
    pluginManager.apply("maven-publish")
    pluginManager.apply("org.jetbrains.kotlin.multiplatform")

    extensions.configure<PublishingExtension>("publishing") {
        repositories {
            maven(repoDir) { name = "foo" }
        }
    }

    extensions.configure<KotlinMultiplatformExtension>("kotlin") {
        iosArm64()
    }
}

tasks.register("clean", Delete::class) {
    delete(buildDir)
}