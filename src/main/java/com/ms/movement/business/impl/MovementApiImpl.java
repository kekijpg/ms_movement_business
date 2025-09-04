package com.ms.movement.business.impl;

import com.ms.movement.business.api.MovementsApi;
import com.ms.movement.business.model.MovementReportResponse;
import com.ms.movement.business.service.MovementService;
import com.ms.movement.business.mapper.MovementMapper;
import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@RestController
public class MovementApiImpl implements MovementsApi {
    private final MovementService movementService;

    @Autowired
    public MovementApiImpl(MovementService movementService, MovementMapper movementMapper) {
        this.movementService = movementService;
    }

    @Override
    public Mono<ResponseEntity<MovementResponse>> createMovement(Mono<MovementRequest> movementRequest,
                                                                 ServerWebExchange exchange) {
        return movementRequest
                .map(MovementMapper::fromRequest)
                .flatMap(movementService::create)
                .map(MovementMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> getAllMovements(ServerWebExchange exchange) {
        Flux<MovementResponse> responses = movementService.getAll()
                .map(MovementMapper::toResponse);
        return Mono.just(ResponseEntity.ok(responses));
    }

    @Override
    public Mono<ResponseEntity<MovementResponse>> getMovementById(String id, ServerWebExchange exchange) {
        return movementService.getById(id)
                .map(MovementMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> getMovementsByCustomerId(String customerId,
                                                                                 ServerWebExchange exchange) {
        Flux<MovementResponse> responses = movementService.getByCustomer(customerId)
                .map(MovementMapper::toResponse);
        return Mono.just(ResponseEntity.ok(responses));
    }

    @Override
    public Mono<ResponseEntity<MovementResponse>> updateMovement(String id,
                                                                 Mono<MovementRequest> movementRequest,
                                                                 ServerWebExchange exchange) {
        return movementRequest
                .map(MovementMapper::fromRequest)
                .flatMap(req -> movementService.update(id, req))
                .map(MovementMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteMovement(String id, ServerWebExchange exchange) {
        return movementService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> getLast10Movements(
            String productId, ServerWebExchange exchange) {

        Flux<MovementResponse> responses = movementService.getLast10Movements(productId)
                .map(MovementMapper::toResponse);

        return Mono.just(ResponseEntity.ok(responses));
    }

    @Override
    public Mono<ResponseEntity<MovementReportResponse>> getMovementReport(
            String productId,
            String startDate,
            String endDate,
            ServerWebExchange exchange) {

        Instant start = Instant.parse(startDate);
        Instant end   = Instant.parse(endDate);

        return movementService.generateReport(productId, start, end)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
