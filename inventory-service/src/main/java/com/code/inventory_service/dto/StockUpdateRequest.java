package com.code.inventory_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockUpdateRequest {
    // Solo necesitamos la cantidad a agregar (positivo) o restar (negativo)
    private Integer quantity;
}