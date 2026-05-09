package com.stockflow.inventory_service.repository;

import com.stockflow.inventory_service.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByProductId(Long productId);
}
