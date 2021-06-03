package com.agricraft.agrijsonutilities.jsonrecipegen.processors;

import com.agricraft.agrijsonutilities.jsonrecipegen.IJsonElementProcessor;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.gson.*;

public class ProcessorGrowthTicks implements IJsonElementProcessor {
    @Override
    public String type() {
        return "growth_ticks";
    }

    @Override
    public JsonElement apply(JsonObject input, AgriJson source) throws JsonParseException {
        if(source.getType() != AgriJsonType.PLANT) {
            throw new InvalidAgriJsonTypeException(this.type() + " json processor expects a plant json as source object");
        }
        if(!input.has("base")) {
            throw new JsonSyntaxException(this.type() + " needs a \"base\" property");
        }
        int base = input.get("base").getAsInt();
        return new JsonPrimitive((int) (base/source.getJson().get("growth_chance").getAsDouble()));
    }
}
