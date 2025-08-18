package com.ms_movement_business.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "movements")
public class Movement {
    @Id
    private String id;
    private String productId;
    private String customerId;
    private String type;
    private String amount;
    private String date;
    private String description;
}
