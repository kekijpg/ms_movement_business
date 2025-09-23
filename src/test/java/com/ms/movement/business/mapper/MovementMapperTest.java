package com.ms.movement.business.mapper;

import com.ms.movement.business.entity.Movement;
import com.ms.movement.business.model.MovementRequest;
import com.ms.movement.business.model.MovementResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovementMapperTest {
    private final MovementMapper mapper = new MovementMapper();

    @Test
    void fromRequestShouldMapAllFieldsWithZDate() {
        MovementRequest req = new MovementRequest();
        req.setCustomerId("CUS-1");
        req.setProductId("PROD-1");
        req.setType(MovementRequest.TypeEnum.DEPOSITO);
        req.setAmount(new BigDecimal("12.5"));
        req.setDate("2025-09-02T20:00:00Z");
        req.setDescription("hello");

        Movement out = mapper.fromRequest(req);

        assertNotNull(out);
        assertNull(out.getId());
        assertEquals("CUS-1", out.getCustomerId());
        assertEquals("PROD-1", out.getProductId());
        assertEquals("DEPOSITO", out.getType());
        assertEquals(new BigDecimal("12.5"), out.getAmount());
        assertEquals(Instant.parse("2025-09-02T20:00:00Z"), out.getDate());
        assertEquals("hello", out.getDescription());
    }

    @Test
    void fromRequestShouldParseDateWithoutZoneAsUTC() {
        String noZone = "2025-09-02T20:00:00";
        Instant expected = LocalDateTime.parse(noZone)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        MovementRequest req = new MovementRequest();
        req.setCustomerId("C");
        req.setProductId("P");
        req.setType(MovementRequest.TypeEnum.RETIRO);
        req.setAmount(new BigDecimal("1.00"));
        req.setDate(noZone);

        Movement out = mapper.fromRequest(req);

        assertEquals(expected, out.getDate());
        assertEquals("RETIRO", out.getType());
    }

    @Test
    void fromRequestShouldHandleNulls() {
        // request null -> null
        assertNull(mapper.fromRequest(null));

        MovementRequest req = new MovementRequest();
        req.setCustomerId("C");
        req.setProductId("P");
        req.setType(null);
        req.setAmount(new BigDecimal("2.00"));
        req.setDate(null);

        Movement out = mapper.fromRequest(req);

        assertNotNull(out);
        assertNull(out.getType());
        assertNull(out.getDate());
        assertEquals("C", out.getCustomerId());
        assertEquals("P", out.getProductId());
        assertEquals(new BigDecimal("2.00"), out.getAmount());
    }

    @Test
    void toResponseShouldMapAndConvertInstantToAmericaLimaOffset() {
        Instant instant = Instant.parse("2025-01-01T03:00:00Z");
        Movement m = new Movement();
        m.setId("ID-1");
        m.setCustomerId("CUS-1");
        m.setProductId("PROD-1");
        m.setType("TRANSFERENCIA");
        m.setAmount(new BigDecimal("99.99"));
        m.setDate(instant);
        m.setDescription("desc");

        MovementResponse resp = mapper.toResponse(m);

        assertNotNull(resp);
        assertEquals("ID-1", resp.getId());
        assertEquals("CUS-1", resp.getCustomerId());
        assertEquals("PROD-1", resp.getProductId());
        assertEquals(MovementResponse.TypeEnum.TRANSFERENCIA, resp.getType());
        assertEquals(new BigDecimal("99.99"), resp.getAmount());
        assertEquals("desc", resp.getDescription());

        assertNotNull(resp.getDate());
        assertEquals(instant, resp.getDate().toInstant());
        ZoneId lima = ZoneId.of("America/Lima");
        ZoneOffset expectedOffset = lima.getRules().getOffset(instant);
        assertEquals(expectedOffset, resp.getDate().getOffset());
    }

    @Test
    void toResponseShouldSetNullTypeWhenStringIsInvalid() {
        Movement m = new Movement();
        m.setType("NO_EXISTE");

        MovementResponse resp = mapper.toResponse(m);

        assertNotNull(resp);
        assertNull(resp.getType());
    }

    @Test
    void toResponseShouldReturnNullWhenInputNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toResponseListShouldMapAll() {
        Movement a = new Movement();
        a.setId("A");
        a.setType("DEPOSITO");

        Movement b = new Movement();
        b.setId("B");
        b.setType("RETIRO");

        List<MovementResponse> list = mapper.toResponseList(Arrays.asList(a, b));

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getId());
        assertEquals(MovementResponse.TypeEnum.DEPOSITO, list.get(0).getType());
        assertEquals("B", list.get(1).getId());
        assertEquals(MovementResponse.TypeEnum.RETIRO, list.get(1).getType());
    }

}
