package com.agricraft.agrijsonutilities.util;

import com.google.gson.JsonObject;

public class AgriJson {
    private final JsonObject json;
    private final String originalPath;
    private final AgriJsonType type;

    public AgriJson(JsonObject json, String originalPath, AgriJsonType type) {
        this.json = json;
        this.originalPath = originalPath;
        this.type = type;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public String getOriginalPath() {
        return this.originalPath;
    }

    public AgriJsonType getType() {
        return this.type;
    }
}
