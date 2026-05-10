package com.stockflow.inventory_service.service;

import com.stockflow.inventory_service.dto.MovementRequest;
import com.stockflow.inventory_service.model.Movement;
import com.stockflow.inventory_service.model.Product;
import com.stockflow.inventory_service.repository.MovementRepository;
import com.stockflow.inventory_service.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("Debe reducir el stock correctamente cuando el movimiento es OUT")
    void registerMovement_ShouldReduceStock_WhenTypeIsOUT() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .currentStock(10)
                .minStock(5)
                .unitPrice(new BigDecimal("1000"))
                .build();

        MovementRequest request = new MovementRequest(1L, "OUT", 3, "Venta");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.save(any(Movement.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        inventoryService.registerMovement(request);

        // Assert
        assertEquals(7, product.getCurrentStock(), "El stock debería haber bajado de 10 a 7");
        verify(productRepository, times(1)).save(product);
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no hay suficiente stock para una salida")
    void registerMovement_ShouldThrowException_WhenStockIsInsufficient() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .currentStock(2)
                .minStock(5)
                .build();

        MovementRequest request = new MovementRequest(1L, "OUT", 5, "Venta grande");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.registerMovement(request);
        });

        assertEquals("Stock insuficiente para realizar la salida", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
}
