package com.example.sorting_app.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*

import com.example.sorting_app.R
import java.util.LinkedList

import java.util.concurrent.*

/*
The implementation of threaded merge sort was done by Muhammad Hussain
21K-3584
 */

fun mergeSortNonThreaded(arr: IntArray, start: Int, end: Int) {
    if (start >= end) return
    val mid = (start + end) / 2
    mergeSortNonThreaded(arr, start, mid)
    mergeSortNonThreaded(arr, mid + 1, end)
    mergeNonThreaded(arr, start, mid, end)
}

fun mergeNonThreaded(arr: IntArray, start: Int, mid: Int, end: Int) {
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

/*
This implementation of Quick Sort was done by Vania Abbas
21K-3584
 */

fun quickSort(arr: IntArray, left: Int, right: Int) {
    if (left >= right) {
        return
    }
    val pivot = arr[(left + right) / 2]
    val index = partition(arr, left, right, pivot)
    quickSort(arr, left, index - 1)
    quickSort(arr, index, right)
}

fun partition(arr: IntArray, left: Int, right: Int, pivot: Int): Int {
    var i = left
    var j = right
    while (i <= j) {
        while (arr[i] < pivot) {
            i++
        }
        while (arr[j] > pivot) {
            j--
        }
        if (i <= j) {
            val temp = arr[i]
            arr[i] = arr[j]
            arr[j] = temp
            i++
            j--
        }
    }
    return i
}


fun calculateAverage(list: LinkedList<Float>): Float{
    var averageTime = 0f
    for (time in list){
        averageTime += time
    }
    averageTime /= list.size
    return averageTime
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
        val wasThreadUsed = findViewById<TextView>(R.id.thread_used)
        val timeToFillTextView = findViewById<TextView>(R.id.time_to_fill_text_view)
        val avgTimePrinter = findViewById<TextView>(R.id.avg_time_text_view)
        output_text_view.movementMethod = ScrollingMovementMethod()
        // This will set the spinner dropdown
        val mySortingList = listOf("Merge Sort Kotlin", "Quick Sort C++")

        var avgTimeToSortMergeSortThreaded = LinkedList<Float>() // This will calculate avg time to sort using merge sort
        var avgTimeToSortQuickSortThreaded = LinkedList<Float>() // This will calculate avg time to sort using quick sort
        var avgTimeToMergeSort = LinkedList<Float>()
        var avgTimeToQuickSort = LinkedList<Float>()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            mySortingList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dropdown.adapter = adapter

        var listener:String = ""// This listener variable has our selected algorithm name
        dropdown?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                listener = mySortingList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nothing to do here
            }
        }
        var oldSize = 0
        val threadBox = findViewById<CheckBox>(R.id.thread_check_box) // Get the id of thread box
        sort_button.setOnClickListener {
            /*
                Once the user clicks the sort button, this block of code executes
             */
            val size = input_field.text.toString().toInt() // Parse the text received to
            if(oldSize != size)
            {
                avgTimeToSortMergeSortThreaded.clear()
                avgTimeToMergeSort.clear()
                avgTimeToQuickSort.clear()
                avgTimeToSortQuickSortThreaded.clear()
            }
            oldSize = size
            if (threadBox.isChecked && size >= 5000 && listener == "Merge Sort Kotlin")
            {

                // Create an array of the size specified and fill it with random variables
                var array = IntArray(size){0}
                val timeTakenToFillArray = measureTimeMillis {
                    array = IntArray(size) { Random.nextInt(0, size*2) }
                }.toFloat() / 1000
                // Create a different array which will hold the sorted elements
                var sortedArray: IntArray
                // Time the sorting of the array, time in milliseconds is converted to seconds
                val numThreads = Runtime.getRuntime().availableProcessors()
                val threadPool = ThreadPoolExecutor(
                    numThreads,  // core pool size
                    numThreads,  // maximum pool size
                    60L,        // keep-alive time (in seconds)
                    TimeUnit.SECONDS,
                    LinkedBlockingQueue<Runnable>()
                )

                val freeThreads = threadPool.maximumPoolSize - threadPool.activeCount
                val timeTaken = measureTimeMillis {
                    sortedArray = threadedMergeSort(array, freeThreads)
                }.toFloat() / 1000

                avgTimeToSortMergeSortThreaded.addAll(listOf(timeTaken)) // Fill in the array with the time

                val averageTimeToSort = calculateAverage(avgTimeToSortMergeSortThreaded)
                // Use a new array to print a limited number of elements
                // printing millions of array crashes the app
                var arrayToPrint = IntArray(50){0}
                for (i in 0..arrayToPrint.size - 1)
                {
                    arrayToPrint[i] = sortedArray[i]
                }
                runOnUiThread{
                    timeToFillTextView.text = "Time taken to fill array: ${"%.6f".format(timeTakenToFillArray)}s"
                }
                runOnUiThread {
                    output_text_view.text = arrayToPrint.joinToString()
                }
                runOnUiThread{
                    wasThreadUsed.text = "Yes, thread count: " + freeThreads
                }
                runOnUiThread {
                    time_text_view.text = "Time taken to sort array: ${"%.6f".format(timeTaken)}s"
                }
                runOnUiThread{
                    // This block of code executes if the threaded version is selected
                    algorithm_used.text = "Algorithm selected: Merge Sort"
                }
                runOnUiThread{
                    avgTimePrinter.text = "Average time to sort: ${"%.6f".format(averageTimeToSort)}s"
                }
            }
            else if (listener == "Merge Sort Kotlin")
            {
                if (size < 5000)
                {
                    val toastMessage = getString(R.string.toast_msg)
                    val durationOfToast = Toast.LENGTH_LONG

                    val toast = Toast.makeText(applicationContext, toastMessage, durationOfToast)
                    toast.show()
                }
                var array = IntArray(size) {0}
                val timeTakenToFillArray = measureTimeMillis {
                    array = IntArray(size) { Random.nextInt(0, size*2) }
                }.toFloat() / 1000
                val timeTaken = measureTimeMillis {
                    mergeSortNonThreaded(array, 0, array.size-1)
                }.toFloat() / 1000
                avgTimeToMergeSort.addAll(listOf(timeTaken))
                val averageTime = calculateAverage(avgTimeToMergeSort)
                var arrayToPrint = IntArray(50){0}
                for (i in 0..arrayToPrint.size - 1)
                {
                    arrayToPrint[i] = array[i]
                }
                runOnUiThread{
                    timeToFillTextView.text = "Time taken to fill array: ${"%.6f".format(timeTakenToFillArray)}s"
                }
                runOnUiThread {
                    output_text_view.text = arrayToPrint.joinToString()
                }
                runOnUiThread{
                    wasThreadUsed.text = "No"
                }
                runOnUiThread{
                    time_text_view.text = "Time taken to sort array: ${"%.6f".format(timeTaken)}s"
                }
                runOnUiThread{
                    algorithm_used.text = "Algorithm selected: Merge Sort"
                }
                runOnUiThread{
                    avgTimePrinter.text = "Average time to sort: ${"%.6f".format(averageTime)}s"
                }
            }

            else if (listener == "Quick Sort C++" && threadBox.isChecked && size >= 5000)
            {
                var initialArray = IntArray(size){0}
                val timeTakenToFillArray = measureTimeMillis {
                    initialArray = IntArray(size){Random.nextInt(0, size*2)}
                }.toFloat() / 1000
                var sortedArray:IntArray
                val timeTakenToQuickSort = measureTimeMillis{
                    sortedArray = sortArray(initialArray)
                }.toFloat() / 1000

                avgTimeToSortQuickSortThreaded.addAll(listOf(timeTakenToQuickSort))
                val averageTime = calculateAverage(avgTimeToSortQuickSortThreaded)
                var arrayToPrint = IntArray(50){0}
                for (i in 0..arrayToPrint.size-1){
                    arrayToPrint[i] = sortedArray[i]
                }
                runOnUiThread{
                    timeToFillTextView.text = "Time taken to fill array: ${"%.6f".format(timeTakenToFillArray)}s"
                    output_text_view.text = arrayToPrint.joinToString()
                    algorithm_used.text = "Quick Sort"
                    wasThreadUsed.text = "Yes, thread count: " + Runtime.getRuntime().availableProcessors()
                    time_text_view.text = "Time taken to sort: ${"%.6f".format(timeTakenToQuickSort)}s"
                    avgTimePrinter.text = "Average time to sort: ${"%.6f".format(averageTime)}s"
                }
            }
            else if (listener == "Quick Sort C++")
            {
                if (size < 5000)
                {
                    val toastMessage = getString(R.string.toast_msg)
                    val durationOfToast = Toast.LENGTH_LONG

                    val toast = Toast.makeText(applicationContext, toastMessage, durationOfToast)
                    toast.show()
                }
                var initialArray = IntArray(size){0}
                val timeTakenToFillArray = measureTimeMillis{
                    initialArray = IntArray(size){Random.nextInt(0, size*2)}
                }.toFloat() / 1000
                var sortedArray:IntArray
                val timeTakenToQuickSort = measureTimeMillis {
                   // quickSort(initialArray, 0, initialArray.size - 1)
                    sortedArray = quickSortNonThreadedCPP(initialArray)
                }.toFloat() / 1000

                avgTimeToQuickSort.addAll(listOf(timeTakenToQuickSort))
                val averageTime = calculateAverage(avgTimeToQuickSort)

                var arrayToPrint = IntArray(50){0}
                for (i in 0..arrayToPrint.size - 1){
                    arrayToPrint[i] = sortedArray[i]
                }
                runOnUiThread{
                    output_text_view.text = arrayToPrint.joinToString()
                    time_text_view.text = "Time taken to sort: ${"%.6f".format(timeTakenToQuickSort)}s"
                    timeToFillTextView.text = "Time taken to fill array: ${"%.6f".format(timeTakenToFillArray)}s"
                    algorithm_used.text = "Quick Sort"
                    wasThreadUsed.text = "No"
                    avgTimePrinter.text = "Average time to sort: ${"%.6f".format(averageTime)}s"
                }
            }
        }
    }

    external fun sortArray(arr: IntArray): IntArray
    external fun quickSortNonThreadedCPP(arr: IntArray): IntArray
    companion object{
        init{
            System.loadLibrary("sorting_app")
        }
    }
}
