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

@Named(value = "CarteBean")
@RequestScoped
public class CarteBean implements Serializable {

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

        public Dish() {
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class CarteItem {
        String category;
        String description;
        Dish dish;
        int price;

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
    }

    public CarteBean() throws IOException, URISyntaxException, InterruptedException {
        setLists();
    }

    List<CarteItem> starters = new ArrayList<>();
    List<CarteItem> mainCourses = new ArrayList<>();
    List<CarteItem> desserts = new ArrayList<>();
    List<CarteItem> drinks = new ArrayList<>();

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

    public void setLists() throws IOException, URISyntaxException, InterruptedException {
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
                case "Dessert":
                    desserts.add(i);
                    break;
                case "Dryck":
                    drinks.add(i);
                    break;
            }

        }
    }

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/carte"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


}

