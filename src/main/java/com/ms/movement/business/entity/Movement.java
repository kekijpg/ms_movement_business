package com.ms.movement.business.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

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
}
