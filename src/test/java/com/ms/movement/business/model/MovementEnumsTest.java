package com.ms.movement.business.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementEnumsTest {

    //no coincide
    @Test
    void requestTypeEnumFromValueInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> MovementRequest.TypeEnum.fromValue("NO_EXISTE"));
    }

    @Test
    void responseTypeEnumFromValueValid() {
        MovementResponse.TypeEnum e = MovementResponse.TypeEnum.fromValue("DEPOSITO");
        assertEquals(MovementResponse.TypeEnum.DEPOSITO, e);
    }

    //prueba constructores vacios
    @Test
    void movementResponseDefaultConstructor() {
        MovementResponse resp = new MovementResponse();
        assertNull(resp.getId());
        assertNull(resp.getCustomerId());
    }


}
