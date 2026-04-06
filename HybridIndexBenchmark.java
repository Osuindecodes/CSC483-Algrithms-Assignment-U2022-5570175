package com.csc483.assignment1.search;

import java.util.*;

/**
 * Benchmarks the hybrid search approach.
 */
public class HybridIndexBenchmark {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("HYBRID SEARCH INDEX PERFORMANCE ANALYSIS");
        System.out.println("=".repeat(70));
        
        int[] sizes = {1000, 10000, 50000, 100000};
        
        for (int size : sizes) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("TESTING WITH " + size + " PRODUCTS");
            System.out.println("-".repeat(50));
            
            benchmarkHybridIndex(size);
        }
    }
    
    private static void benchmarkHybridIndex(int n) {
        // Generate initial data
        Product[] initialProducts = DataGenerator.generateRandomProducts(n);
        
        // Build hybrid index
        long buildStart = System.nanoTime();
        HybridProductIndex index = new HybridProductIndex(initialProducts);
        long buildEnd = System.nanoTime();
        
        System.out.printf("Index build time: %.3f ms%n", (buildEnd - buildStart) / 1_000_000.0);
        
        // Test ID searches
        System.out.println("\nID SEARCH PERFORMANCE:");
        Random rand = new Random(42);
        
        // Best case (middle element)
        int midId = index.getAllProducts()[n/2].getProductId();
        testIdSearch(index, midId, "Best Case (middle)");
        
        // Average case (random IDs that exist)
        double avgTime = 0;
        int numTests = 100;
        for (int i = 0; i < numTests; i++) {
            int randomId = index.getAllProducts()[rand.nextInt(n)].getProductId();
            long start = System.nanoTime();
            index.searchById(randomId);
            long end = System.nanoTime();
            avgTime += (end - start);
        }
        System.out.printf("  %-30s: %8.3f ms (avg of %d searches)%n", 
            "Average Case (random)", avgTime / (numTests * 1_000_000.0), numTests);
        
        // Worst case (non-existent)
        testIdSearch(index, 999999, "Worst Case (not found)");
        
        // Test name searches
        System.out.println("\nNAME SEARCH PERFORMANCE (using index):");
        
        // Get some product names
        String[] testNames = {"Laptop", "Smartphone", "Keyboard", "XYZNonExistent"};
        
        for (String name : testNames) {
            long start = System.nanoTime();
            List<Product> results = index.searchByName(name);
            long end = System.nanoTime();
            System.out.printf("  %-30s: %8.3f ms (Found %d matches)%n", 
                "Search for '" + name + "'", (end - start) / 1_000_000.0, results.size());
        }
        
        // Test insert performance
        System.out.println("\nINSERT PERFORMANCE:");
        
        // Generate new products with unique IDs
        Set<Integer> existingIds = new HashSet<>();
        for (Product p : index.getAllProducts()) {
            existingIds.add(p.getProductId());
        }
        
        int newId = 200001; // Start above existing range
        avgTime = 0;
        numTests = Math.min(100, n / 10); // Don't add too many
        
        for (int i = 0; i < numTests; i++) {
            // Find unused ID
            while (existingIds.contains(newId)) {
                newId++;
            }
            existingIds.add(newId);
            
            Product newProduct = new Product(newId, 
                "NewProduct " + i, "Test", 99.99, 10);
            
            long start = System.nanoTime();
            index.addProduct(newProduct);
            long end = System.nanoTime();
            avgTime += (end - start);
        }
        
        System.out.printf("  %-30s: %8.3f ms (avg of %d inserts)%n", 
            "Average insert time", avgTime / (numTests * 1_000_000.0), numTests);
        
        // Final size
        System.out.printf("  %-30s: %d products%n", "Final index size", index.size());
    }
    
    private static void testIdSearch(HybridProductIndex index, int targetId, String caseType) {
        long start = System.nanoTime();
        Product result = index.searchById(targetId);
        long end = System.nanoTime();
        
        double timeMs = (end - start) / 1_000_000.0;
        System.out.printf("  %-30s: %8.3f ms %s%n", 
            caseType, timeMs, (result != null ? "(Found)" : "(Not Found)"));
    }
}