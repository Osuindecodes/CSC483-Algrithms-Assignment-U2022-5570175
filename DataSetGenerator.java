package com.csc483.assignment2.sorting;

import java.util.*;

/**
 * Generates datasets with various characteristics for benchmarking.
 */
public class DataSetGenerator {
    private static final Random random = new Random(42); // Fixed seed for reproducibility
    
    /**
     * Generates an array with random integers.
     */
    public static Integer[] generateRandom(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
    
    /**
     * Generates an already sorted array.
     */
    public static Integer[] generateSorted(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }
    
    /**
     * Generates a reverse sorted array.
     */
    public static Integer[] generateReverseSorted(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }
    
    /**
     * Generates a nearly sorted array (90% sorted, 10% random).
     */
    public static Integer[] generateNearlySorted(int size) {
        Integer[] arr = generateSorted(size);
        
        // Randomly swap about 10% of elements
        int numSwaps = size / 10;
        for (int i = 0; i < numSwaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        
        return arr;
    }
    
    /**
     * Generates an array with many duplicates (only 10 distinct values).
     */
    public static Integer[] generateManyDuplicates(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10); // Only 0-9
        }
        return arr;
    }
    
    /**
     * Verifies if an array is sorted.
     */
    public static <T extends Comparable<T>> boolean isSorted(T[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}
