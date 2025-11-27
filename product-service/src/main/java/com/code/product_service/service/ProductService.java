package com.code.product_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.code.product_service.dto.CreateProductRequest;
import com.code.product_service.exception.ResourceNotFoundException;
import com.code.product_service.model.Product;
import com.code.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    // Crear usando DTO
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        // El ID se genera automáticamente por JPA
        return repository.save(product);
    }

    public Product getProductById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // ACTUALIZAR (Requisito Senior: Transaccionalidad)
    @Transactional
    public Optional<Product> updateProduct(String id, CreateProductRequest request) {
        return repository.findById(id).map(existingProduct -> {
            existingProduct.setName(request.getName());
            existingProduct.setDescription(request.getDescription());
            existingProduct.setPrice(request.getPrice());
            return repository.save(existingProduct);
        });
    }

    // ELIMINAR
    public boolean deleteProduct(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
