package com.agricraft.agrijsonutilities.github;

import com.agricraft.agrijsonutilities.util.AgriJson;
import com.agricraft.agrijsonutilities.util.AgriJsonType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class GitHubFetcher {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    private static final String GITHUB_API_AGRIPLANTS_REPOSITORY = "repos/AgriCraft/AgriPlants/contents/";

    public GitHubFetcher() {}

    public Map<AgriJsonType, List<AgriJson>> fetchJsons() throws IOException {
        Map<AgriJsonType, List<AgriJson>> jsons = Maps.newEnumMap(AgriJsonType.class);
        JsonArray contents = this.queryGithubApi(GITHUB_API_BASE_URL + GITHUB_API_AGRIPLANTS_REPOSITORY);
        this.fetchFromDirRecursive(contents, jsons);
        return jsons;
    }

    protected void fetchFromDirRecursive(JsonArray contents, Map<AgriJsonType, List<AgriJson>> jsons) throws IOException {
        for(JsonElement element : contents) {
            FileType fileType = this.getFileType(element);
            if(fileType.isDir()) {
                System.out.println("SCANNING: " + element.getAsJsonObject().get("path").getAsString());
                String url = element.getAsJsonObject().get("url").getAsString();
                this.fetchFromDirRecursive(this.queryGithubApi(url), jsons);
            } else if(fileType.isJson()) {
                System.out.println("DOWNLOADING: " + element.getAsJsonObject().get("path").getAsString());
                AgriJsonType jsonType = fileType.getType();
                String path = element.getAsJsonObject().get("path").getAsString();
                String subDir = path.substring(0, path.indexOf("_"));
                JsonObject json = this.downloadJson(element.getAsJsonObject().get("download_url").getAsString());
                jsons.computeIfAbsent(jsonType, type -> Lists.newArrayList()).add(
                        new AgriJson(json, subDir, jsonType)
                );
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
        Request request = Request.Get(url);
        Content content = request.execute().returnContent();
        InputStreamReader reader = new InputStreamReader(content.asStream());
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(reader).getAsJsonObject();
        reader.close();
        return object;
    }

    protected JsonArray queryGithubApi(String query) throws IOException {
        Request request = Request.Get(query);
        Content content = request.execute().returnContent();
        String jsonString = content.asString();
        return new JsonParser().parse(jsonString).getAsJsonArray();
    }
}
