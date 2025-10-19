package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> WAREHOUSES_BY_NAME = new HashMap<>();
    private final Map<UUID, Product> PRODUCTS_BY_ID = new HashMap<>();
    private final List<Product> PRODUCT_LIST = new ArrayList<>();
    private final Set<UUID> CHANGED_PRODUCT_IDS = new HashSet<>();
    private final String name;


    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance(String name) {
        return WAREHOUSES_BY_NAME.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance() {
        return getInstance("default");
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return List.copyOf(PRODUCT_LIST);
    }

    public Set<UUID> getChangedProducts() {
        return Set.copyOf(CHANGED_PRODUCT_IDS);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (PRODUCTS_BY_ID.containsKey(product.uuid())) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        PRODUCTS_BY_ID.put(product.uuid(), product);
        PRODUCT_LIST.add(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(PRODUCTS_BY_ID.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Optional.ofNullable(PRODUCTS_BY_ID.get(id))
                .ifPresentOrElse(
                        p -> {
                            p.setPrice(newPrice);
                            CHANGED_PRODUCT_IDS.add(id); },
                        () -> {
                            throw new NoSuchElementException("Product not found with id: " + id);
                        }
                );
    }

    public List<Perishable> expiredProducts() {
        return PRODUCT_LIST.stream()
                .filter(p -> p instanceof Perishable perishable && perishable.isExpired())
                .map(p -> (Perishable) p)
                .toList();
    }

    public List<Shippable> shippableProducts() {
        return PRODUCT_LIST.stream()
                .filter(p -> p instanceof Shippable s)
                .map(s -> (Shippable) s)
                .toList();
    }

    public void remove(UUID id) {
        Optional.ofNullable(PRODUCTS_BY_ID.remove(id))
                .ifPresent(p -> {
                    CHANGED_PRODUCT_IDS.remove(id);
                    PRODUCT_LIST.remove(p);
                });
    }

    public void clearProducts() {
        PRODUCTS_BY_ID.clear();
        CHANGED_PRODUCT_IDS.clear();
        PRODUCT_LIST.clear();
    }

    public boolean isEmpty() {
        return PRODUCT_LIST.isEmpty();
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return PRODUCT_LIST.stream()
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
        return "Warehouse{" + "name='" + name + '\'' + ", productsById=" + PRODUCTS_BY_ID + ", changedProducts=" + CHANGED_PRODUCT_IDS + '}';
    }
}