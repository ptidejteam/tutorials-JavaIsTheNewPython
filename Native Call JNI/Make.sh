#!/bin/bash
gcc -Wl,--add-stdcall-alias -shared -I"${JAVA_HOME}"/include -I"${JAVA_HOME}"/include/win32 -o net_ptidej_panama_jni_Main.dll net_ptidej_panama_jni_Main.cpp
