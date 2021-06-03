package com.agricraft.agrijsonutilities.util;

import com.google.gson.JsonParseException;

import java.util.Arrays;

public enum AgriJsonType {
    PLANT,
    SOIL,
    MUTATION,
    WEED;

    public static AgriJsonType fromString(String string) throws JsonParseException {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(string))
                .findFirst()
                .orElseThrow(() -> new JsonParseException("Invalid AgriJsonType: " + string));
    }
}
