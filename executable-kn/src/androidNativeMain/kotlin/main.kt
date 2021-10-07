import platform.android.ANDROID_LOG_INFO
import platform.android.__android_log_write
import platform.posix.sleep
import kotlin.native.internal.test.main

fun main() {
    println("KN: Hello world! Sleeping for 5 seconds...")
    __android_log_write(ANDROID_LOG_INFO.toInt(),"Playground", "KN: Hello world! Sleeping for 5 seconds...");

    sleep(5)

    println("KN: Slept for 5 seconds! Exiting.")
    __android_log_write(ANDROID_LOG_INFO.toInt(),"Playground", "KN: Slept for 5 seconds! Exiting.");
}