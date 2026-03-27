cmake_minimum_required(VERSION 3.10)
project("flag106")

add_library(flag106 SHARED flag106.cpp)

find_library(log-lib log)
target_link_libraries(flag106 ${log-lib})