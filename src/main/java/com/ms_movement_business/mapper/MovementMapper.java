package com.ms_movement_business.mapper;

import com.ms_movement_business.model.MovementRequest;
import com.ms_movement_business.model.MovementResponse;
import com.ms_movement_business.entity.Movement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovementMapper {
    public static Movement fromRequest(MovementRequest request) {
        if (request == null) return null;

        Movement movement = new Movement();
        movement.setId(null);
        movement.setCustomerId(request.getCustomerId());
        movement.setProductId(request.getProductId());
        movement.setType(request.getType());
        movement.setAmount(request.getAmount());
        movement.setDate(request.getDate());
        movement.setDescription(request.getDescription());

        return movement;
    }

    public static MovementResponse toResponse(Movement movement) {
        if (movement == null) return null;

        MovementResponse response = new MovementResponse();
        response.setId(movement.getId());
        response.setCustomerId(movement.getCustomerId());
        response.setProductId(movement.getProductId());
        response.setType(movement.getType());
        response.setAmount(movement.getAmount());
        response.setDate(movement.getDate());
        response.setDescription(movement.getDescription());

        return response;
    }

    public static List<MovementResponse> toResponseList(List<Movement> movements) {
        List<MovementResponse> responses = new ArrayList<>();
        if (movements != null) {
            for (Movement m : movements) {
                responses.add(toResponse(m));
            }
        }
        return responses;
    }
}
