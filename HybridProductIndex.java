package com.csc483.assignment1.search;

import java.util.*;

/**
 * Implements a hybrid search structure with indexing for both ID and name searches.
 * Maintains sorted array for binary search by ID and HashMap for fast name lookups.
 */
public class HybridProductIndex {
    private Product[] products;           // Sorted array for binary search
    private Map<String, List<Product>> nameIndex;  // Index for name searches
    private int size;                      // Current number of products
    private static final int INITIAL_CAPACITY = 100;
    
    /**
     * Creates an empty hybrid index.
     */
    public HybridProductIndex() {
        this.products = new Product[INITIAL_CAPACITY];
        this.nameIndex = new HashMap<>();
        this.size = 0;
    }
    
    /**
     * Creates a hybrid index from an existing array of products.
     * Time Complexity: O(n log n) for sorting + O(n) for indexing
     * 
     * @param initialProducts Array of products to initialize with
     */
    public HybridProductIndex(Product[] initialProducts) {
        this.products = Arrays.copyOf(initialProducts, initialProducts.length);
        this.size = initialProducts.length;
        this.nameIndex = new HashMap<>();
        
        // Sort array for binary search
        Arrays.sort(this.products, 0, size);
        
        // Build name index
        rebuildNameIndex();
    }
    
    /**
     * Rebuilds the entire name index from the products array.
     * Time Complexity: O(n)
     */
    private void rebuildNameIndex() {
        nameIndex.clear();
        for (int i = 0; i < size; i++) {
            Product p = products[i];
            if (p != null) {
                // Index by full name (lowercase for case-insensitive search)
                String key = p.getProductName().toLowerCase();
                nameIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
                
                // Also index by individual words for partial matching
                String[] words = p.getProductName().toLowerCase().split("\\s+");
                for (String word : words) {
                    if (word.length() > 2) { // Skip very short words
                        nameIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(p);
                    }
                }
            }
        }
    }
    
    /**
     * Searches for a product by ID using binary search.
     * Time Complexity: O(log n)
     * 
     * @param targetId The product ID to find
     * @return The product or null if not found
     */
    public Product searchById(int targetId) {
        int left = 0;
        int right = size - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = products[mid].getProductId();
            
            if (midId == targetId) {
                return products[mid];
            }
            if (midId < targetId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    /**
     * Searches for products by name using the index.
     * Time Complexity: O(1) average case for exact match
     * 
     * @param targetName Name or partial name to search for
     * @return List of matching products
     */
    public List<Product> searchByName(String targetName) {
        if (targetName == null || targetName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String key = targetName.toLowerCase().trim();
        List<Product> exactMatches = nameIndex.getOrDefault(key, Collections.emptyList());
        
        // If we have exact matches, return them
        if (!exactMatches.isEmpty()) {
            return new ArrayList<>(exactMatches);
        }
        
        // Otherwise, do sequential search through all products
        // (This is the trade-off for not indexing every substring)
        List<Product> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Product p = products[i];
            if (p.getProductName().toLowerCase().contains(key)) {
                results.add(p);
            }
        }
        return results;
    }
    
    /**
     * Adds a new product while maintaining sorted order.
     * Time Complexity: O(n) for array insertion + O(1) for index update
     * 
     * @param newProduct The product to add
     * @return true if added successfully, false if duplicate ID
     */
    public boolean addProduct(Product newProduct) {
        // Check for duplicate ID using binary search
        if (searchById(newProduct.getProductId()) != null) {
            return false; // Duplicate ID
        }
        
        // Ensure array capacity
        if (size == products.length) {
            products = Arrays.copyOf(products, products.length * 2);
        }
        
        // Find insertion position using binary search
        int insertPos = findInsertionPosition(newProduct.getProductId());
        
        // Shift elements to make room
        System.arraycopy(products, insertPos, products, insertPos + 1, size - insertPos);
        
        // Insert new product
        products[insertPos] = newProduct;
        size++;
        
        // Update name index
        String key = newProduct.getProductName().toLowerCase();
        nameIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(newProduct);
        
        // Index individual words
        String[] words = newProduct.getProductName().toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 2) {
                nameIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(newProduct);
            }
        }
        
        return true;
    }
    
    /**
     * Finds the correct insertion position for a new ID using binary search.
     */
    private int findInsertionPosition(int newId) {
        int left = 0;
        int right = size - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = products[mid].getProductId();
            
            if (midId < newId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * Removes a product by ID.
     * Time Complexity: O(n) for array shifting + O(1) for index update
     * 
     * @param productId ID of product to remove
     * @return true if removed, false if not found
     */
    public boolean removeProduct(int productId) {
        int pos = findProductPosition(productId);
        if (pos == -1) {
            return false;
        }
        
        Product removed = products[pos];
        
        // Shift elements left
        System.arraycopy(products, pos + 1, products, pos, size - pos - 1);
        products[size - 1] = null;
        size--;
        
        // Update name index (expensive - would need more sophisticated indexing)
        // For simplicity, we rebuild the index (optimization possible)
        rebuildNameIndex();
        
        return true;
    }
    
    /**
     * Finds the position of a product by ID using binary search.
     */
    private int findProductPosition(int productId) {
        int left = 0;
        int right = size - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = products[mid].getProductId();
            
            if (midId == productId) {
                return mid;
            }
            if (midId < productId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    /**
     * Gets all products (sorted by ID).
     */
    public Product[] getAllProducts() {
        return Arrays.copyOf(products, size);
    }
    
    /**
     * Gets the current size.
     */
    public int size() {
        return size;
    }
}