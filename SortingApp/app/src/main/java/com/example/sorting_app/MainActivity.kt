package com.example.sorting_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun mergeSort(arr: Array<Int>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    mergeSort(arr, start, mid)
    mergeSort(arr, mid + 1, end)
    merge(arr, start, mid, end)
}

fun merge(arr: Array<Int>, start: Int, mid: Int, end: Int) {
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

fun mergeSortMultiThreaded(arr: Array<Int>, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    if (end - start <= 1000) {
        mergeSort(arr, start, end)
    } else {
        thread { mergeSortMultiThreaded(arr, start, mid) }
        thread { mergeSortMultiThreaded(arr, mid + 1, end) }
    }
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

        ArrayAdapter.createFromResource(
            this,
            R.array.algorithm_list,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dropdown.adapter = adapter
        }
        val threadBox = findViewById<CheckBox>(R.id.thread_check_box)
        val useThreads = threadBox.isChecked
        sort_button.setOnClickListener {
            val size = input_field.text.toString().toInt()

            if (useThreads)
            {
                thread {
                    val array = Array(size) { Random.nextInt(0, size) }
                    val timeTaken = measureTimeMillis {
                        mergeSortMultiThreaded(array, 0, array.size - 1)
                        runOnUiThread {
                            output_text_view.text = array.joinToString()
                        }
                    }.toFloat() / 1000

                    runOnUiThread {
                        time_text_view.text = "Time taken: ${"%.4f".format(timeTaken)}ms"
                    }
                }
            }
            else
            {
                val array = Array(size) {_ ->
                    Random.nextInt(0, size)
                }

                val timeTaken = measureTimeMillis {
                    mergeSort(array, 0, array.size - 1)
                }.toFloat() / 1000

                output_text_view.text = array.joinToString()

                time_text_view.text = "Time taken: ${"%.4f".format(timeTaken)}ms"
            }

        }
    }
}
