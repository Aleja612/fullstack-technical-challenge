package com.code.product_service.service;

import com.code.product_service.dto.CreateProductRequest;
import com.code.product_service.exception.ResourceNotFoundException;
import com.code.product_service.model.Product;
import com.code.product_service.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    @DisplayName("Create Product - Success")
    void createProduct_Success() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest("Laptop", "Gaming Laptop", 1500.00);
        Product savedProduct = new Product("uuid-123", "Laptop", "Gaming Laptop", 1500.00);
        
        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = service.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("uuid-123", result.getId());
        assertEquals("Laptop", result.getName());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Get Product By ID - Success")
    void getProductById_Success() {
        // Arrange
        String id = "uuid-123";
        Product product = new Product(id, "Phone", "Smart Phone", 800.00);
        when(repository.findById(id)).thenReturn(Optional.of(product));

        // Act
        Product result = service.getProductById(id);

        // Assert
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Get Product By ID - Not Found Throws Exception")
    void getProductById_NotFound() {
        // Arrange
        String id = "uuid-not-found";
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.getProductById(id));
    }

    @Test
    @DisplayName("Update Product - Success")
    void updateProduct_Success() {
        // Arrange
        String id = "uuid-123";
        CreateProductRequest updateRequest = new CreateProductRequest("Laptop Pro", "Better Laptop", 2000.00);
        Product existingProduct = new Product(id, "Laptop", "Old Desc", 1000.00);
        
        when(repository.findById(id)).thenReturn(Optional.of(existingProduct));
        // Simulamos que al guardar devuelve el objeto ya modificado
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Product> result = service.updateProduct(id, updateRequest);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Laptop Pro", result.get().getName());
        assertEquals(2000.00, result.get().getPrice());
    }

    @Test
    @DisplayName("Delete Product - Success")
    void deleteProduct_Success() {
        // Arrange
        String id = "uuid-123";
        when(repository.existsById(id)).thenReturn(true);

        // Act
        boolean deleted = service.deleteProduct(id);

        // Assert
        assertTrue(deleted);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Get All Products - Paginado")
    void getAllProducts_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Product p1 = new Product("1", "A", "Desc", 10.0);
        Page<Product> page = new PageImpl<>(Collections.singletonList(p1));
        
        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Product> result = service.getAllProducts(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }
}