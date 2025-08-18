package com.ms_movement_business;


import com.ms_movement_business.api.MovementsApiDelegate;
import com.ms_movement_business.model.MovementRequest;
import com.ms_movement_business.model.MovementResponse;
import com.ms_movement_business.service.MovementService;
import com.ms_movement_business.mapper.MovementMapper;
import com.ms_movement_business.entity.Movement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementApiDelegateImpl implements MovementsApiDelegate {
    private final MovementService service;
    private final MovementMapper mapper;

    public MovementApiDelegateImpl(MovementService service, MovementMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<MovementResponse>> getAllMovements() {
        List<MovementResponse> responses = service.findAll()
                .stream()
                .map(MovementMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<MovementResponse> createMovement(MovementRequest request) {
        Movement movement = mapper.fromRequest(request);
        Movement created = service.create(movement);
        return ResponseEntity.status(201).body(mapper.toResponse(created));
    }

    @Override
    public ResponseEntity<MovementResponse> getMovementById(String id) {
        return service.findById(id)
                .map(MovementMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<MovementResponse> updateMovement(String id, MovementRequest request) {
        Movement movement = mapper.fromRequest(request);
        Movement updated = service.update(id, movement);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteMovement(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MovementResponse>> getMovementsByCustomerId(String customerId) {
        List<MovementResponse> responses = service.findByCustomerId(customerId)
                .stream()
                .map(MovementMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<MovementResponse>> getMovementsByProductId(String productId) {
        List<MovementResponse> responses = service.findByProductId(productId)
                .stream()
                .map(MovementMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}