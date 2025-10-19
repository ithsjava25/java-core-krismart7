package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> WAREHOUSES_BY_NAME = new HashMap<>();
    private final Map<UUID, Product> productsById = new HashMap<>();
    private final Set<UUID> changedProducts = new HashSet<>();
    private final List<Product> products = new ArrayList<>();
    private final String name;

    private Warehouse(String name) { this.name = name; }

    public static Warehouse getInstance(String name) {
        return WAREHOUSES_BY_NAME.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance() { return getInstance("default"); }

    public String getName() { return name; }

    public List<Product> getProducts() { return List.copyOf(products); }

    public Set<UUID> getChangedProducts() { return Set.copyOf(changedProducts); }

    public void addProduct(Product product) {
        if (product == null) { throw new IllegalArgumentException("Product cannot be null."); }
        if (productsById.containsKey(product.uuid())) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        productsById.put(product.uuid(), product);
        products.add(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productsById.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Optional.ofNullable(productsById.get(id))
                .ifPresentOrElse(
                        p -> {
                            p.setPrice(newPrice);
                            changedProducts.add(id); },
                        () -> {
                            throw new NoSuchElementException("Product not found with id: " + id);
                        }
                );
    }

    public List<Perishable> expiredProducts() {
        return products.stream()
                .filter(p -> p instanceof Perishable perishable && perishable.isExpired())
                .map(p -> (Perishable) p)
                .toList();
    }

    public List<Shippable> shippableProducts() {
        return products.stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .toList();
    }

    public void remove(UUID id) {
        Optional.ofNullable(productsById.remove(id))
                .ifPresent(p -> {
                    changedProducts.remove(id);
                    products.remove(p);
                });
    }

    public void clearProducts() {
        productsById.clear();
        changedProducts.clear();
        products.clear();
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.collectingAndThen(Collectors.toList(), List::copyOf)
                ));
    }

    public boolean isEmpty() { return products.isEmpty(); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(name, warehouse.name);
    }

    @Override
    public int hashCode() { return Objects.hashCode(name); }

    @Override
    public String toString() {
        return "Warehouse{" + "name='" + name + '\'' + ", productsById=" + productsById + ", changedProducts=" + changedProducts + '}';
    }
}