# https://developer.android.com/studio/projects/configure-cmake
cmake_minimum_required(VERSION 3.4.1)

# https://cmake.org/cmake/help/latest/command/project.html?highlight=project
project(playground)
add_executable(playground main.cpp)

# Use android logging
find_library(LOG_PATH log)
target_link_libraries(playground ${LOG_PATH})
