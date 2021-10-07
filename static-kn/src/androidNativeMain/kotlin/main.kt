import platform.android.ANDROID_LOG_INFO
import platform.android.__android_log_write

@CName("HELLO")
fun hello() {
    println("KN: Hello world!")
    __android_log_write(ANDROID_LOG_INFO.toInt(),"Playground", "KN: Hello world!");
}