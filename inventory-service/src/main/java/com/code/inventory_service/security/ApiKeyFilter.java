package com.code.inventory_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api-key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 🔥 SOLUCIÓN CORS: Permitir siempre las peticiones OPTIONS (Preflight)
        // El navegador las manda sin API Key para verificar permisos. Debemos dejarlas pasar.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Excluir Swagger (Opcional, pero recomendado)
        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Validación normal de API Key
        String requestKey = request.getHeader("X-API-KEY");

        if (requestKey == null || !requestKey.equals(validApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errors\": [{\"status\": \"401\", \"title\": \"Unauthorized\", \"detail\": \"Invalid or missing API Key\"}]}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}