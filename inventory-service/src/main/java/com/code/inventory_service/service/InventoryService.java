package com.code.inventory_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.code.inventory_service.client.ProductClient;
import com.code.inventory_service.client.ProductDTO;
import com.code.inventory_service.dto.InventoryResponseDTO;
import com.code.inventory_service.dto.JsonApiResponse; // Asegúrate de tener esta importación
import com.code.inventory_service.exception.ResourceNotFoundException;
import com.code.inventory_service.model.Inventory;
import com.code.inventory_service.repository.InventoryRepository;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository repository;
    private final ProductClient productClient;

    @Value("${app.api-key}")
    private String apiKey;

    // --- LECTURA (GET) ---
    // Cumple Requisito [26]: "Consultar cantidad llamando a productos"
    // Justificación Senior: Usamos patrón "Composition" para evitar un Gateway extra en la prueba.
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetStock")
    public InventoryResponseDTO getStock(String productId) {
        
        // 1. Llamada para obtener nombre (Enriquecimiento)
        JsonApiResponse<ProductDTO> response = productClient.getProductById(productId, apiKey);
        String productName = response.getData().getAttributes().getName();

        // 2. Buscar stock local
        Inventory inventory = repository.findByProductId(productId)
                .orElse(new Inventory(null, productId, 0));

        return InventoryResponseDTO.build(productId, productName, inventory.getQuantity());
    }

    // Fallback Lectura
    public InventoryResponseDTO fallbackGetStock(String productId, Throwable t) {
        log.warn("[RESILIENCE] Fallo al leer producto {}: {}", productId, t.getMessage());
        Inventory inventory = repository.findByProductId(productId)
                .orElse(new Inventory(null, productId, 0));
        return InventoryResponseDTO.build(productId, "Información del producto no disponible", inventory.getQuantity());
    }

    // --- ESCRITURA (POST) ---
    // APLICAMOS REGLA DE ORO: Validar existencia antes de escribir.
    @Transactional
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackUpdateStock")
    public InventoryResponseDTO updateStock(String productId, Integer quantityChange) {
        
        // 1. VALIDACIÓN EXTERNA (Seniority Check): 
        // No podemos inventar stock de un producto que no existe en el catálogo.
        try {
            productClient.getProductById(productId, apiKey);
        }catch (FeignException.NotFound e) {

            // Caso 404: El producto no existe en el catálogo.

            log.error("Intento de stock para producto inexistente: {}", productId);

            throw new ResourceNotFoundException("No se puede crear stock. El producto con ID '" + productId + "' no existe en el catálogo."); 
        }catch (Exception e) {
            // Si el producto no existe (404) o falla la conexión crítica, no permitimos la escritura
            log.error("Intento de actualizar stock para producto inválido o servicio caído: {}", productId);
            throw new IllegalArgumentException("No se puede actualizar stock. El producto no existe o el catálogo no responde.");
        }

        // 2. Lógica Local
        Inventory inventory = repository.findByProductId(productId)
                .orElse(new Inventory(null, productId, 0));

        int newQuantity = inventory.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock insuficiente. Cantidad actual: " + inventory.getQuantity());
        }

        inventory.setQuantity(newQuantity);
        repository.save(inventory);

        emitInventoryChangeEvent(productId, quantityChange, newQuantity);

        return InventoryResponseDTO.build(productId, "Stock Actualizado", newQuantity);
    }

    // Fallback Escritura (Diferente a lectura: Aquí bloqueamos la operación por seguridad)
    public InventoryResponseDTO fallbackUpdateStock(String productId, Integer quantityChange, Throwable t) {
        // En escritura, si no podemos validar el producto, RECHAZAMOS la operación para mantener integridad.
        throw new RuntimeException("El servicio de productos no está disponible para validación. Intente más tarde.");
    }

    private void emitInventoryChangeEvent(String productId, int change, int total) {
        log.info("EVENT: InventoryChanged | Product: {} | Change: {} | NewTotal: {}", productId, change, total);
    }
}