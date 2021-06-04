package com.agricraft.agrijsonutilities.json;

import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface IJsonElementProcessor {
    String type();

    JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException;
}
