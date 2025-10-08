package com.example;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Category {

    // Cache storing unique Category instances by normalized name (Flyweight pattern)
    private static final Map<String, Category> CACHE = new ConcurrentHashMap<>();
    // The normalized name of this category (first letter capitalized)
    private final String name;

    // Private constructor to enforce use of the factory method
    private Category(String name) {
        this.name = name;
    }

    // Factory method to obtain a Category instance by name; validates input, normalizes, and caches
    public static Category of(String name) {
        // Validate input is not null
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        // Remove leading and trailing whitespace
        String trimmedName = name.trim();
        // Validate input is not empty after trimming
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Category name can't be blank");
        }

        // Normalize the category name: first letter uppercase, rest lowercase
        String normalizedName = trimmedName.substring(0, 1).toUpperCase() + trimmedName.substring(1).toLowerCase();

        // If not cached yet, create new instance and store it
        if (!CACHE.containsKey(normalizedName)) {
            CACHE.put(normalizedName, new Category(normalizedName));
        }
        // Return cached instance for the normalized name
        return CACHE.get(normalizedName);
    }

    // Returns the normalized category name, providing read-only access to the category value
    public String getName() {
        return name;
    }

    // Checks if this Category is equal to another object based on the normalized name
    // Ensures categories with the same name are considered identical
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    // Returns a hash code consistent with equals()
    // Ensures correct behavior in hash-based collections like maps
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    // Returns a read-only representation of the Category
    // Useful for debugging, logging, and displaying category information
    @Override
    public String toString() {
        return "Category{" + "name='" + name + '\'' + '}';
    }
}