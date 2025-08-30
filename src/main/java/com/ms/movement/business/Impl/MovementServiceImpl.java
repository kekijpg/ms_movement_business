package com.ms.movement.business.Impl;

import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.repository.MovementRepository;
import com.ms.movement.business.service.MovementService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service

public class MovementServiceImpl implements MovementService {
    private final MovementRepository movementRepository;

    public MovementServiceImpl(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public Mono<Movement> create(Movement movement) {
        movement.setDate(LocalDate.now().toString());
        return movementRepository.save(movement);
    }

    @Override
    public Mono<Movement> getById(String movementId) {
        return movementRepository.findById(movementId);
    }

    @Override
    public Flux<Movement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    public Mono<Movement> update(String movementId, Movement movement) {
        return movementRepository.findById(movementId)
                .flatMap(existing -> {
                    existing.setAmount(movement.getAmount());
                    existing.setType(movement.getType());
                    existing.setCustomerId(movement.getCustomerId());
                    existing.setProductId(movement.getProductId());
                    existing.setDate(LocalDate.now().toString());
                    return movementRepository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(String movementId) {
        return movementRepository.deleteById(movementId);
    }

    @Override
    public Flux<Movement> getByCustomer(String customerId) {
        return movementRepository.findByCustomerId(customerId);
    }

}
