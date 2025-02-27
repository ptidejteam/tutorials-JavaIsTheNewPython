#include <cstring>
#include "net_ptidej_panama_jni_Main.h"

JNIEXPORT jint JNICALL Java_net_ptidej_panama_jni_Main_stringLength(JNIEnv *env, jclass jc, jstring js) {
	const char* s = env->GetStringUTFChars(js, NULL);
	return strlen(s);
}