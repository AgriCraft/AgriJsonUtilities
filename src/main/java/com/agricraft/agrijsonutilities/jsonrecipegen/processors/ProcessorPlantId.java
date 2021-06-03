package com.agricraft.agrijsonutilities.jsonrecipegen.processors;

import com.agricraft.agrijsonutilities.jsonrecipegen.IJsonElementProcessor;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ProcessorPlantId implements IJsonElementProcessor {
    @Override
    public String type() {
        return "plant_id";
    }

    @Override
    public JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException {
        if(source.getType() != AgriJsonType.PLANT) {
            throw new InvalidAgriJsonTypeException(this.type() + " json processor expects a plant json as source object");
        }
       return new JsonPrimitive(source.getJson().get("id").getAsString());
    }
}
