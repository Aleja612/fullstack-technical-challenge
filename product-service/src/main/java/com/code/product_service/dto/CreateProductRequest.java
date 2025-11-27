package com.code.product_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    // No tiene ID, porque el ID se genera en el backend
    private String name;
    private String description;
    private Double price;
}
