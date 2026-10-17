package com.akash.inventory.service;

import com.akash.inventory.exception.DuplicateResourceException;
import com.akash.inventory.exception.InsufficientStockException;
import com.akash.inventory.exception.ResourceNotFoundException;
import com.akash.inventory.model.Product;
import com.akash.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repo;

    public Product create(Product p) {
        if (repo.existsBySku(p.getSku())) {
            throw new DuplicateResourceException("SKU already exists: " + p.getSku());
        }
        p.setId(null);
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public Product update(Long id, Product in) {
        Product p = getById(id);
        if (!p.getSku().equals(in.getSku()) && repo.existsBySku(in.getSku())) {
            throw new DuplicateResourceException("SKU already exists: " + in.getSku());
        }
        p.setName(in.getName());
        p.setSku(in.getSku());
        p.setQuantity(in.getQuantity());
        p.setPrice(in.getPrice());
        p.setCategory(in.getCategory());
        return repo.save(p);
    }

    public Product adjustStock(Long id, int change) {
        Product p = getById(id);
        int newQty = p.getQuantity() + change;
        if (newQty < 0) {
            throw new InsufficientStockException(
                    "Not enough stock. Available: " + p.getQuantity() + ", requested change: " + change);
        }
        p.setQuantity(newQty);
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.delete(getById(id));
    }

    @Transactional(readOnly = true)
    public List<Product> lowStock(int threshold) {
        return repo.findByQuantityLessThan(threshold);
    }

    @Transactional(readOnly = true)
    public List<Product> byCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    @Transactional(readOnly = true)
    public List<Product> search(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
}
