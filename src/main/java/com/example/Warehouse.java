package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    // Singleton per namn: namn-nyckel → unik Warehouse-instans
    private static final Map<String, Warehouse> unique = new HashMap<>();
    private final String name;

    // Produkter per UUID
    private final Map<UUID, Product> productsById = new HashMap<>();

    // Spårar ändrade produkter - Set för unika ID:n
    private final Set<UUID> changedProducts = new HashSet<>();

    private final List<Product> productList = new ArrayList<>();

    // Kontrollerad instansiering via getInstance
    private Warehouse(String name) {
        this.name = name;
    }

    // Returnerar samma instans för samma namn, skapar ny om saknas
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
        return Collections.unmodifiableList(productList);
    }

    public Set<UUID> getChangedProducts() {
        return Collections.unmodifiableSet(changedProducts);
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
        Product product = productsById.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setPrice(newPrice);
        changedProducts.add(id);
    }

    public List<Perishable> expiredProducts() {
        return productList.stream()
                .filter(p -> p instanceof Perishable)
                .map(p -> (Perishable) p)
                .filter(Perishable::isExpired)
                .toList();
    }

    public List<Shippable> shippableProducts() {
        return productList.stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .toList();
    }

    public void remove(UUID id) {
        productsById.remove(id);
        changedProducts.remove(id);
        productList.removeIf(p -> p.uuid().equals(id));
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
                .collect(Collectors.groupingBy(Product::category));
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