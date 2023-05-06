import matplotlib.pyplot as plt
import numpy as np

mergeSortThreadedX = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
mergeSortThreadedY = np.array([0.001, 0.002, 0.005, 0.009, 0.015, 0.067, 0.088, 0.126, 0.141, 0.365, 0.522, 0.783, 0.912, 1.108, 1.377, 1.608, 1.867, 2.12])

mergeSortNonThreadX = np.array([1000, 5000 ,10000, 50000, 100000, 500000, 650000, 850000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000])
mergeSortNonThreadY = np.array([0.001, 0.002, 0.003, 0.014, 0.025, 0.138, 0.187, 0.245, 0.286, 0.596, 0.926, 1.245, 1.578, 1.967, 2.285, 2.6, 2.976, 3.297])

plt.xlabel("Elements in array")
plt.ylabel("Time in seconds(s)")
plt.title("Merge Sort Analysis")
plt.ticklabel_format(style="plain", axis="x")
x_ticks = np.arange(0, 20000000, 1000000)
plt.xticks(x_ticks)
plt.plot(mergeSortThreadedX, mergeSortThreadedY, marker="o", label="Threaded")
plt.plot(mergeSortNonThreadX, mergeSortNonThreadY, marker="o", label="Non-Threaded")
plt.grid(axis="y")
plt.legend()
plt.show()