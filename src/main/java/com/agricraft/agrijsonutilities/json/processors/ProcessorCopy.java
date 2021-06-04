package com.agricraft.agrijsonutilities.json.processors;

import com.agricraft.agrijsonutilities.json.IJsonElementProcessor;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class ProcessorCopy implements IJsonElementProcessor {
    @Override
    public String type() {
        return "copy";
    }

    @Override
    public JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException {
        if(!input.has("value")) {
            throw new JsonSyntaxException("copy processor must have a \"value\" field");
        }
        return input.get("value");
    }
}
