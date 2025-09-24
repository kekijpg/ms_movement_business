package com.ms.movement.business.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class MovementModelTest {

    @Test
    void movementRequestShouldHoldValues() {
        MovementRequest req = new MovementRequest();
        req.setCustomerId("CUS-123");
        req.setProductId("PROD-123");
        req.setType(MovementRequest.TypeEnum.DEPOSITO);
        req.setAmount(new BigDecimal("100.00"));
        req.setDate("2025-09-22T00:00:00Z");
        req.setDescription("test");

        assertEquals("CUS-123", req.getCustomerId());
        assertEquals("PROD-123", req.getProductId());
        assertEquals(MovementRequest.TypeEnum.DEPOSITO, req.getType());
        assertEquals(new BigDecimal("100.00"), req.getAmount());
        assertEquals("2025-09-22T00:00:00Z", req.getDate());
        assertEquals("test", req.getDescription());
    }

    @Test
    void movementResponseShouldHoldValues() {
        MovementResponse resp = new MovementResponse();
        resp.setId("MOV-1");
        resp.setProductId("PROD-1");
        resp.setCustomerId("CUS-1");
        resp.setType(MovementResponse.TypeEnum.RETIRO);
        resp.setAmount(new BigDecimal("9.99"));
        resp.setDate(OffsetDateTime.parse("2025-01-01T00:00:00Z"));
        resp.setDescription("desc");

        assertEquals("MOV-1", resp.getId());
        assertEquals("PROD-1", resp.getProductId());
        assertEquals("CUS-1", resp.getCustomerId());
        assertEquals(MovementResponse.TypeEnum.RETIRO, resp.getType());
        assertEquals(new BigDecimal("9.99"), resp.getAmount());
        assertEquals(OffsetDateTime.parse("2025-01-01T00:00:00Z"), resp.getDate());
        assertEquals("desc", resp.getDescription());
    }

    @Test
    void movementReportResponseShouldHoldValues() {
        MovementReportResponse report = new MovementReportResponse();
        report.setProductId("PROD-X");
        report.setStartDate("2024-01-01T00:00:00Z");
        report.setEndDate("2024-12-31T23:59:59Z");
        report.setMovements(Collections.emptyList());

        assertEquals("PROD-X", report.getProductId());
        assertEquals("2024-01-01T00:00:00Z", report.getStartDate());
        assertEquals("2024-12-31T23:59:59Z", report.getEndDate());
        assertNotNull(report.getMovements());
        assertTrue(report.getMovements().isEmpty());
    }
}
