package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named(value = "FoodItemsBean")
@RequestScoped
public class FoodItemsBean implements Serializable {
    public static class Dish {
        private int id;
        private String name;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public List<Dish> getList() {
        return list;
    }

    public void setList(List<Dish> list) {
        this.list = list;
    }

    public FoodItemsBean() throws IOException, URISyntaxException, InterruptedException {
        addList();
    }

    private List<Dish> list;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addList() throws IOException, InterruptedException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        Dish[] list_arr = objectMapper.readValue(getJSON(), Dish[].class);
        setList(new ArrayList<>(Arrays.asList(list_arr)));
    }

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        // Sam http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public HttpResponse<String> addDish(String name) throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"id\": 1, \"name\": \"" + name + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

}
