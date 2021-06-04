package com.agricraft.agrijsonutilities.json;

import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.List;

public class JsonConversionTemplate {
    private final AgriJsonType sourceType;
    private final File outputDir;
    private final List<JsonConversionAction> actions;

    public JsonConversionTemplate(String outputDir, JsonObject template) {
        this(new File(outputDir), template);
    }

    public JsonConversionTemplate(File outputDir, JsonObject template) {
        JsonObject configuration = template.get("<agri_json_processor_config>").getAsJsonObject();
        if(!configuration.has("target")) {
            throw new JsonSyntaxException("Json template configuration must have a target");
        }
        this.sourceType = AgriJsonType.fromString(configuration.get("target").getAsString());
        if(configuration.has("directory")) {
            this.outputDir = new File(outputDir, configuration.get("directory").getAsString());
        } else {
            this.outputDir = outputDir;
        }
        ImmutableList.Builder<JsonConversionAction> listBuilder = new ImmutableList.Builder<>();
        boolean flag = true;
        if(configuration.has("process")) {
            JsonArray array = configuration.get("process").getAsJsonArray();
            for(int i = 0; i < array.size(); i++) {
                flag = false;
                String key = array.get(i).getAsString();
                if(!template.has(key)) {
                    throw new JsonSyntaxException("Template must have a property defined for key: \"" + key + "\"");
                }
                listBuilder.add(new JsonConversionAction(key, template.get(key).getAsJsonObject()));
            }
        }
        if(flag) {
            System.out.println("WARNING: no processors have been defined in the given configuration");
        }
        this.actions = listBuilder.build();
    }

    public AgriJsonType getSourceType() {
        return this.sourceType;
    }

    public File getOutputDir() {
        return this.outputDir;
    }

    public JsonObject apply(AgriJson source) {
        if(source.getType() != this.getSourceType()) {
            throw new JsonSyntaxException("Invalid source json (type: " + source.getType() + ") was received, expected " + this.getSourceType());
        }
        JsonObject output = new JsonObject();
        this.actions.forEach(action -> action.process(output, source));
        return output;
    }

}
