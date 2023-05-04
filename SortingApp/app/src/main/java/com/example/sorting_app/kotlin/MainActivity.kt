package com.example.sorting_app.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.*
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.measureTimeMillis

import com.example.sorting_app.R
import com.example.sorting_app.java.MultiThreadedMergeSort

import java.util.concurrent.*

fun mergeSort(arr: IntArray, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    mergeSort(arr, start, mid)
    mergeSort(arr, mid + 1, end)
    merge(arr, start, mid, end)
}

fun merge(arr: IntArray, start: Int, mid: Int, end: Int) {
    val temp = IntArray(end - start + 1)
    var i = start
    var j = mid + 1
    var k = 0
    while (i <= mid && j <= end) {
        if (arr[i] <= arr[j]) {
            temp[k++] = arr[i++]
        } else {
            temp[k++] = arr[j++]
        }
    }
    while (i <= mid) {
        temp[k++] = arr[i++]
    }
    while (j <= end) {
        temp[k++] = arr[j++]
    }
    for (p in temp.indices) {
        arr[start + p] = temp[p]
    }
}

fun threadedMergeSort(arr: IntArray, numThreads: Int): IntArray {
    val pool = Executors.newFixedThreadPool(numThreads)
    val chunkSize = arr.size / numThreads

    // Divide the array into chunks and sort each chunk in a separate thread
    val futures = mutableListOf<Future<IntArray>>()
    for (i in 0 until numThreads) {
        val start = i * chunkSize
        val end = if (i == numThreads - 1) arr.size else (i + 1) * chunkSize
        futures.add(pool.submit(Callable { mergeSort(arr.copyOfRange(start, end)) }))
    }

    // Merge the sorted chunks together
    var result = futures[0].get()
    for (i in 1 until numThreads) {
        result = merge(result, futures[i].get())
    }

    pool.shutdown()

    return result
}

fun mergeSort(arr: IntArray): IntArray {
    if (arr.size <= 1) {
        return arr
    }

    val mid = arr.size / 2
    val left = arr.copyOfRange(0, mid)
    val right = arr.copyOfRange(mid, arr.size)

    return merge(mergeSort(left), mergeSort(right))
}

fun merge(left: IntArray, right: IntArray): IntArray {
    val result = IntArray(left.size + right.size)
    var i = 0
    var j = 0
    var k = 0

    while (i < left.size && j < right.size) {
        if (left[i] <= right[j]) {
            result[k++] = left[i++]
        } else {
            result[k++] = right[j++]
        }
    }

    while (i < left.size) {
        result[k++] = left[i++]
    }

    while (j < right.size) {
        result[k++] = right[j++]
    }

    return result
}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sort_button = findViewById<Button>(R.id.sort_button)
        val input_field = findViewById<EditText>(R.id.input_field)
        val output_text_view = findViewById<TextView>(R.id.output_text_view)
        val time_text_view = findViewById<TextView>(R.id.time_text_view)
        val dropdown = findViewById<Spinner>(R.id.select_algo)
        val algorithm_used = findViewById<TextView>(R.id.algorithm_used)
        output_text_view.movementMethod = ScrollingMovementMethod()
        // This will set the spinner dropdown
        ArrayAdapter.createFromResource(
            this,
            R.array.algorithm_list,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dropdown.adapter = adapter
        }

        val listener = dropdown.selectedItem.toString()
        val threadBox = findViewById<CheckBox>(R.id.thread_check_box)
        sort_button.setOnClickListener {
            val size = input_field.text.toString().toInt()
            if (threadBox.isChecked)
            {
                algorithm_used.text = "Algorithm Selected: Merge Sort"
                val array = IntArray(size) { Random.nextInt(0, size) }

                    val timeTaken = measureTimeMillis {
                        //mergeSortMultiThreaded(array, 0, array.size - 1)
                        val sortedArray = threadedMergeSort(array, 8)
                        runOnUiThread {
                            output_text_view.text = sortedArray[50].toString()
                        }
                    }.toFloat() / 1000

                    runOnUiThread {
                        time_text_view.text = "Time taken: ${"%.6f".format(timeTaken)}ms"
                    }

            }
            else
            {
                val array = IntArray(size) {_ ->
                    Random.nextInt(0, size)
                }

                val timeTaken = measureTimeMillis {
                    mergeSort(array, 0, array.size - 1)
                }.toFloat() / 1000
//                var arrayToPrint = Array(size){0}
//                for (i in 0..1000)
//                {
//                    arrayToPrint[i] = array[i]
//                }
                runOnUiThread {
                    output_text_view.text = array[50].toString()
                }
                runOnUiThread{
                    time_text_view.text = "Time taken: ${"%.6f".format(timeTaken)}ms"
                }

            }

        }
    }
}
