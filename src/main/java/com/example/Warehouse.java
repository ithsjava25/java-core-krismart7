package com.example;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    // Singleton per namn: namn-nyckel → unik Warehouse-instans
    private static final Map<String, Warehouse> unique = new HashMap<>();
    private final String name;

    // Produkter per UUID
    private final Map<UUID, Product> productsById = new HashMap<>();

    // Spårar ändrade produkter - Set för unika ID:n
    private final Set<UUID> changedProducts = new HashSet<>();

    // Kontrollerad instansiering via getInstance
    private Warehouse(String name) {
        this.name = name;
    }

    // Returnerar samma instans för samma namn, skapar ny om saknas
    public static Warehouse getInstance(String name) {
        return unique.computeIfAbsent(name, Warehouse::new);
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(productsById.values()));
    }

    public Set<UUID> getChangedProducts() {
        return Collections.unmodifiableSet(changedProducts);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        productsById.put(product.uuid(), product);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productsById.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = productsById.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setPrice(newPrice);
        changedProducts.add(id);
    }

    public List<Perishable> expiredProducts() {
        List<Perishable> expiredProducts = new ArrayList<>();
        for (Product p : productsById.values()) {
            if (p instanceof Perishable perishable && perishable.isExpired()) {
                expiredProducts.add(perishable);
            }
        }
        return expiredProducts;
    }

    public List<Shippable> shippableProducts() {
        List<Shippable> shippableProducts = new ArrayList<>();
        for (Product p : productsById.values()) {
            if (p instanceof Shippable shippable) {
                shippableProducts.add(shippable);
            }
        }
        return shippableProducts;
    }

    public void remove(UUID id) {
        productsById.remove(id);
        changedProducts.remove(id);
    }

    public void clearProducts() {
        productsById.clear();
        changedProducts.clear();
    }

    public boolean isEmpty() {
        return productsById.isEmpty();
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        Map<Category, List<Product>> grouped = new HashMap<>();
        for (Product p : productsById.values()) {
            grouped.computeIfAbsent(p.category(), k -> new ArrayList<>()).add(p);
        }
        return grouped;
    }

    @Override
    public String toString() {
        return "Warehouse{" + "name='" + name + '\'' + ", productsById=" + productsById + ", changedProducts=" + changedProducts + '}';
    }
}