package com.ms.movement.business.repository;

import com.ms.movement.business.entity.Movement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<Movement, String> {
    //genera la consulta por SpringData
    Flux<Movement> findByCustomerId(String customerId);

    Flux<Movement> findByProductIdOrderByDateDesc(String productId);

    Flux<Movement> findByProductIdAndDateBetween(String productId, Instant startDate, Instant endDate);
}
