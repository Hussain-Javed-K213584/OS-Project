#include <jni.h>
#include <iostream>
#include <algorithm>
#include <thread>

using namespace std;

const int MAX_THREADS = 8;

void quicksort(int* arr, int left, int right);

void parallel_quicksort(jint* arr, int left, int right, int depth) {
    if (depth >= MAX_THREADS || right - left <= 10000) {
        quicksort(arr, left, right);
        return;
    }

    int i = left, j = right;
    int pivot = arr[(left + right) / 2];

    while (i <= j) {
        while (arr[i] < pivot) {
            i++;
        }
        while (arr[j] > pivot) {
            j--;
        }
        if (i <= j) {
            swap(arr[i], arr[j]);
            i++;
            j--;
        }
    }

    thread left_thread(parallel_quicksort, arr, left, j, depth + 1);
    thread right_thread(parallel_quicksort, arr, i, right, depth + 1);

    left_thread.join();
    right_thread.join();
}

void quicksort(jint* arr, int left, int right) {
    if (left >= right) {
        return;
    }

    int i = left, j = right;
    int pivot = arr[(left + right) / 2];

    while (i <= j) {
        while (arr[i] < pivot) {
            i++;
        }
        while (arr[j] > pivot) {
            j--;
        }
        if (i <= j) {
            swap(arr[i], arr[j]);
            i++;
            j--;
        }
    }

    quicksort(arr, left, j);
    quicksort(arr, i, right);
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_sorting_1app_kotlin_MainActivity_sortArray(JNIEnv *env, jobject thiz,
                                                            jintArray arr) {
        // TODO: implement sortArray()
        jint* buffer = env->GetIntArrayElements(arr, NULL);
        jsize length = env->GetArrayLength(arr);
//        int *arrayCPP = new int[length];
//        for (int i = 0; i < length; i++)
//        {
//            arrayCPP[i] = buffer[i];
//        }
    parallel_quicksort(buffer, 0, length-1, 0);
        jintArray result = env->NewIntArray(length);
        env->SetIntArrayRegion(result, 0, length, (const jint*)buffer);
//        delete[] arrayCPP;
        return result;
}