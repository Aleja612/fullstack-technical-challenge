package com.code.product_service.dto;

import com.code.product_service.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String id;
    private String name;
    private String description;
    private Double price;

    // PATRÓN "STATIC FACTORY METHOD"
    // Esto centraliza la lógica de conversión Entidad -> DTO aquí,
    // limpiando tus controladores y servicios.
    public static ProductResponseDTO fromEntity(Product product) {
        if (product == null) return null;
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice()
        );
    }
}