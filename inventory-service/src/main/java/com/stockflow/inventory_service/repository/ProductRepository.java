package com.stockflow.inventory_service.repository;

import com.stockflow.inventory_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCurrentStockLessThanEqual(Integer minStock); // Para las alertas
}
