# Operating Systems Project Spring 2023
## Comparison of sorting algorithms in android using threads

This project was undertaken by 3 studens from FAST National University. Our goal was to analyze the use of multiple threads in sorting algorithms. We wanted to see if android applications can utilize their multicore processors in speeding up the sorting time of arrays. We have implemented two sorting algorithms in this project, Merge Sort and Quick Sort. Both of these algorithms have their threaded and non-threaded versions. The merge sort algorithm was written in Kotlin while Quick Sort algorithm was written in C++.

## Merge Sort

We first compared the threaded version of merge sort with the non threaded version of merge sort. The total number of threads available on my device were 8 so this analysis uses the threaded version of merge sort with 8 threads.

![merge_sort_graph](images/merge_sort_analysis.png)

We can see from the above graph that as the size of our array increased the time delta between the threaded and non-threaded version also increased. I (Hussain Javed) was not able test with more data due to either the limit of my android phone or due to a bug in the application as I could not create an array greater than 17 million integers. The divide and conquer approach of the merge sort algorithm is the reason we can see a difference in time between the threaded and non-threaded version of the algorithm. As the array size increased, the algorithm divided the arrays into subarrays between threads which resulted in quicker sorting. When sorting data of 10 million arrays we can see a 1 second difference between both the merge sort versions.
