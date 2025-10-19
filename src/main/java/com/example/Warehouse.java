package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> unique = new HashMap<>();
    private final Map<UUID, Product> productsById = new HashMap<>();
    private final List<Product> productList = new ArrayList<>();
    private final Set<UUID> changedProducts = new HashSet<>();
    private final String name;


    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance(String name) {
        return unique.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance() {
        return getInstance("default");
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return List.copyOf(productList);
    }

    public Set<UUID> getChangedProducts() {
        return Set.copyOf(changedProducts);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (productsById.containsKey(product.uuid())) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        productsById.put(product.uuid(), product);
        productList.add(product);
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
        return productList.stream()
                .filter(p -> p instanceof Perishable perishable && perishable.isExpired())
                .map(p -> (Perishable) p)
                .toList();
    }

    public List<Shippable> shippableProducts() {
        return productList.stream()
                .filter(p -> p instanceof Shippable s)
                .map(s -> (Shippable) s)
                .toList();
    }

    public void remove(UUID id) {
        Optional.ofNullable(productsById.remove(id))
                .ifPresent(p -> {
                    changedProducts.remove(id);
                    productList.remove(p);
                });
    }

    public void clearProducts() {
        productsById.clear();
        changedProducts.clear();
        productList.clear();
    }

    public boolean isEmpty() {
        return productList.isEmpty();
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return productList.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.collectingAndThen(Collectors.toList(), List::copyOf)
                ));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(name, warehouse.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Warehouse{" + "name='" + name + '\'' + ", productsById=" + productsById + ", changedProducts=" + changedProducts + '}';
    }
}