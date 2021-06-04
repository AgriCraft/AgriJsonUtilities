package com.agricraft.agrijsonutilities.github;

import com.agricraft.agrijsonutilities.json.AgriJsonOutput;
import com.agricraft.agrijsonutilities.json.JsonConversionTemplate;
import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.agricraft.agrijsonutilities.util.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class allows the scanning and downloading of AgriCraft jsons from the AgriPlants repository.
 * It uses the GitHub API to query repository contents using http requests
 * See: https://docs.github.com/en/rest/reference/repos#get-repository-content
 */
public class GitHubFetcher {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    private static final String GITHUB_API_RATE_QUERY = GITHUB_API_BASE_URL + "rate_limit";
    private static final String GITHUB_AGRIPLANTS_REPOSITORY = "repos/AgriCraft/AgriPlants/contents/";

    private final JsonParser parser;
    private final String token;

    public GitHubFetcher(JsonParser parser, @Nullable String token) {
        this.parser = parser;
        this.token = token;
    }

    public Map<AgriJsonType, List<AgriJson>> fetchJsons() throws IOException {
        Map<AgriJsonType, List<AgriJson>> jsons = Maps.newEnumMap(AgriJsonType.class);
        this.fetchFromDirRecursive(
                this.queryGithubApi(GITHUB_API_BASE_URL + GITHUB_AGRIPLANTS_REPOSITORY),
                (type) -> true,
                json -> jsons.computeIfAbsent(json.getType(), type -> Lists.newArrayList()).add(json)
        );
        return jsons;
    }

    public List<Pair<AgriJson, AgriJsonOutput>> processTemplates(List<JsonConversionTemplate> templates) throws IOException {
        if(templates.isEmpty()) {
            System.out.println("Received empty list of templates to process");
            return Collections.emptyList();
        }
        List<Pair<AgriJson, AgriJsonOutput>> output = Lists.newArrayList();
        this.fetchFromDirRecursive(
                this.queryGithubApi(GITHUB_API_BASE_URL + GITHUB_AGRIPLANTS_REPOSITORY),
                type -> templates.stream().map(JsonConversionTemplate::getSourceType).anyMatch(type::equals),
                json -> templates.forEach(template -> {
                    AgriJsonOutput result = new AgriJsonOutput(
                            template.apply(json),
                            template.getOutputDir()
                    );
                    output.add(Pair.of(json, result));
                })
        );
        return output;
    }

    protected void fetchFromDirRecursive(JsonArray contents, Predicate<AgriJsonType> filter, Consumer<AgriJson> consumer) throws IOException {
        for(JsonElement element : contents) {
            FileType fileType = this.getFileType(element);
            if(fileType.isDir()) {
                System.out.println("SCANNING: " + element.getAsJsonObject().get("path").getAsString());
                String url = element.getAsJsonObject().get("url").getAsString();
                this.fetchFromDirRecursive(this.queryGithubApi(url), filter, consumer);
            } else if(fileType.isJson()) {
                AgriJsonType jsonType = fileType.getType();
                String path = element.getAsJsonObject().get("path").getAsString();
                if(filter.test(jsonType)) {
                    System.out.println("DOWNLOADING: " + path);
                    JsonObject json = this.downloadJson(element.getAsJsonObject().get("download_url").getAsString());
                    consumer.accept(new AgriJson(json, path, jsonType));
                } else {
                    System.out.println("SKIPPING: " + path);
                }
            }
        }
    }

    protected FileType getFileType(JsonElement element) {
        if (!element.isJsonObject()) {
            // this shouldn't ever happen
            return FileType.INVALID;
        }
        JsonObject object = element.getAsJsonObject();
        if (!object.has("type")) {
            // skip root files (e.g. README, .GITIGNORE, etc.)
            return FileType.INVALID;
        }
        if(object.get("type").getAsString().equals("dir")) {
            if(object.has("name") && object.has("url")) {
                return FileType.DIR;
            } else {
                return FileType.INVALID;
            }
        }
        if(object.get("type").getAsString().equals("file")) {
            if(!object.has("name") || !object.has("path") || !object.has("download_url")) {
                return FileType.INVALID;
            }
            String name = object.get("name").getAsString();
            if(name.length() <= 5 || !name.endsWith(".json")) {
                return FileType.INVALID;
            }
            String typeString = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));
            try {
                switch (AgriJsonType.fromString(typeString)) {
                    case PLANT: return FileType.JSON_PLANT;
                    case SOIL: return FileType.JSON_SOIL;
                    case MUTATION: return FileType.JSON_MUTATION;
                    case WEED: return FileType.JSON_WEED;
                }
            } catch(Exception e) {
                return FileType.INVALID;
            }
        }
        return FileType.INVALID;
    }

    protected JsonObject downloadJson(String url) throws IOException {
        Request request = this.applyToken(Request.Get(url));
        Content content = request.execute().returnContent();
        InputStreamReader reader = new InputStreamReader(content.asStream());
        JsonObject object = this.parser.parse(reader).getAsJsonObject();
        reader.close();
        return object;
    }

    protected JsonArray queryGithubApi(String query) throws IOException {
        Request request = this.applyToken(Request.Get(query));
        Content content = request.execute().returnContent();
        String jsonString = content.asString();
        return this.parser.parse(jsonString).getAsJsonArray();
    }

    public JsonElement checkRateLimit() throws IOException {
        Request request = this.applyToken(Request.Get(GITHUB_API_RATE_QUERY));
        Content content = request.execute().returnContent();
        String jsonString = content.asString();
        return this.parser.parse(jsonString);
    }

    protected Request applyToken(Request request) {
        if(this.token == null) {
            return request;
        } else {
            return request.addHeader("Authorization", "token " + this.token);
        }
    }
}
