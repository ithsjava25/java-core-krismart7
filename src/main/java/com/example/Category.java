package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Category {

    private static final Map<String, Category> category = new HashMap<>();
    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        if (name == null) throw new IllegalArgumentException("Category name can't be null");
        if (name.isBlank()) { throw new IllegalArgumentException("Category name can't be blank"); }

        String trimmed = name.trim();
        String normalized = Character.toUpperCase(trimmed.charAt(0))
                + trimmed.substring(1).toLowerCase();

        return category.computeIfAbsent(normalized, Category::new);
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() { return Objects.hashCode(name); }

    @Override
    public String toString() { return "Category{" + "name='" + name + '\'' + '}'; }
}