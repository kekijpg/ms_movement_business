package com.ms_movement_business.util;

import java.util.UUID;
public final class ShortId {
    private ShortId() {}
    public static String newId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
