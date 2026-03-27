#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ctf_hauntedhotel_Room106Activity_getFlagFromNative(JNIEnv* env, jobject) {

    unsigned char encrypted[] = {
            0x01, 0x16, 0x04, 0x39,
            0x21, 0x72, 0x2E, 0x2E,
            0x71, 0x21, 0x36, 0x72,
            0x30, 0x1D, 0x73, 0x72,
            0x74, 0x3F,
            0x00
    };

    for (int i = 0; encrypted[i]; i++) {
        encrypted[i] ^= 0x42;
    }

    return env->NewStringUTF((char*)encrypted);
}