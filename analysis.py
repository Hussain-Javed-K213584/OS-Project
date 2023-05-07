import matplotlib.pyplot as plt
import numpy as np

mergeSortThreadedY = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
mergeSortThreadedX = np.array([0.001, 0.002, 0.005, 0.009, 0.015, 0.067, 0.088, 0.126, 0.141, 0.365, 0.522, 0.783, 0.912, 1.108, 1.377, 1.608, 1.867, 2.12])

mergeSortNonThreadY = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
mergeSortNonThreadX = np.array([0.001, 0.002, 0.003, 0.014, 0.025, 0.138, 0.187, 0.245, 0.286, 0.596, 0.926, 1.245, 1.578, 1.967, 2.285, 2.6, 2.976, 3.297])

quickSortThreadedY = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
quickSortThreadedX = np.array([0.00, 0.001, 0.003, 0.08, 0.013, 0.072, 0.087, 0.116, 0.128, 0.392, 0.348, 0.529, 0.589, 0.64, 0.756, 0.779, 1.054, 1.346])

quickSortNonThreadedY = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
quickSortNonThreadedX = np.array([0.001, 0.002, 0.004, 0.021, 0.046, 0.255, 0.347, 0.471, 0.575, 1.136, 1.748, 2.405, 3.38, 3.694, 4.342, 5.10, 5.83, 6.394])

plt.xlabel("Time in seconds(s)")
plt.ylabel("Elements in array")
plt.title("Sorting Analysis Kotlin and C++")
plt.ticklabel_format(style="plain", axis="both")
y_ticks = np.arange(0, 20000000, 1000000)
plt.yticks(y_ticks)
plt.plot(mergeSortThreadedX, mergeSortThreadedY, marker="o", label="Merge Sort Threaded Kotlin")
plt.plot(mergeSortNonThreadX, mergeSortNonThreadY, marker="o", label="Merge Sort Non-Threaded Kotlin")
plt.plot(quickSortThreadedX, quickSortThreadedY, marker="o", label="Quick Sort Threaded C++")
plt.plot(quickSortNonThreadedX, quickSortNonThreadedY, marker="o", label="Quick Sort Non-Threaded C++")
plt.grid(axis="y")
plt.legend(loc="lower right")
plt.show()