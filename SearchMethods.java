package com.csc483.assignment1.search;

import java.util.*;

/**
 * Implements various search algorithms for product lookup.
 */
public class SearchMethods {
    
    /**
     * Performs sequential search to find a product by ID.
     * Time Complexity: O(n)
     * 
     * @param products Array of products (can be unsorted)
     * @param targetId The product ID to search for
     * @return The found Product or null if not found
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        if (products == null || products.length == 0) {
            return null;
        }
        
        for (Product product : products) {
            if (product != null && product.getProductId() == targetId) {
                return product;
            }
        }
        return null;
    }
    
    /**
     * Performs binary search to find a product by ID.
     * PRECONDITION: Array must be sorted by productId
     * Time Complexity: O(log n)
     * 
     * @param products Array of products sorted by ID
     * @param targetId The product ID to search for
     * @return The found Product or null if not found
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        if (products == null || products.length == 0) {
            return null;
        }
        
        int left = 0;
        int right = products.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow
            
            if (products[mid] == null) {
                // Skip null elements (shouldn't happen in valid dataset)
                left = mid + 1;
                continue;
            }
            
            int currentId = products[mid].getProductId();
            
            if (currentId == targetId) {
                return products[mid];
            }
            
            if (currentId < targetId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return null;
    }
    
    /**
     * Performs sequential search to find products by name (case-insensitive).
     * Returns all products containing the target name as a substring.
     * Time Complexity: O(n)
     * 
     * @param products Array of products
     * @param targetName Name or partial name to search for
     * @return List of matching products
     */
    public static List<Product> searchByName(Product[] products, String targetName) {
        List<Product> results = new ArrayList<>();
        
        if (products == null || products.length == 0 || targetName == null) {
            return results;
        }
        
        String searchTerm = targetName.toLowerCase().trim();
        
        for (Product product : products) {
            if (product != null && product.getProductName() != null &&
                product.getProductName().toLowerCase().contains(searchTerm)) {
                results.add(product);
            }
        }
        
        return results;
    }
    
    /**
     * Sorts products array by ID using Arrays.sort (for binary search preparation).
     */
    public static void sortProductsById(Product[] products) {
        Arrays.sort(products);
    }
}