package com.code.product_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.code.product_service.dto.CreateProductRequest;
import com.code.product_service.dto.JsonApiResponse;
import com.code.product_service.dto.ProductResponseDTO;
import com.code.product_service.model.Product;
import com.code.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<JsonApiResponse<ProductResponseDTO>> create(@RequestBody CreateProductRequest request) {
        Product created = service.createProduct(request);

        // Convertimos Entidad -> DTO antes de responder
        ProductResponseDTO responseDto = ProductResponseDTO.fromEntity(created);

        return ResponseEntity.ok(JsonApiResponse.success("products", responseDto.getId(), responseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductResponseDTO>> getById(@PathVariable String id) {
        // Si no existe, el Service lanza la excepción y el GlobalExceptionHandler
        // responde.
        Product product = service.getProductById(id);
        ProductResponseDTO dto = ProductResponseDTO.fromEntity(product);
        return ResponseEntity.ok(JsonApiResponse.success("products", dto.getId(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductResponseDTO>> update(
            @PathVariable String id,
            @RequestBody CreateProductRequest request) {

        return service.updateProduct(id, request)
                .map(p -> {
                    ProductResponseDTO dto = ProductResponseDTO.fromEntity(p);
                    return ResponseEntity.ok(JsonApiResponse.success("products", dto.getId(), dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<JsonApiResponse<List<ProductResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Crear objeto Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtener página de la DB
        Page<Product> productPage = service.getAllProducts(pageable);

        // Convertir a DTOs
        List<ProductResponseDTO> dtoList = productPage.stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());

        // NOTA SENIOR: En JSON API real, deberíamos incluir "meta" con totalPages, etc.
        // Pero por tiempo, devolver la lista paginada dentro de 'data' es aceptable,
        // o podemos envolverlo mejor. Mantengamos la estructura simple por ahora.
        return ResponseEntity.ok(JsonApiResponse.success("products", "page_" + page, dtoList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}