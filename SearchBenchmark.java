package com.csc483.assignment1.search;

import java.util.*;

/**
 * Benchmarks and compares search algorithm performance.
 */
public class SearchBenchmark {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("TECHMART SEARCH PERFORMANCE ANALYSIS");
        System.out.println("=".repeat(70));
        
        int[] sizes = {1000, 10000, 50000, 100000};
        
        for (int size : sizes) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("TESTING WITH " + size + " PRODUCTS");
            System.out.println("-".repeat(50));
            
            runBenchmark(size);
        }
    }
    
    private static void runBenchmark(int n) {
        // Generate data
        Product[] unsortedProducts = DataGenerator.generateRandomProducts(n);
        Product[] sortedProducts = DataGenerator.copyProducts(unsortedProducts);
        SearchMethods.sortProductsById(sortedProducts);
        
        // Select test IDs
        int firstId = sortedProducts[0].getProductId();
        int middleId = sortedProducts[n/2].getProductId();
        int lastId = sortedProducts[n-1].getProductId();
        int nonExistentId = 999999;
        int randomId = sortedProducts[random.nextInt(n)].getProductId();
        
        // Test sequential search
        System.out.println("\nSEQUENTIAL SEARCH:");
        testSequentialSearch(unsortedProducts, firstId, "Best Case (first element)");
        testSequentialSearch(unsortedProducts, middleId, "Average Case (middle)");
        testSequentialSearch(unsortedProducts, lastId, "Worst Case (last element)");
        testSequentialSearch(unsortedProducts, nonExistentId, "Not Found Case");
        
        // Test binary search
        System.out.println("\nBINARY SEARCH:");
        testBinarySearch(sortedProducts, middleId, "Best Case (middle)");
        testBinarySearch(sortedProducts, firstId, "Average Case (first)");
        testBinarySearch(sortedProducts, lastId, "Average Case (last)");
        testBinarySearch(sortedProducts, nonExistentId, "Worst Case (not found)");
        
        // Test name search
        System.out.println("\nNAME SEARCH (Sequential):");
        testNameSearch(unsortedProducts, "Laptop");
    }
    
    private static void testSequentialSearch(Product[] products, int targetId, String caseType) {
        long startTime = System.nanoTime();
        Product result = SearchMethods.sequentialSearchById(products, targetId);
        long endTime = System.nanoTime();
        
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-30s: %8.3f ms %s%n", 
            caseType, timeMs, (result != null ? "(Found)" : "(Not Found)"));
    }
    
    private static void testBinarySearch(Product[] products, int targetId, String caseType) {
        long startTime = System.nanoTime();
        Product result = SearchMethods.binarySearchById(products, targetId);
        long endTime = System.nanoTime();
        
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-30s: %8.3f ms %s%n", 
            caseType, timeMs, (result != null ? "(Found)" : "(Not Found)"));
    }
    
    private static void testNameSearch(Product[] products, String targetName) {
        long startTime = System.nanoTime();
        List<Product> results = SearchMethods.searchByName(products, targetName);
        long endTime = System.nanoTime();
        
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-30s: %8.3f ms (Found %d matches)%n", 
            "Search for '" + targetName + "'", timeMs, results.size());
    }
    
    private static final Random random = new Random(42);
}