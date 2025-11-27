package com.code.inventory_service.client;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDTO {
    private String name;
    // No necesitamos price ni description para el inventario, así ahorramos memoria.
}
