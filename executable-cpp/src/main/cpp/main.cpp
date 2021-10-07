#include <iostream>
#include <chrono>
#include <thread>
#include "android/log.h"

int main() {
    std::cout << "CPP: Hello world! Sleeping for 5 seconds...\n";
    __android_log_write(ANDROID_LOG_INFO, "Playground", "CPP: Hello world! Sleeping for 5 seconds...");

    std::this_thread::sleep_for(std::chrono::seconds(5));

    std::cout << "CPP: Slept for 5 seconds! Exiting.\n";
    __android_log_write(ANDROID_LOG_INFO, "Playground", "CPP: Slept for 5 seconds! Exiting.");
    return 0;
}