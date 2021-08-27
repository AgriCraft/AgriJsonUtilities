package com.agricraft.agrijsonutilities;

import com.agricraft.agrijsonutilities.github.GitHubFetcher;
import com.agricraft.agrijsonutilities.json.AgriJsonOutput;
import com.agricraft.agrijsonutilities.json.JsonConversionTemplate;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenerateRecipeJsons {
    public static void main(String[] args) {
        try {
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Config config = new Config(args);
            new GitHubFetcher(parser, config.getToken()).processTemplates(loadTemplates(parser), config.getModId()).forEach(pair -> {
                try {
                    writeOutput(gson, pair.getA(), pair.getB());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println("ENCOUNTERED AN ERROR, TERMINATING");
            e.printStackTrace();
        }
    }

    protected static List<JsonConversionTemplate> loadTemplates(JsonParser parser) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("templates");
        if (url == null) {
            return Collections.emptyList();
        }
        File[] files = new File(url.getPath()).listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        FileReader reader = new FileReader(file);
                        JsonObject template = parser.parse(reader).getAsJsonObject();
                        reader.close();
                        return new JsonConversionTemplate("output", template);
                    } catch (Exception e) {
                        System.out.println("Failed to parse template: " + file);
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected static void writeOutput(Gson gson, AgriJson source, AgriJsonOutput output) throws IOException {
        String name = source.getOriginalPath().substring(
                source.getOriginalPath().lastIndexOf('/') + 1,
                source.getOriginalPath().lastIndexOf('_'));
        String subDir = source.getOriginalPath().substring(0, source.getOriginalPath().indexOf('/')).replace("mod_", "");
        File directory = new File(new File(output.getPath(), subDir), "plants");
        if(directory.exists() || directory.mkdirs()) {
            File outputFile = new File(directory, name + ".json");
            if(!outputFile.exists() || outputFile.delete()) {
                if(outputFile.createNewFile()) {
                    FileWriter writer = new FileWriter(outputFile);
                    gson.toJson(output.getJson(), writer);
                    writer.close();
                } else {
                    throw new IOException("Failed to create output file: " + outputFile);
                }
            } else {
                throw new IOException("Failed to locate output file: " + outputFile);
            }
        } else {
            throw new IOException("Failed to locate output directory: " + directory);
        }
    }

    private static class Config {
        private String token;
        private String modid;

        private Config(String[] args) {
            Arrays.stream(args).forEach(arg -> {
                String[] split = arg.split("=");
                if(split.length < 2) {
                    System.out.println("Skipping invalid argument: " + arg);
                    return;
                }
                try {
                    this.getClass().getDeclaredField(split[0]).set(this, split[1]);
                } catch (Exception e) {
                    System.out.println("Skipping invalid argument: " + arg);
                    e.printStackTrace();
                }
            });
        }

        @SuppressWarnings("unused")
        public Config setToken(@Nullable String token) {
            this.token = token;
            return this;
        }

        @SuppressWarnings("unused")
        public Config setModId(@Nullable String modId) {
            this.modid = modId;
            return this;
        }

        @Nullable
        public String getToken() {
            return this.token;
        }

        @Nullable
        public String getModId() {
            return this.modid;
        }

    }
}
