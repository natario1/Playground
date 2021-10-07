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
            executable(listOf(DEBUG)) {
                baseName = "playgroundKn"
            }
        }
    }
}


val archs = listOf("Arm32", "Arm64", "X86", "X64")
    .associateWith { "androidNative$it" }

/**
 * Define the run task for each architecture. Choose one based on the running emulator/device arch,
 * for example ./gradlew executable-kn:runX86 for a x86 emulator.
 *
 * Note that after building the KN executable, all we have to do is:
 * 1. adb push {executable} /data/local/tmp
 * 2. adb shell /data/local/tmp/{executable}
 * This task is slightly more complex because it enables core dumps and pulls them after failure,
 * for debugging purposes. Find them in build/dumps/.
 */
archs.forEach { (archName, target) ->
    tasks.register("run${archName.capitalize()}") {
        dependsOn("linkDebugExecutable${target.capitalize()}")
        doLast {
            val fileName = "playgroundKn.so"
            val executable = file("build/bin/$target/debugExecutable/$fileName")
            // push executable to device
            exec {
                setExecutable("adb")
                args("push", executable.absolutePath, "/data/local/tmp")
            }
            // execute it. ulimit -c to enable core dumps
            val res = exec {
                isIgnoreExitValue = true
                setExecutable("adb")
                args("shell", "ulimit -c unlimited && cd /data/local/tmp && ./$fileName")
            }
            // pull dump on failure
            if (res.exitValue == 139) {
                val dump = file("build/dumps/$target/core")
                dump.parentFile!!.mkdirs()
                exec {
                    setExecutable("adb")
                    args("pull", "/data/local/tmp/core", dump.absolutePath)
                }
                println("Segfault! Analyze the core dump with the following command:")
                println("lldb --core ${dump.absolutePath} ${executable.absolutePath}")
            }
            // fail if failed
            res.rethrowFailure().assertNormalExitValue()
        }
    }
}
