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

    public CarteBean() throws IOException, InterruptedException, URISyntaxException {
        setLists();
    }

    URI uri;

    {
        try {
            // uri = new URI("http://89.233.229.182:8080/antons-skafferi-db-1.0-SNAPSHOT/api/carte");
            uri = new URI("http://" + new URL().getLink() + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/carte");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public CarteItem getCarteItem() {
        return carteItem;
    }

    public void setCarteItem(CarteItem carteItem) {
        this.carteItem = carteItem;
    }

    CarteItem carteItem = new CarteItem();
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

    public void setLists() throws IOException, InterruptedException, URISyntaxException {
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

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
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

    public HttpResponse<String> deleteItem(int id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"id\":" + id + " }"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }


    public HttpResponse<String> addItem(CarteItem carteItem) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(carteItem);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        starters.clear();
        mainCourses.clear();
        desserts.clear();
        drinks.clear();
        setLists();
        return response;
    }
}

