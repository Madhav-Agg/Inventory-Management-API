package com.akash.inventory.repository;

import com.akash.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByQuantityLessThan(int threshold);

    List<Product> findByNameContainingIgnoreCase(String name);

    boolean existsBySku(String sku);
}
