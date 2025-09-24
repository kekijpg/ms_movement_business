package com.ms.movement.business;

import com.ms.movement.business.impl.MovementApiImpl;
import com.ms.movement.business.mapper.MovementMapper;
import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.model.MovementReportResponse;
import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import com.ms.movement.business.service.MovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = MovementApiImpl.class)
@Import(MovementApiImpl.class)
class MovementApiImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovementService movementService;

    @MockBean
    private MovementMapper movementMapper;

    private MovementRequest movementRequest;

    private Movement movementEntity;

    private MovementResponse movementResponse;

    @BeforeEach
    void setUp() {

        movementRequest = new MovementRequest();
        movementRequest.setCustomerId("CUS-1");
        movementRequest.setProductId("PROD-1");
        movementRequest.setType(MovementRequest.TypeEnum.DEPOSITO);
        movementRequest.setAmount(new BigDecimal("12.5"));
        movementRequest.setDate("2025-01-01T00:00:00Z");
        movementRequest.setDescription("purchase");

        movementEntity = new Movement();
        movementEntity.setId("MOV-1");
        movementEntity.setCustomerId("CUS-1");
        movementEntity.setProductId("PROD-1");
        movementEntity.setType("DEPOSITO");
        movementEntity.setAmount(new BigDecimal("12.5"));
        movementEntity.setDate(Instant.parse("2025-01-01T00:00:00Z"));
        movementEntity.setDescription("purchase");

        movementResponse = new MovementResponse();
        movementResponse.setId("MOV-1");
        movementResponse.setCustomerId("CUS-1");
        movementResponse.setProductId("PROD-1");
        movementResponse.setType(MovementResponse.TypeEnum.DEPOSITO);
        movementResponse.setAmount(new BigDecimal("12.5"));
        movementResponse.setDate(OffsetDateTime.parse("2025-01-01T00:00:00Z"));
        movementResponse.setDescription("purchase");
    }

    @Test
    void createMovementShouldReturnOk() {
        when(movementMapper.fromRequest(any(MovementRequest.class))).thenReturn(movementEntity);
        when(movementService.create(movementEntity)).thenReturn(Mono.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.post()
                .uri("/api/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movementRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);

        verify(movementService).create(any());
    }

    // el service falla => 5xx (propaga error)
    @Test
    void createMovementShouldPropagateServerError() {
        when(movementMapper.fromRequest(any(MovementRequest.class))).thenReturn(movementEntity);
        when(movementService.create(movementEntity))
                .thenReturn(Mono.error(new RuntimeException("boom"))); // simulamos fallo

        webTestClient.post()
                .uri("/api/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movementRequest)
                .exchange()
                .expectStatus().is5xxServerError(); //da error

        verify(movementService).create(any());
    }

    @Test
    void getAllMovementsShouldReturnOk() {
        when(movementService.getAll()).thenReturn(Flux.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.get()
                .uri("/api/movements")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(1)
                .contains(movementResponse);

        verify(movementService).getAll();
    }

    @Test
    void getAllMovementsShouldReturnOkWithEmptyList() {
        when(movementService.getAll()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/movements")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(0);
    }

    @Test
    void getMovementByIdShouldReturnOk() {
        when(movementService.getById("MOV-1")).thenReturn(Mono.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.get()
                .uri("/api/movements/MOV-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);

        verify(movementService).getById("MOV-1");
    }

    @Test
    void getMovementByIdShouldReturnNotFound() {
        when(movementService.getById("MOV-1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/movements/MOV-1")
                .exchange()
                .expectStatus().isNotFound();

        verify(movementService).getById("MOV-1");
    }

    @Test
    void getMovementsByCustomerShouldReturnOkWithEmptyList() {
        when(movementService.getByCustomer("CUS-EMPTY")).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/movements/customer/CUS-EMPTY")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(0);
    }

    @Test
    void updateMovementShouldReturnOk() {
        when(movementMapper.fromRequest(any(MovementRequest.class))).thenReturn(movementEntity);
        when(movementService.update(eq("MOV-1"), any())).thenReturn(Mono.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.put()
                .uri("/api/movements/MOV-1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movementRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementResponse.class)
                .isEqualTo(movementResponse);

        verify(movementService).update(eq("MOV-1"), any());
    }

    //service devuelve Mono.empty()
    @Test
    void updateMovementShouldReturnNotFoundWhenServiceReturnsEmpty() {
        when(movementMapper.fromRequest(any(MovementRequest.class))).thenReturn(movementEntity);
        when(movementService.update(eq("MOV-404"), any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/movements/MOV-404")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movementRequest)
                .exchange()
                .expectStatus().isNotFound();

        verify(movementService).update(eq("MOV-404"), any());
    }

    @Test
    void deleteMovementShouldReturnNoContent() {
        when(movementService.delete("MOV-1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/movements/MOV-1")
                .exchange()
                .expectStatus().isNoContent();

        verify(movementService).delete("MOV-1");
    }

    @Test
    void getMovementsByCustomerShouldReturnOk() {
        when(movementService.getByCustomer("CUS-1")).thenReturn(Flux.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.get()
                .uri("/api/movements/customer/CUS-1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(1)
                .contains(movementResponse);

        verify(movementService).getByCustomer("CUS-1");
    }

    @Test
    void getLast10MovementsShouldReturnOk() {
        when(movementService.getLast10Movements("PROD-1")).thenReturn(Flux.just(movementEntity));
        when(movementMapper.toResponse(movementEntity)).thenReturn(movementResponse);

        webTestClient.get()
                .uri("/api/movements/last10/PROD-1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovementResponse.class)
                .hasSize(1)
                .contains(movementResponse);

        verify(movementService).getLast10Movements("PROD-1");
    }

    @Test
    void getMovementReportShouldReturnOk() {
        MovementReportResponse report = new MovementReportResponse();
        report.setProductId("PROD-1");
        report.setStartDate("2024-01-01T00:00:00Z");
        report.setEndDate("2024-12-31T23:59:59Z");
        report.setMovements(List.of(movementResponse));

        when(movementService.generateReport(
                eq("PROD-1"),
                eq(Instant.parse("2024-01-01T00:00:00Z")),
                eq(Instant.parse("2024-12-31T23:59:59Z"))
        )).thenReturn(Mono.just(report));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/movements/report/PROD-1")
                        .queryParam("startDate", "2024-01-01T00:00:00Z")
                        .queryParam("endDate", "2024-12-31T23:59:59Z")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovementReportResponse.class)
                .isEqualTo(report);
    }

    //el service devuelve Mono.empty()
    @Test
    void getMovementReportShouldReturnNoContentWhenServiceReturnsEmpty() {
        when(movementService.generateReport(
                eq("PROD-EMPTY"),
                eq(Instant.parse("2024-01-01T00:00:00Z")),
                eq(Instant.parse("2024-12-31T23:59:59Z"))
        )).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/movements/report/PROD-EMPTY")
                        .queryParam("startDate", "2024-01-01T00:00:00Z")
                        .queryParam("endDate", "2024-12-31T23:59:59Z")
                        .build())
                .exchange()
                .expectStatus().isNoContent(); //204
    }
}
