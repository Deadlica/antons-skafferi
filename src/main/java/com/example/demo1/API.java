package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {
    private static final URI link; // Home

    static {
        try {
            // link = new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/"); // School
            link = new URI("http://89.233.229.182:8080/antons-skafferi-db-1.0-SNAPSHOT/api/"); // Home
            // link = new URI("http://31.209.47.252:8080/antons-skafferi-db-1.0-SNAPSHOT/api/"); // Can
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //this.link = "31.209.47.252"; //Can
    public static HttpResponse<String> doPost(String apiEndpoint, Object obj) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(link + apiEndpoint))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> doPut(String apiEndpoint, Object obj) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(link + apiEndpoint))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
