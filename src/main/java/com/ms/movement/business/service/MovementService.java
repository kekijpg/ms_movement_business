package com.ms.movement.business.service;

import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.model.MovementReportResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface MovementService {
    // registra un movimiento
    Mono<Movement> create(Movement movement);

    // hace la busqueda del ID
    Mono<Movement> getById(String movementId);

    // listado completo
    Flux<Movement> getAll();

    // actualiza un movimiento
    Mono<Movement> update(String movementId, Movement movement);

    // elimina un movimiento por ID
    Mono<Void> delete(String movementId);

    Flux<Movement> getByCustomer(String customerId);

    Flux<Movement> getLast10Movements(String productId);

    Mono<MovementReportResponse> generateReport(String productId, Instant startDate, Instant endDate);
}

