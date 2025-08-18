package com.ms_movement_business.repository;

import com.ms_movement_business.entity.Movement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovementRepository extends MongoRepository<Movement, String> {
    List<Movement> findByCustomerId(String customerId);
    List<Movement> findByProductId(String productId);
}
