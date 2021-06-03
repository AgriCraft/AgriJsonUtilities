package com.agricraft.agrijsonutilities.jsonrecipegen;

import com.google.gson.JsonObject;

import java.nio.file.Path;

public class AgriJsonOutput {
    private final JsonObject json;
    private final Path path;

    public AgriJsonOutput(JsonObject json, Path path) {
        this.json = json;
        this.path = path;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public Path getPath() {
        return this.path;
    }
}
