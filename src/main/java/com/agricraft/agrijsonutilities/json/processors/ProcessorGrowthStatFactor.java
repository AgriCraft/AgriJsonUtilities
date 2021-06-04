package com.agricraft.agrijsonutilities.json.processors;

import com.agricraft.agrijsonutilities.json.IJsonElementProcessor;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ProcessorGrowthStatFactor implements IJsonElementProcessor {
    @Override
    public String type() {
        return "growth_stat_factor";
    }

    @Override
    public JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException {
        if(source.getType() != AgriJsonType.PLANT) {
            throw new InvalidAgriJsonTypeException(this.type() + " json processor expects a plant json as source object");
        }
        return new JsonPrimitive(source.getJson().get("growth_bonus").getAsDouble()/source.getJson().get("growth_chance").getAsDouble());
    }
}
