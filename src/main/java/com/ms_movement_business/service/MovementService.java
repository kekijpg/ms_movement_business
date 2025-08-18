package com.ms_movement_business.service;

import com.ms_movement_business.entity.Movement;
import com.ms_movement_business.util.ShortId;
import com.ms_movement_business.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovementService {
    private final MovementRepository repository;

    public MovementService(MovementRepository repository) {
        this.repository = repository;
    }

    public List<Movement> findAll() {
        return repository.findAll();
    }

    public Optional<Movement> findById(String id) {
        return repository.findById(id);
    }

    public Movement create(Movement movement) {
        return repository.save(movement);
    }

    public Movement update(String id, Movement movement) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Movement not found with id: " + id);
        movement.setId(id);
        return repository.save(movement);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Movement> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    public List<Movement> findByProductId(String productId) {
        return repository.findByProductId(productId);
    }
}
