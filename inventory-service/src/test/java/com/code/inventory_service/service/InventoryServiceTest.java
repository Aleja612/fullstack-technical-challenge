package com.code.inventory_service.service;

import com.code.inventory_service.client.ProductClient;
import com.code.inventory_service.client.ProductDTO;
import com.code.inventory_service.dto.InventoryResponseDTO;
import com.code.inventory_service.dto.JsonApiResponse;
import com.code.inventory_service.exception.ResourceNotFoundException;
import com.code.inventory_service.model.Inventory;
import com.code.inventory_service.repository.InventoryRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock private InventoryRepository repository;
    @Mock private ProductClient productClient;

    @InjectMocks private InventoryService service;

    @BeforeEach
    void setUp() {
        // Inyectamos la API Key privada usando Reflection porque es @Value
        ReflectionTestUtils.setField(service, "apiKey", "test-secret-key");
    }

    // --- TEST GET STOCK (Lectura) ---

    @Test
    @DisplayName("Get Stock - Success (Enriched)")
    void getStock_Success() {
        String prodId = "p-1";
        // Mock DB Local
        Inventory mockInv = new Inventory(1L, prodId, 50);
        when(repository.findByProductId(prodId)).thenReturn(Optional.of(mockInv));

        // Mock API Remota (Feign)
        ProductDTO pDto = new ProductDTO(); pDto.setName("Super TV");
        JsonApiResponse<ProductDTO> apiResp = JsonApiResponse.success("products", prodId, pDto);
        when(productClient.getProductById(eq(prodId), anyString())).thenReturn(apiResp);

        // Act
        InventoryResponseDTO result = service.getStock(prodId);

        // Assert
        assertEquals(50, result.getQuantity());
        assertEquals("Super TV", result.getProductName()); // Validamos enriquecimiento
    }

    @Test
    @DisplayName("Get Stock - Fallback (Cuando falla Product Service)")
    void getStock_Fallback() {
        String prodId = "p-1";
        Inventory mockInv = new Inventory(1L, prodId, 50);
        when(repository.findByProductId(prodId)).thenReturn(Optional.of(mockInv));

        // Simulamos fallo
        RuntimeException error = new RuntimeException("Service Down");
        
        // Act (Llamamos directo al fallback para probar su lógica aislada)
        InventoryResponseDTO result = service.fallbackGetStock(prodId, error);

        // Assert
        assertEquals(50, result.getQuantity());
        assertTrue(result.getProductName().contains("no disponible")); // Validamos degradación
    }

    // --- TEST UPDATE STOCK (Escritura) ---

    @Test
    @DisplayName("Update Stock - Success (Product Exists)")
    void updateStock_Success() {
        String prodId = "p-1";
        int qtyToAdd = 10;
        
        // 1. Mock Validación Exitosa
        when(productClient.getProductById(eq(prodId), anyString())).thenReturn(new JsonApiResponse<>());
        
        // 2. Mock Inventario existente
        Inventory existing = new Inventory(1L, prodId, 100);
        when(repository.findByProductId(prodId)).thenReturn(Optional.of(existing));

        // Act
        InventoryResponseDTO result = service.updateStock(prodId, qtyToAdd);

        // Assert
        assertEquals(110, result.getQuantity());
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("Update Stock - Creates New Inventory if Empty")
    void updateStock_CreatesNew() {
        String prodId = "p-new";
        
        // 1. Mock Validación Exitosa
        when(productClient.getProductById(anyString(), anyString())).thenReturn(new JsonApiResponse<>());
        
        // 2. Mock Inventario Vacío
        when(repository.findByProductId(prodId)).thenReturn(Optional.empty());

        // Act
        service.updateStock(prodId, 5);

        // Assert
        verify(repository).save(argThat(inv -> 
            inv.getProductId().equals(prodId) && inv.getQuantity() == 5
        ));
    }

    @Test
    @DisplayName("Update Stock - Throws 404 when Product Remote Not Found")
    void updateStock_ProductNotFound() {
        String prodId = "ghost";
        
        // Mock Feign 404 Exception
        Request req = Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
        FeignException notFound = new FeignException.NotFound("Not Found", req, null, null);
        
        when(productClient.getProductById(eq(prodId), anyString())).thenThrow(notFound);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.updateStock(prodId, 10));
        
        // Aseguramos que NUNCA tocó la base de datos local
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Update Stock - Throws Exception on Insufficient Stock")
    void updateStock_Insufficient() {
        String prodId = "p-1";
        
        when(productClient.getProductById(anyString(), anyString())).thenReturn(new JsonApiResponse<>());
        
        Inventory existing = new Inventory(1L, prodId, 5); // Solo hay 5
        when(repository.findByProductId(prodId)).thenReturn(Optional.of(existing));

        // Act & Assert (Intentamos restar 10)
        assertThrows(IllegalArgumentException.class, () -> service.updateStock(prodId, -10));
    }
}