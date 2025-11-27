package com.code.product_service.security; // ⚠️ OJO: Cambia el paquete según corresponda (product o inventory)

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

    @Value("${app.api-key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // --- LOGS DE DEBUGGING (Bórralos luego) ---
        System.out.println(">>> FILTRO EJECUTÁNDOSE PARA: " + request.getMethod() + " " + request.getRequestURI());
        // ------------------------------------------

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println(">>> ¡ES OPTIONS! DEJANDO PASAR..."); // Si no ves esto, el IF no funciona
            filterChain.doFilter(request, response);
            return;
        }

        String requestKey = request.getHeader("X-API-KEY");

        // Loguear qué clave llegó (cuidado, solo para dev local)
        System.out.println(">>> API KEY RECIBIDA: " + requestKey);

        if (requestKey == null || !requestKey.equals(validApiKey)) {
            System.out.println(">>> ⛔ BLOQUEADO POR API KEY INCORRECTA");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // ... resto del código de error ...
            return;
        }

        filterChain.doFilter(request, response);
    }
}