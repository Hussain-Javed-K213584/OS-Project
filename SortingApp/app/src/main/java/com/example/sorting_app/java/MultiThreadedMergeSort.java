package com.example.sorting_app.java;

import java.util.concurrent.*;

public class MultiThreadedMergeSort {
    private static ExecutorService pool;

    public static void parallelMergeSort(int[] arr) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(numThreads);
        parallelMergeSort(arr, 0, arr.length - 1);
        pool.shutdown();
    }

    private static void parallelMergeSort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            Future<?> left = pool.submit(() -> parallelMergeSort(arr, start, mid));
            Future<?> right = pool.submit(() -> parallelMergeSort(arr, mid + 1, end));

            try {
                left.get();
                right.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            merge(arr, start, mid, end);
        }
    }

    private static void merge(int[] arr, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        while (j <= end) {
            temp[k++] = arr[j++];
        }

        for (i = start; i <= end; i++) {
            arr[i] = temp[i - start];
        }
    }
}

