package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.json.JsonObject;

import java.io.*;
import java.net.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Named(value = "FoodItemsBean")
@RequestScoped
public class FoodItemsBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

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
    private String category = "";

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    URI uri;

    {
        try {
            uri = new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish");
            //uri = new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public void setName(String name) {
        this.name = name;
    }

    public void addList() throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Dish[] list_arr = objectMapper.readValue(getJSON(), Dish[].class);
        setList(new ArrayList<>(Arrays.asList(list_arr)));
    }

    public String getJSON() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public HttpResponse<String> addDish(String name, String type) throws IOException, InterruptedException {
        Dish tempDish = new Dish(1, name);
        if (Objects.equals(type, "")) tempDish.setType("");
        else tempDish.setType(type);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tempDish);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        list.clear();
        addList();
        return response;
    }


    public HttpResponse<String> delItem(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri + "?id=" + id))
                .header("Content-Type", "application/json;charset=UTF-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

    public HttpResponse<String> putItem() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"id\":266, \"name\":\"Aubergine Tartare\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

}
