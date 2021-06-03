package com.agricraft.agrijsonutilities.jsonrecipegen;

import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public interface IJsonElementProcessor {
    String type();

    JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException;
}
