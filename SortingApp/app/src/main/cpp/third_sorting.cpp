//
// Created by Hussain on 06/05/2023.
//

#include <jni.h>
#include <algorithm>

int partition(int arr[], int start, int end)
{

    int pivot = arr[start];

    int count = 0;
    for (int i = start + 1; i <= end; i++) {
        if (arr[i] <= pivot)
            count++;
    }

    int pivotIndex = start + count;
    std::swap(arr[pivotIndex], arr[start]);

    int i = start, j = end;

    while (i < pivotIndex && j > pivotIndex) {

        while (arr[i] <= pivot) {
            i++;
        }

        while (arr[j] > pivot) {
            j--;
        }

        if (i < pivotIndex && j > pivotIndex) {
            std::swap(arr[i++], arr[j--]);
        }
    }

    return pivotIndex;
}

void quickSortNonThreaded(jint* array, int start, int end)
{
    if (start >= end)
        return;
    int pivot = partition(array, start, end);
    quickSortNonThreaded(array, start, pivot-1);
    quickSortNonThreaded(array, pivot+1, end);
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_sorting_1app_kotlin_MainActivity_quickSortNonThreadedCPP(JNIEnv *env, jobject thiz,
                                                            jintArray arr) {
    // TODO: implement sortArray()
    jint* buffer = env->GetIntArrayElements(arr, NULL);
    jsize length = env->GetArrayLength(arr);
//        int *arrayCPP = new int[length];
//        for (int i = 0; i < length; i++)
//        {
//            arrayCPP[i] = buffer[i];
//        }
    quickSortNonThreaded(buffer, 0, length-1);
    jintArray result = env->NewIntArray(length);
    env->SetIntArrayRegion(result, 0, length, (const jint*)buffer);
//        delete[] arrayCPP;
    return result;
}