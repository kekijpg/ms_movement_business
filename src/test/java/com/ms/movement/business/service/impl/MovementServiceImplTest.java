package com.ms.movement.business.service.impl;

import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.impl.MovementServiceImpl;
import com.ms.movement.business.model.MovementResponse;
import com.ms.movement.business.repository.MovementRepository;
import com.ms.movement.business.mapper.MovementMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private MovementMapper movementMapper;

    @InjectMocks
    private MovementServiceImpl service;

    private Movement newMovement(String id, String productId, String customerId, BigDecimal amount, Instant date) {
        Movement m = new Movement();
        m.setId(id);
        m.setProductId(productId);
        m.setCustomerId(customerId);
        m.setAmount(amount);
        m.setDate(date);
        return m;
    }

    @Test
    void createShouldSetDateWhenNullAndSave() {
        Movement req = newMovement(null, "PROD-1", "CUS-1", new BigDecimal("10.00"), null);
        Movement saved = newMovement("ID-1", "PROD-1", "CUS-1", new BigDecimal("10.00"), Instant.now());

        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> {
            Movement arg = inv.getArgument(0);
            saved.setDate(arg.getDate());
            return Mono.just(saved);
        });

        StepVerifier.create(service.create(req))
                .assertNext(out -> {
                    assertEquals("ID-1", out.getId());
                    assertNotNull(out.getDate());
                })
                .verifyComplete();

        verify(movementRepository).save(any(Movement.class));
    }

    @Test
    void createShouldNotOverrideExistingDate() {
        Instant fixed = Instant.parse("2024-01-01T00:00:00Z");
        Movement req = newMovement(null, "PROD-1", "CUS-1", new BigDecimal("10.00"), fixed);
        Movement saved = newMovement("ID-2", "PROD-1", "CUS-1", new BigDecimal("10.00"), fixed);

        when(movementRepository.save(any(Movement.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(req))
                .expectNextMatches(out -> fixed.equals(out.getDate()))
                .verifyComplete();

        verify(movementRepository).save(any(Movement.class));
    }

    @Test
    void getByIdShouldReturnMono() {
        Movement m = newMovement("ID-3", "PROD-1", "CUS-1", new BigDecimal("5.00"), Instant.now());
        when(movementRepository.findById("ID-3")).thenReturn(Mono.just(m));

        StepVerifier.create(service.getById("ID-3"))
                .expectNext(m)
                .verifyComplete();

        verify(movementRepository).findById("ID-3");
    }

    @Test
    void getAllShouldReturnFlux() {
        when(movementRepository.findAll()).thenReturn(Flux.just(
                newMovement("A", "P", "C", new BigDecimal("1.00"), Instant.now()),
                newMovement("B", "P", "C", new BigDecimal("2.00"), Instant.now())
        ));

        StepVerifier.create(service.getAll())
                .expectNextCount(2)
                .verifyComplete();

        verify(movementRepository).findAll();
    }

    @Test
    void updateShouldMergeFieldsAndSetNowOnDate() {
        Movement existing = newMovement("ID-U", "PROD-OLD", "CUS-OLD", new BigDecimal("1.00"),
                Instant.parse("2023-01-01T00:00:00Z"));
        Movement incoming = newMovement(null, "PROD-NEW", "CUS-NEW", new BigDecimal("9.99"),
                Instant.parse("1999-01-01T00:00:00Z"));

        when(movementRepository.findById("ID-U")).thenReturn(Mono.just(existing));
        when(movementRepository.save(any(Movement.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.update("ID-U", incoming))
                .assertNext(updated -> {
                    assertEquals("ID-U", updated.getId());
                    assertEquals("PROD-NEW", updated.getProductId());
                    assertEquals("CUS-NEW", updated.getCustomerId());
                    assertEquals(new BigDecimal("9.99"), updated.getAmount());
                    assertNotNull(updated.getDate());
                    assertTrue(updated.getDate().isAfter(Instant.parse("2023-01-01T00:00:00Z")));
                })
                .verifyComplete();

        verify(movementRepository).findById("ID-U");
        verify(movementRepository).save(any(Movement.class));
    }

    @Test
    void deleteShouldDelegateToRepository() {
        when(movementRepository.deleteById("ID-D")).thenReturn(Mono.empty());

        StepVerifier.create(service.delete("ID-D"))
                .verifyComplete();

        verify(movementRepository).deleteById("ID-D");
    }

    @Test
    void getByCustomerShouldDelegateToRepository() {
        when(movementRepository.findByCustomerId("CUS-1"))
                .thenReturn(Flux.just(newMovement("X", "P", "CUS-1", new BigDecimal("1.00"), Instant.now())));

        StepVerifier.create(service.getByCustomer("CUS-1"))
                .expectNextCount(1)
                .verifyComplete();

        verify(movementRepository).findByCustomerId("CUS-1");
    }

    @Test
    void getLast10MovementsShouldLimitToTen() {
        List<Movement> twenty = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            twenty.add(newMovement("ID-" + i, "PROD-1", "CUS-1", new BigDecimal("1.00"), Instant.now()));
        }
        when(movementRepository.findByProductIdOrderByDateDesc("PROD-1"))
                .thenReturn(Flux.fromIterable(twenty));

        StepVerifier.create(service.getLast10Movements("PROD-1"))
                .expectNextCount(10)
                .verifyComplete();

        verify(movementRepository).findByProductIdOrderByDateDesc("PROD-1");
    }

    @Test
    void generateReportShouldMapMovementsAndBuildResponse() {
        Movement m1 = newMovement("R1", "PROD-1", "CUS-1", new BigDecimal("3.00"), Instant.now());
        Movement m2 = newMovement("R2", "PROD-1", "CUS-1", new BigDecimal("7.00"), Instant.now());

        MovementResponse dto1 = new MovementResponse();
        MovementResponse dto2 = new MovementResponse();

        when(movementRepository.findByProductIdAndDateBetween(anyString(), any(), any()))
                .thenReturn(Flux.just(m1, m2));

        when(movementMapper.toResponse(m1)).thenReturn(dto1);
        when(movementMapper.toResponse(m2)).thenReturn(dto2);

        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end   = Instant.parse("2024-12-31T23:59:59Z");

        StepVerifier.create(service.generateReport("PROD-1", start, end))
                .assertNext(report -> {
                    assertEquals("PROD-1", report.getProductId());
                    assertEquals(start.toString(), report.getStartDate());
                    assertEquals(end.toString(), report.getEndDate());
                    assertNotNull(report.getMovements());
                    assertEquals(2, report.getMovements().size());
                })
                .verifyComplete();

        verify(movementRepository).findByProductIdAndDateBetween(eq("PROD-1"), eq(start), eq(end));
        verify(movementMapper, times(2)).toResponse(any(Movement.class));
    }
}
