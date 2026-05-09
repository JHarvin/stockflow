package com.stockflow.inventory_service.service;

import com.stockflow.inventory_service.dto.MovementRequest;
import com.stockflow.inventory_service.model.Movement;
import com.stockflow.inventory_service.model.Product;
import com.stockflow.inventory_service.repository.MovementRepository;
import com.stockflow.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;

    public List<Product> getCriticalStock() {
        // El repositorio buscará productos donde current_stock <= min_stock
        return productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() <= p.getMinStock())
                .toList();
    }

    @Transactional
    public Movement registerMovement(MovementRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Lógica de actualización de stock
        if ("OUT".equalsIgnoreCase(request.type())) {
            if (product.getCurrentStock() < request.quantity()) {
                throw new RuntimeException("Stock insuficiente para realizar la salida");
            }
            product.setCurrentStock(product.getCurrentStock() - request.quantity());
        } else {
            product.setCurrentStock(product.getCurrentStock() + request.quantity());
        }

        productRepository.save(product);

        Movement movement = Movement.builder()
                .productId(request.productId())
                .type(request.type().toUpperCase())
                .quantity(request.quantity())
                .reason(request.reason())
                .build();

        return movementRepository.save(movement);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Movement> getHistoryByProduct(Long productId) {
        return movementRepository.findByProductId(productId);
    }

}
