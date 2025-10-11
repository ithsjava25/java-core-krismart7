package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Category {

    // Flyweight-cache: normaliserat namn → unik Category-instans
    private static final Map<String, Category> CACHE = new HashMap<>();
    private final String name;

    // Endast instanser via of() (flyweight/factory-mönster)
    private Category(String name) {
        this.name = name;
    }

    // Skapar/återanvänder Category-instans (factory)
    public static Category of(String name) {
        if (name == null) { throw new IllegalArgumentException("Category name can't be null"); }

        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) { throw new IllegalArgumentException("Category name can't be blank"); }

        String capitalizedName = trimmedName.substring(0, 1).toUpperCase()
                                + trimmedName.substring(1).toLowerCase();

        if (!CACHE.containsKey(capitalizedName)) {
            CACHE.put(capitalizedName, new Category(capitalizedName));
        }
        return CACHE.get(capitalizedName);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Category{" + "name='" + name + '\'' + '}';
    }
}