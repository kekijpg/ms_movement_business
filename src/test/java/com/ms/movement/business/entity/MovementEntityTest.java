package com.ms.movement.business.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MovementEntityTest {
    @Test
    void movementEntityShouldHoldValues() {
        Movement m = new Movement();
        m.setId("ID-1");
        m.setProductId("PROD-1");
        m.setCustomerId("CUS-1");
        m.setType("DEPOSITO");
        m.setAmount(new BigDecimal("123.4"));
        Instant now = Instant.parse("2025-01-01T00:00:00Z");
        m.setDate(now);
        m.setDescription("desc");

        assertEquals("ID-1", m.getId());
        assertEquals("PROD-1", m.getProductId());
        assertEquals("CUS-1", m.getCustomerId());
        assertEquals("DEPOSITO", m.getType());
        assertEquals(new BigDecimal("123.4"), m.getAmount());
        assertEquals(now, m.getDate());
        assertEquals("desc", m.getDescription());
    }

    @Test
    void movementEntityToStringNotNull() {
        Movement m = new Movement();
        m.setId("X");
        assertNotNull(m.toString());
        assertTrue(m.toString().contains("X"));
    }

}
