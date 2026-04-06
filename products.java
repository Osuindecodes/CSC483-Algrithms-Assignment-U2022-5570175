package com.csc483.assignment1.search;

import java.util.Objects;

/**
 * Represents a product in the TechMart inventory system.
 * Implements Comparable to enable sorting by product ID.
 */
public class Product implements Comparable<Product> {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;
    
    /**
     * Constructs a new Product with specified attributes.
     * 
     * @param productId Unique identifier for the product
     * @param productName Name of the product
     * @param category Product category
     * @param price Product price in dollars
     * @param stockQuantity Available quantity in stock
     */
    public Product(int productId, String productName, String category, 
                   double price, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    
    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    /**
     * Compares products by productId for sorting.
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }
    
    /**
     * Checks equality based on productId.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId == product.productId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
    
    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s', price=%.2f, stock=%d}",
                productId, productName, category, price, stockQuantity);
    }
}