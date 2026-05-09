package com.stockflow.inventory_service.controller;

import com.stockflow.inventory_service.dto.MovementRequest;
import com.stockflow.inventory_service.model.Movement;
import com.stockflow.inventory_service.model.Product;
import com.stockflow.inventory_service.service.InventoryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Endpoints para la gestión de StockFlow")
@CrossOrigin(origins = "*") // Crucial para que Angular se conecte mañana
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/products")
    @Operation(summary = "Obtener todos los productos")
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts(); // Agrega este método al service después
    }

    @GetMapping("/alerts")
    @Operation(summary = "Obtener productos con stock crítico")
    public List<Product> getAlerts() {
        return inventoryService.getCriticalStock();
    }

    @PostMapping("/movements")
    @Operation(summary = "Registrar entrada o salida de inventario")
    public ResponseEntity<Movement> registerMovement(@Valid @RequestBody MovementRequest request) {
        return new ResponseEntity<>(inventoryService.registerMovement(request), HttpStatus.CREATED);
    }

    @GetMapping("/history/{productId}")
    @RateLimiter(name = "historyLimiter") // Punto 2.3: Rate Limiting
    @Operation(summary = "Historial de movimientos por producto")
    public List<Movement> getHistory(@PathVariable Long productId) {
        return inventoryService.getHistoryByProduct(productId);
    }
}
