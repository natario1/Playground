plugins {
    kotlin("android")
    id("com.android.application")
}

val archs = mapOf(
    "Arm32" to "armeabi-v7a",
    "Arm64" to "arm64-v8a",
    "X86" to "x86",
    "X64" to "x86_64"
)

android {
    compileSdk = 31
    ndkVersion = "21.3.6528147"
    defaultConfig {
        targetSdk = 31
        applicationId = "io.deepmedia.tools.testing.ndk"
        ndk {
            abiFilters.addAll(archs.values)
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
}

/**
 * Define the run task for each architecture. Choose one based on the running emulator/device arch,
 * for example ./gradlew executable-cpp:runX86 for a x86 emulator.
 */
archs.forEach { (name, arch) ->
    tasks.register("run${name.capitalize()}") {
        dependsOn("externalNativeBuildDebug")
        doLast {
            val executable = file("build/intermediates/cmake/debug/obj/${arch}/playground")
            exec {
                setExecutable("adb")
                args("push", executable.absolutePath, "/data/local/tmp")
            }
            exec {
                setExecutable("adb")
                args("shell", "/data/local/tmp/playground")
            }
        }
    }
}

