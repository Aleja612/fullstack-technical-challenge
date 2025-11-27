package com.code.inventory_service.controller;

import com.code.inventory_service.dto.InventoryResponseDTO;
import com.code.inventory_service.dto.JsonApiResponse;
import com.code.inventory_service.dto.StockUpdateRequest;
import com.code.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{productId}")
    public ResponseEntity<JsonApiResponse<InventoryResponseDTO>> getStock(@PathVariable String productId) {
        InventoryResponseDTO response = service.getStock(productId);
        return ResponseEntity.ok(JsonApiResponse.success("inventory", productId, response));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<JsonApiResponse<InventoryResponseDTO>> updateStock(
            @PathVariable String productId,
            @RequestBody StockUpdateRequest request) {
        
        InventoryResponseDTO response = service.updateStock(productId, request.getQuantity());
        return ResponseEntity.ok(JsonApiResponse.success("inventory", productId, response));
    }
}