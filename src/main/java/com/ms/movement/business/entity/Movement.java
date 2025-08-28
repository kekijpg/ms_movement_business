package com.ms.movement.business.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "movements")
public class Movement {
    @Id
    private String id;
    private String productId;
    private String customerId;
    private String type;
    private BigDecimal amount;
    private String date;
    private String description;
    private BigDecimal commissionAmount;    //monto de la comision
    private BigDecimal commission;          //max de movimiento
    private String relatedAccountId;        //cuenta destino si es una transferencia
    private String correlationId;           //rastrear transacciones
}
