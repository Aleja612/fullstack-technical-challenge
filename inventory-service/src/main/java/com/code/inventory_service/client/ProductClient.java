package com.code.inventory_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.code.inventory_service.dto.JsonApiResponse;

@FeignClient(name = "product-service", url = "${PRODUCTS_SERVICE_URL}")
public interface ProductClient {

    // Solo definimos la firma del método. Spring hace el resto.
    @GetMapping("/api/v1/products/{id}")
    JsonApiResponse<ProductDTO> getProductById(
            @PathVariable("id") String id,
            @RequestHeader("X-API-KEY") String apiKey);
}
