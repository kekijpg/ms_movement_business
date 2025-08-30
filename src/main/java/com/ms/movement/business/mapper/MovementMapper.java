package com.ms.movement.business.mapper;

import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import com.ms.movement.business.entity.Movement;
import org.springframework.stereotype.Component;

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

        // Manejo de enum -> String
        if (request.getType() != null) {
            movement.setType(request.getType().toString());
        } else {
            movement.setType(null);
        }

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

        // Manejo seguro de String → Enum
        if (movement.getType() != null) {
            try {
                response.setType(MovementResponse.TypeEnum.fromValue(movement.getType()));
            } catch (IllegalArgumentException ex) {
                response.setType(null);
            }
        } else {
            response.setType(null);
        }

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
