package com.code.product_service.config;

import com.code.product_service.model.Product;
import com.code.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            List<Product> products = List.of(
                new Product(null, "MacBook Pro M3", "Chip M3 Pro, 18GB RAM, 512GB SSD. Space Black.", 2499.00),
                new Product(null, "iPhone 15 Pro", "Titanio natural, botón de acción, A17 Pro.", 999.00),
                new Product(null, "Sony WH-1000XM5", "Cancelación de ruido líder en la industria.", 348.00),
                new Product(null, "Monitor Dell UltraSharp", "32 pulgadas 4K USB-C Hub.", 850.00),
                new Product(null, "Keychron Q1 Pro", "Teclado mecánico custom inalámbrico.", 199.00)
            );
            repository.saveAll(products);
            System.out.println(">>> 🚀 DATA SEEDER: Productos iniciales cargados.");
        }
    }
}