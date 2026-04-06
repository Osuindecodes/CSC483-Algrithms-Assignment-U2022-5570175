package com.csc483.assignment2.sorting;

import java.util.*;

/**
 * Implementation of various sorting algorithms for benchmarking.
 */
public class SortingAlgorithms {
    
    // ==================== BUBBLE SORT ====================
    public static <T extends Comparable<T>> SortResult bubbleSort(T[] arr) {
        T[] array = Arrays.copyOf(arr, arr.length);
        long comparisons = 0;
        long swaps = 0;
        long startTime = System.nanoTime();
        
        int n = array.length;
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (array[j].compareTo(array[j + 1]) > 0) {
                    // Swap
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        
        long endTime = System.nanoTime();
        return new SortResult(array, endTime - startTime, comparisons, swaps);
    }
    
    // ==================== INSERTION SORT ====================
    public static <T extends Comparable<T>> SortResult insertionSort(T[] arr) {
        T[] array = Arrays.copyOf(arr, arr.length);
        long comparisons = 0;
        long swaps = 0;
        long startTime = System.nanoTime();
        
        int n = array.length;
        for (int i = 1; i < n; i++) {
            T key = array[i];
            int j = i - 1;
            
            comparisons++;
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                swaps++;
                j--;
                if (j >= 0) comparisons++;
            }
            array[j + 1] = key;
            if (j + 1 != i) swaps++; // Count the final placement as a swap
        }
        
        long endTime = System.nanoTime();
        return new SortResult(array, endTime - startTime, comparisons, swaps);
    }
    
    // ==================== MERGE SORT ====================
    public static <T extends Comparable<T>> SortResult mergeSort(T[] arr) {
        T[] array = Arrays.copyOf(arr, arr.length);
        long[] metrics = new long[2]; // [comparisons, moves]
        long startTime = System.nanoTime();
        
        mergeSortHelper(array, 0, array.length - 1, metrics);
        
        long endTime = System.nanoTime();
        return new SortResult(array, endTime - startTime, metrics[0], metrics[1]);
    }
    
    private static <T extends Comparable<T>> void mergeSortHelper(T[] arr, int left, int right, long[] metrics) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(arr, left, mid, metrics);
            mergeSortHelper(arr, mid + 1, right, metrics);
            merge(arr, left, mid, right, metrics);
        }
    }
    
    private static <T extends Comparable<T>> void merge(T[] arr, int left, int mid, int right, long[] metrics) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        @SuppressWarnings("unchecked")
        T[] leftArr = (T[]) new Comparable[n1];
        @SuppressWarnings("unchecked")
        T[] rightArr = (T[]) new Comparable[n2];
        
        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);
        
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            metrics[0]++; // comparison
            if (leftArr[i].compareTo(rightArr[j]) <= 0) {
                arr[k++] = leftArr[i++];
                metrics[1]++; // move
            } else {
                arr[k++] = rightArr[j++];
                metrics[1]++; // move
            }
        }
        
        while (i < n1) {
            arr[k++] = leftArr[i++];
            metrics[1]++; // move
        }
        
        while (j < n2) {
            arr[k++] = rightArr[j++];
            metrics[1]++; // move
        }
    }
    
    // ==================== QUICK SORT ====================
    public static <T extends Comparable<T>> SortResult quickSort(T[] arr) {
        T[] array = Arrays.copyOf(arr, arr.length);
        long[] metrics = new long[2]; // [comparisons, swaps]
        long startTime = System.nanoTime();
        
        quickSortHelper(array, 0, array.length - 1, metrics);
        
        long endTime = System.nanoTime();
        return new SortResult(array, endTime - startTime, metrics[0], metrics[1]);
    }
    
    private static <T extends Comparable<T>> void quickSortHelper(T[] arr, int low, int high, long[] metrics) {
        if (low < high) {
            int pi = partition(arr, low, high, metrics);
            quickSortHelper(arr, low, pi - 1, metrics);
            quickSortHelper(arr, pi + 1, high, metrics);
        }
    }
    
    private static <T extends Comparable<T>> int partition(T[] arr, int low, int high, long[] metrics) {
        // Median-of-three pivot selection to avoid worst-case
        int mid = low + (high - low) / 2;
        if (arr[mid].compareTo(arr[low]) < 0) swap(arr, low, mid, metrics);
        if (arr[high].compareTo(arr[low]) < 0) swap(arr, low, high, metrics);
        if (arr[high].compareTo(arr[mid]) < 0) swap(arr, mid, high, metrics);
        
        swap(arr, mid, high - 1, metrics); // Place pivot at high-1
        T pivot = arr[high - 1];
        
        int i = low;
        int j = high - 1;
        
        while (true) {
            while (arr[++i].compareTo(pivot) < 0) {
                metrics[0]++; // comparison (the condition check)
                if (i == high - 1) break;
            }
            metrics[0]++; // for the failed condition
            
            while (arr[--j].compareTo(pivot) > 0) {
                metrics[0]++; // comparison
                if (j == low) break;
            }
            metrics[0]++; // for the failed condition
            
            if (i >= j) break;
            
            swap(arr, i, j, metrics);
        }
        
        swap(arr, i, high - 1, metrics); // Restore pivot
        return i;
    }
    
    private static <T> void swap(T[] arr, int i, int j, long[] metrics) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        metrics[1]++; // count as a swap
    }
    
    // ==================== HEAP SORT ====================
    public static <T extends Comparable<T>> SortResult heapSort(T[] arr) {
        T[] array = Arrays.copyOf(arr, arr.length);
        long comparisons = 0;
        long swaps = 0;
        long startTime = System.nanoTime();
        
        int n = array.length;
        
        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            long[] result = heapify(array, n, i);
            comparisons += result[0];
            swaps += result[1];
        }
        
        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            T temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            swaps++;
            
            // Heapify reduced heap
            long[] result = heapify(array, i, 0);
            comparisons += result[0];
            swaps += result[1];
        }
        
        long endTime = System.nanoTime();
        return new SortResult(array, endTime - startTime, comparisons, swaps);
    }
    
    private static <T extends Comparable<T>> long[] heapify(T[] arr, int n, int i) {
        long comparisons = 0;
        long swaps = 0;
        
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        // Compare with left child
        if (left < n) {
            comparisons++;
            if (arr[left].compareTo(arr[largest]) > 0) {
                largest = left;
            }
        }
        
        // Compare with right child
        if (right < n) {
            comparisons++;
            if (arr[right].compareTo(arr[largest]) > 0) {
                largest = right;
            }
        }
        
        // If largest is not root
        if (largest != i) {
            T swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            swaps++;
            
            // Recursively heapify the affected sub-tree
            long[] recursiveResult = heapify(arr, n, largest);
            comparisons += recursiveResult[0];
            swaps += recursiveResult[1];
        }
        
        return new long[]{comparisons, swaps};
    }
    
    /**
     * Result container for sorting operations.
     */
    public static class SortResult {
        public final Comparable<?>[] sortedArray;
        public final long timeNanos;
        public final long comparisons;
        public final long swaps;
        
        public SortResult(Comparable<?>[] sortedArray, long timeNanos, long comparisons, long swaps) {
            this.sortedArray = sortedArray;
            this.timeNanos = timeNanos;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }
        
        public double timeMs() {
            return timeNanos / 1_000_000.0;
        }
    }
}