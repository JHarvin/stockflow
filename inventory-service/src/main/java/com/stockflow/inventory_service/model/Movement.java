package com.stockflow.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")//movimientos en ingles
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String type; // IN / OUT
    private Integer quantity;
    private String reason;
    private LocalDateTime timestamp;
}
