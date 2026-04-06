package com.csc483.assignment1.search;

import java.util.*;

/**
 * Generates random product data for testing and benchmarking.
 */
public class DataGenerator {
    private static final Random random = new Random(42); // Fixed seed for reproducibility
    
    private static final String[] PRODUCT_NAMES = {
        "Laptop", "Smartphone", "Tablet", "Monitor", "Keyboard", "Mouse", 
        "Headphones", "Webcam", "Printer", "Scanner", "Router", "Modem",
        "External Drive", "USB Cable", "HDMI Cable", "Power Bank", 
        "Smart Watch", "Speaker", "Microphone", "Graphics Card"
    };
    
    private static final String[] CATEGORIES = {
        "Computers", "Mobile Devices", "Accessories", "Networking", 
        "Audio", "Peripherals", "Storage", "Components"
    };
    
    /**
     * Generates an array of random products with unique IDs.
     * 
     * @param size Number of products to generate
     * @return Array of random products
     */
    public static Product[] generateRandomProducts(int size) {
        Set<Integer> usedIds = new HashSet<>();
        Product[] products = new Product[size];
        
        for (int i = 0; i < size; i++) {
            int id;
            do {
                id = random.nextInt(200000) + 1; // IDs from 1 to 200,000
            } while (usedIds.contains(id));
            usedIds.add(id);
            
            String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + " " + (random.nextInt(100) + 1);
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            double price = 10.0 + random.nextDouble() * 1990.0; // $10 to $2000
            int stock = random.nextInt(500) + 1;
            
            products[i] = new Product(id, name, category, price, stock);
        }
        
        return products;
    }
    
    /**
     * Creates a copy of the products array (for testing with different search types).
     */
    public static Product[] copyProducts(Product[] original) {
        Product[] copy = new Product[original.length];
        for (int i = 0; i < original.length; i++) {
            // Create new Product with same attributes
            Product p = original[i];
            copy[i] = new Product(p.getProductId(), p.getProductName(), 
                                  p.getCategory(), p.getPrice(), p.getStockQuantity());
        }
        return copy;
    }
}