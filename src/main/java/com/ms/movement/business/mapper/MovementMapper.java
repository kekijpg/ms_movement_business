package com.ms.movement.business.mapper;

import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import com.ms.movement.business.entity.Movement;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovementMapper {
    public Movement fromRequest(MovementRequest request) {
        if (request == null) {
            return null;
        }

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

        //Manejo flexible de fecha: con o sin Z
        if (request.getDate() != null) {
            try {
                // Caso 1: viene con Z o con offset (ej: 2025-09-02T20:00:00Z o 2025-09-02T20:00:00-05:00)
                movement.setDate(Instant.parse(request.getDate()));
            } catch (DateTimeParseException ex) {
                // Caso 2: viene sin zona (ej: 2025-09-02T20:00:00)
                movement.setDate(
                        LocalDateTime.parse(request.getDate())
                                .atZone(ZoneOffset.UTC)
                                .toInstant()
                );
            }
        }

        movement.setDescription(request.getDescription());

        return movement;
    }

    public MovementResponse toResponse(Movement movement) {
        if (movement == null) {
            return null;
        }

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

        if (movement.getDate() != null) {
            response.setDate(
                    movement.getDate()
                            .atZone(ZoneId.of("America/Lima"))
                            .toOffsetDateTime()
            );
        } else {
            response.setDate(null);
        }

        response.setDescription(movement.getDescription());

        return response;
    }

    public List<MovementResponse> toResponseList(List<Movement> movements) {
        List<MovementResponse> responses = new ArrayList<>();
        if (movements != null) {
            for (Movement m : movements) {
                responses.add(toResponse(m));
            }
        }
        return responses;
    }
}
