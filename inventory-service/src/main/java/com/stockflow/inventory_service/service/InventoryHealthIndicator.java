package com.stockflow.inventory_service.service;

import com.stockflow.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryHealthIndicator implements HealthIndicator {

    private final ProductRepository productRepository;

    @Override
    public Health health() {
        long totalProducts = productRepository.count();

        // Si no hay productos, el sistema está "UP" pero sin datos
        if (totalProducts == 0) {
            return Health.up().withDetail("message", "Inventario vacío").build();
        }

        // Contamos cuántos productos tienen stock <= minStock
        long criticalProducts = productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() <= p.getMinStock())
                .count();

        double criticalPercentage = (double) criticalProducts / totalProducts;

        // Lógica de la prueba: > 20% crítico = DOWN
        if (criticalPercentage > 0.20) {
            return Health.down()
                    .withDetail("status", "CRITICAL_STOCK_LEVELS")
                    .withDetail("criticalPercentage", String.format("%.2f%%", criticalPercentage * 100))
                    .withDetail("criticalCount", criticalProducts)
                    .withDetail("totalCount", totalProducts)
                    .build();
        }

        return Health.up()
                .withDetail("criticalPercentage", String.format("%.2f%%", criticalPercentage * 100))
                .build();
    }
}
