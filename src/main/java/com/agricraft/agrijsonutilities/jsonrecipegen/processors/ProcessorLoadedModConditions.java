package com.agricraft.agrijsonutilities.jsonrecipegen.processors;

import com.agricraft.agrijsonutilities.jsonrecipegen.IJsonElementProcessor;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.InvalidAgriJsonTypeException;
import com.google.common.collect.Sets;
import com.google.gson.*;

import java.util.Set;

public class ProcessorLoadedModConditions implements IJsonElementProcessor {
    @Override
    public String type() {
        return "loaded_mod_conditions";
    }

    @Override
    public JsonElement apply(JsonObject input, AgriJson source) throws InvalidAgriJsonTypeException {
        JsonArray output = new JsonArray();
        Set<String> included = Sets.newHashSet("agricraft", "minecraft");
        if(input.has("include")) {
            this.addConditions(output, input.get("include").getAsJsonArray(), included);
        }
        this.addConditions(output, source.getJson().get("mods").getAsJsonArray(), included);
        return output;
    }

    protected void addConditions(JsonArray output, JsonArray inputs, Set<String> included) {
        for(int i = 0; i < inputs.size(); i++) {
            String mod = inputs.get(i).getAsString();
            if(!included.contains(mod)) {
                included.add(mod);
                output.add(this.createCondition(mod));
            }
        }
    }

    protected JsonObject createCondition(String mod) {
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "forge:mod_loaded");
        condition.addProperty("modid", mod);
        return condition;
    }
}
