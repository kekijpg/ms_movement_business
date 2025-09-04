package com.ms.movement.business.impl;

import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.mapper.MovementMapper;
import com.ms.movement.business.model.MovementReportResponse;
import com.ms.movement.business.repository.MovementRepository;
import com.ms.movement.business.service.MovementService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service

public class MovementServiceImpl implements MovementService {
    private final MovementRepository movementRepository;

    public MovementServiceImpl(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public Mono<Movement> create(Movement movement) {
        if (movement.getDate() == null) {
            movement.setDate(Instant.now());
        }
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
                    existing.setDate(Instant.now());
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

    @Override
    public Flux<Movement> getLast10Movements(String productId) {
        return movementRepository.findByProductIdOrderByDateDesc(productId)
                .take(10);
    }

    @Override
    public Mono<MovementReportResponse> generateReport(String productId, Instant startDate, Instant endDate) {
        return movementRepository.findByProductIdAndDateBetween(productId, startDate, endDate)
                .map(MovementMapper::toResponse)
                .collectList()
                .map(responses -> {
                    MovementReportResponse report = new MovementReportResponse();
                    report.setProductId(productId);
                    report.setStartDate(startDate.toString());
                    report.setEndDate(endDate.toString());
                    report.setMovements(responses);
                    return report;
                });
    }
}
