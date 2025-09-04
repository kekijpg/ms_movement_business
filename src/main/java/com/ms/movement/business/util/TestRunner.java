package com.ms.movement.business.util;

import com.ms.movement.business.service.MovementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {

    private final MovementService movementService;

    public TestRunner(MovementService movementService) {
        this.movementService = movementService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Probando búsqueda de últimos movimientos ===");

        movementService.getLast10Movements("CRED001")
                .doOnNext(movement -> System.out.println(">> " + movement))
                .blockLast();

        System.out.println("=== Fin de la prueba ===");


    }
}
