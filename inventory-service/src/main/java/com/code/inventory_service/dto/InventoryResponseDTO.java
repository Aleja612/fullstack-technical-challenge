package com.code.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class InventoryResponseDTO {
    private String productId;
    private String productName; // Viene del MS Productos
    private Integer quantity;   // Viene de BD Local DB
    private String status;      // Info extra sobre la disponibilidad

    public static InventoryResponseDTO build(String pId, String pName, Integer qty) {
        String status = qty > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
        return new InventoryResponseDTO(pId, pName, qty, status);
    }
}
