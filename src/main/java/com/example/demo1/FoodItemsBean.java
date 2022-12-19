package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Named(value = "FoodItemsBean")
@RequestScoped
public class FoodItemsBean implements Serializable {
    public List<Dish> getList() {
        return list;
    }

    public void setList(List<Dish> list) {
        this.list = list;
    }

    public FoodItemsBean() throws IOException, InterruptedException {
        addList();
    }

    private List<Dish> list;
    private String name;
    private String category = "";

    private int id;


    private final URI uri;

    {
        try {
            uri = new URI("http://" + API.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish");
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
        HttpResponse<String> response = API.doPost("dish", tempDish);
        list.clear();
        addList();
        return response;
    }

    public HttpResponse<String> putItem(int id) throws IOException, InterruptedException {
        Dish tempDish = new Dish(id, "");
        HttpResponse<String> response = API.doPut("dish", tempDish);
        for (Dish dish : list) {
            if (dish.getId() == id) {
                list.remove(dish);
                break;
            }
        }
        return response;
    }

    //-----------------------------------------------------------------------------------------------
    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
