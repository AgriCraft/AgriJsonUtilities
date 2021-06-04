package com.agricraft.agrijsonutilities.util;

import com.google.gson.JsonObject;

public class AgriJson {
    private final JsonObject json;
    private final String subDir;
    private final AgriJsonType type;

    public AgriJson(JsonObject json, String subDir, AgriJsonType type) {
        this.json = json;
        this.subDir = subDir;
        this.type = type;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public String getSubDir() {
        return this.subDir;
    }

    public AgriJsonType getType() {
        return this.type;
    }
}
