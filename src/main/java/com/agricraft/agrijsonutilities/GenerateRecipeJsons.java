package com.agricraft.agrijsonutilities;

import com.agricraft.agrijsonutilities.github.GitHubFetcher;
import com.agricraft.agrijsonutilities.json.JsonConversionTemplate;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class GenerateRecipeJsons {
    public static void main(String[] args) {

    }

    private static void testJsonConversion() {
        // temporary test implementation
        try {
            JsonParser parser = new JsonParser();
            JsonObject input = parser.parse(new FileReader(new File("wheat_plant.json"))).getAsJsonObject();
            JsonObject jsonTemplate = parser.parse(new FileReader(new File("templates\\botany_pots.json"))).getAsJsonObject();
            JsonConversionTemplate template = new JsonConversionTemplate("output", jsonTemplate);
            JsonObject output = template.apply(new AgriJson(input, "vanilla", AgriJsonType.PLANT));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File directory = template.getOutputDir();
            if(!directory.exists()) {
                directory.mkdirs();
            }
            File outputFile = new File(template.getOutputDir(), "wheat.json");
            if(outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            FileWriter writer = new FileWriter(outputFile);
            gson.toJson(output, writer);
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void testGithubFetcher() {
        // temporary test implementation
        try {
            GitHubFetcher fetcher = new GitHubFetcher();
            Map<AgriJsonType, List<AgriJson>> jsons = fetcher.fetchJsons();
            boolean debug = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
