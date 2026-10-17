package com.akash.inventory.service;

import com.akash.inventory.exception.DuplicateResourceException;
import com.akash.inventory.exception.InsufficientStockException;
import com.akash.inventory.exception.ResourceNotFoundException;
import com.akash.inventory.model.Product;
import com.akash.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    private Product sample() {
        Product p = new Product();
        p.setId(1L);
        p.setName("Keyboard");
        p.setSku("KB-001");
        p.setQuantity(10);
        p.setPrice(new BigDecimal("799.00"));
        p.setCategory("Electronics");
        return p;
    }

    @Test
    void getById_returnsProduct() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample()));
        assertEquals("KB-001", service.getById(1L).getSku());
    }

    @Test
    void getById_throwsWhenMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void create_rejectsDuplicateSku() {
        when(repo.existsBySku("KB-001")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.create(sample()));
        verify(repo, never()).save(any());
    }

    @Test
    void adjustStock_addsQuantity() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample()));
        when(repo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        assertEquals(15, service.adjustStock(1L, 5).getQuantity());
    }

    @Test
    void adjustStock_throwsWhenNotEnough() {
        when(repo.findById(1L)).thenReturn(Optional.of(sample()));
        assertThrows(InsufficientStockException.class, () -> service.adjustStock(1L, -50));
        verify(repo, never()).save(any());
    }
}
