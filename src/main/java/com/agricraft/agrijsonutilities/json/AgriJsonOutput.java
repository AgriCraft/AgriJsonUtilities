package com.agricraft.agrijsonutilities.json;

import com.google.gson.JsonObject;

import java.io.File;

public class AgriJsonOutput {
    private final JsonObject json;
    private final File path;

    public AgriJsonOutput(JsonObject json, File path) {
        this.json = json;
        this.path = path;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public File getPath() {
        return this.path;
    }
}
