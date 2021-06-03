package com.agricraft.agrijsonutilities.util;

import com.google.gson.JsonObject;

public class AgriJson {
    private final JsonObject json;
    private final AgriJsonType type;

    public AgriJson(JsonObject json, AgriJsonType type) {
        this.json = json;
        this.type = type;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public AgriJsonType getType() {
        return this.type;
    }
}
