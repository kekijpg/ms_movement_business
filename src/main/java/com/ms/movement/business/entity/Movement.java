package com.ms.movement.business.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;

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

    private Instant date;

    private String description;
}
