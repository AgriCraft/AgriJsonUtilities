package com.agricraft.agrijsonutilities.jsonrecipegen;

import com.agricraft.agrijsonutilities.util.AgriJson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonConversionAction {
    private final String key;
    private final IJsonElementProcessor processor;
    private final JsonObject input;

    public JsonConversionAction(String key, JsonObject input) {
        this.key = key;
        if(!input.has("type")) {
            throw new JsonSyntaxException("json conversion field target must have a \"type\" specified");
        }
        String type = input.get("type").getAsString();
        IJsonElementProcessor processor = ElementProcessors.getProcessor(type);
        if(processor == null) {
            throw new JsonSyntaxException("json conversion field has an invalid \"type\" specified: " + type);
        }
        this.processor = processor;
        this.input = input;
    }

    public String getKey() {
        return this.key;
    }

    public void process(JsonObject output, AgriJson source) {
        output.add(this.getKey(), this.processor.apply(this.input, source));
    }
}
