package com.stockflow.inventory_service.service;

import com.stockflow.inventory_service.model.Product;
import com.stockflow.inventory_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryHealthIndicatorTest {
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private InventoryHealthIndicator healthIndicator;

    @Test
    void health_ShouldReturnDown_WhenCriticalStockAbove20Percent() {
        // Simular
        List<Product> products = List.of(
                Product.builder().currentStock(1).minStock(5).build(),
                Product.builder().currentStock(1).minStock(5).build(),
                Product.builder().currentStock(1).minStock(5).build()
        );
        when(repository.count()).thenReturn(10L);
        when(repository.findAll()).thenReturn(products);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }
}

