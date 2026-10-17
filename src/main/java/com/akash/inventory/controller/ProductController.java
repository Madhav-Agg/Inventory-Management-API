package com.akash.inventory.controller;

import com.akash.inventory.model.Product;
import com.akash.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(p));
    }

    @GetMapping
    public List<Product> all(@RequestParam(required = false) String category,
                             @RequestParam(required = false) String search) {
        if (category != null && !category.isBlank()) {
            return service.byCategory(category);
        }
        if (search != null && !search.isBlank()) {
            return service.search(search);
        }
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product p) {
        return service.update(id, p);
    }

    /** Add stock with a positive number, remove stock with a negative number. */
    @PatchMapping("/{id}/stock")
    public Product stock(@PathVariable Long id, @RequestParam int change) {
        return service.adjustStock(id, change);
    }

    @GetMapping("/low-stock")
    public List<Product> lowStock(@RequestParam(defaultValue = "10") int threshold) {
        return service.lowStock(threshold);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
