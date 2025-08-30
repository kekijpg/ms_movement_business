package com.ms.movement.business.Impl;

import com.ms.movement.business.api.MovementsApi;
import com.ms.movement.business.service.MovementService;
import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.mapper.MovementMapper;
import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RestController
public class MovementApiImpl implements MovementsApi {
    private final MovementService movementService;

    public MovementApiImpl(MovementService movementService) {
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
}
