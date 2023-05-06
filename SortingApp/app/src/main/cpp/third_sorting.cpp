//
// Created by Hussain on 06/05/2023.
//

#include <jni.h>


extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_sorting_1app_kotlin_MainActivity_sortArrayThirdAlgorithm(JNIEnv *env, jobject thiz,
                                                            jintArray arr) {
    // TODO: implement sortArray()
    jint* buffer = env->GetIntArrayElements(arr, NULL);
    jsize length = env->GetArrayLength(arr);
//        int *arrayCPP = new int[length];
//        for (int i = 0; i < length; i++)
//        {
//            arrayCPP[i] = buffer[i];
//        }
//    parallel_quicksort(buffer, 0, length-1, 0);
    jintArray result = env->NewIntArray(length);
    env->SetIntArrayRegion(result, 0, length, (const jint*)buffer);
//        delete[] arrayCPP;
    return result;
}