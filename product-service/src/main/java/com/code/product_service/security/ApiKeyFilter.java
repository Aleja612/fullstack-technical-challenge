package com.code.product_service.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api-key}") // Lee del application.yml
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Permitir acceso libre a endpoints de documentación (Swagger) si los añades luego
        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validación de API Key
        String requestKey = request.getHeader("X-API-KEY");

        if (requestKey == null || !requestKey.equals(validApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/vnd.api+json");
            response.getWriter().write("{\"errors\": [{\"status\": \"401\", \"title\": \"Unauthorized\", \"detail\": \"Invalid or missing API Key\"}]}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}