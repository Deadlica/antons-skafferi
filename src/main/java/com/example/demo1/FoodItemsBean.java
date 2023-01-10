package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
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
    private List<Dish> list;
    private List<String> categories = new ArrayList<>();
    String selectedCategory;
    private String name;
    private String category = "";
    private int id;
    private final URI uri;

    {
        try {
            uri = new URI(API.link + "dish");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public FoodItemsBean() throws IOException, InterruptedException {
        addList();
    }

    public void addList() throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Dish[] list_arr = objectMapper.readValue(getJSON(), Dish[].class);
        setList(new ArrayList<>(Arrays.asList(list_arr)));
        putCategories();
    }

    public void putCategories(){
        categories.add("Ny sort...");
        for(Dish d : list){
            if(!d.getType().isEmpty()){
                if(!categories.contains(d.getType())){
                    categories.add(d.getType());
                }
            }
        }
        selectedCategory = categories.get(0);
    }

    public boolean isOldCategory(){
        return !selectedCategory.contains("Ny sort...");
    }

    public void updateListener(ValueChangeEvent valueChangeEvent){
        selectedCategory = valueChangeEvent.getNewValue().toString();
    }

    public HttpResponse<String> addDish(String name, String type) throws IOException, InterruptedException {
        Dish tempDish = new Dish(1, name);
        if(isOldCategory()) type = selectedCategory;

        if (Objects.equals(type, "")) tempDish.setType("");
        else tempDish.setType(type);
        HttpResponse<String> response = API.doPost("dish", tempDish);

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin");
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

    //-----------------------------------------------------------------------------------------------
    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Dish> getList() {
        return list;
    }

    public void setList(List<Dish> list) {
        this.list = list;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
