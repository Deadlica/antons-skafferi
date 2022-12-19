package com.example.demo1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.faces.view.ViewScoped;
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

@Named(value = "CarteBean")
@ViewScoped
public class CarteBean implements Serializable {
    CarteItem carteItem = new CarteItem();
    List<Dish> allDishes = new ArrayList<>();


    List<Dish> unusedDishes = new ArrayList<>();
    List<CarteItem> starters = new ArrayList<>();
    List<CarteItem> mainCourses = new ArrayList<>();
    List<CarteItem> desserts = new ArrayList<>();
    List<CarteItem> drinks = new ArrayList<>();
    URI uri;

    {
        try {
            uri = new URI("http://" + new URL().getLink() + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/carte");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static class CarteItem {
        String category = "Förrätt";
        String description = "";
        Dish dish = new Dish();
        int price = 0;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Dish getDish() {
            return dish;
        }

        public void setDish(Dish dish) {
            this.dish = dish;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String json;
            try {
                json = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return json;
        }

    }

    public CarteBean() throws IOException, InterruptedException, URISyntaxException {
        setLists();
        setAllDishes();
        APIUnusedDishes();
    }


    public void setLists() throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        CarteItem[] list_arr = objectMapper.readValue(getJSON(), CarteItem[].class);
        List<CarteItem> arr = new ArrayList<>(Arrays.asList(list_arr));

        for (CarteItem i :
                arr) {
            switch (i.category) {
                case "Förrätt":
                    starters.add(i);
                    break;
                case "Varmrätt":
                    mainCourses.add(i);
                    break;
                case "Efterrätt":
                    desserts.add(i);
                    break;
                case "Dryck":
                    drinks.add(i);
                    break;
            }

        }
    }

    public void dishAttributesFromId(int id) {
        carteItem.dish = dishFromId(id);
    }

    public Dish dishFromId(int id) {
        for (Dish d : allDishes) {
            if (d.getId() == id) {
                carteItem.dish = d;
                return d;
            }
        }
        return null;
    }


    public String getJSON() throws IOException, InterruptedException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    static class idPOD {
        int id;

        public idPOD(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public void deleteId(int id, List<CarteItem> list) {
        for (CarteItem i : list) {
            if (i.dish.getId() == id) {
                list.remove(i);
                break;
            }
        }
    }

    public void APIUnusedDishes() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + new URL().getLink() + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish/available"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        Dish[] list_arr = objectMapper.readValue(response.body(), Dish[].class);
        unusedDishes = new ArrayList<>(Arrays.asList(list_arr));
    }

    public HttpResponse<String> deleteItem(int id) throws IOException, InterruptedException {
        idPOD object = new idPOD(id);
        unusedDishes.add(dishFromId(id));
        HttpResponse<String> response = API.doPut("carte", object);
        deleteId(id, starters);
        deleteId(id, mainCourses);
        deleteId(id, desserts);
        deleteId(id, drinks);
        return response;
    }

    public HttpResponse<String> addItem() throws IOException, InterruptedException {
        dishAttributesFromId(carteItem.dish.getId());
        for (Dish d : unusedDishes) {
            if (d.getId() == carteItem.dish.getId()) {
                unusedDishes.remove(d);
                break;
            }
        }
        HttpResponse<String> response = API.doPost("carte", carteItem);
        resetLists();
        return response;
    }

    public void resetLists() throws IOException, InterruptedException {
        starters.clear();
        mainCourses.clear();
        desserts.clear();
        drinks.clear();
        setLists();
    }

    //-----------------Getters and Setters-----------------//
    public List<Dish> getUnusedDishes() {
        return unusedDishes;
    }

    public void setUnusedDishes(List<Dish> unusedDishes) {
        this.unusedDishes = unusedDishes;
    }

    public List<Dish> getAllDishes() {
        return allDishes;
    }

    public void setAllDishes() throws IOException, InterruptedException {
        FoodItemsBean fib = new FoodItemsBean();
        allDishes = fib.getList();
    }

    public CarteItem getCarteItem() {
        return carteItem;
    }

    public void setCarteItem(CarteItem carteItem) {
        this.carteItem = carteItem;
    }

    public List<CarteItem> getStarters() {
        return starters;
    }

    public void setStarters(List<CarteItem> starters) {
        this.starters = starters;
    }

    public List<CarteItem> getMainCourses() {
        return mainCourses;
    }

    public void setMainCourses(List<CarteItem> mainCourses) {
        this.mainCourses = mainCourses;
    }

    public List<CarteItem> getDesserts() {
        return desserts;
    }

    public void setDesserts(List<CarteItem> desserts) {
        this.desserts = desserts;
    }

    public List<CarteItem> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<CarteItem> drinks) {
        this.drinks = drinks;
    }


}

