package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.time.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Named(value = "LunchBean")
@RequestScoped
public class LunchBean implements Serializable {

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

    public static class LunchItem {
        public String date;
        String description;
        Dish dish;
        int price;

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

    public LunchBean() throws IOException, URISyntaxException, InterruptedException {
        setLists();
    }

    List<LunchItem> monday = new ArrayList<>();
    List<LunchItem> tuesday = new ArrayList<>();
    List<LunchItem> wednesday = new ArrayList<>();
    List<LunchItem> thursday = new ArrayList<>();
    List<LunchItem> friday = new ArrayList<>();

    public List<LunchItem> getMonday() {
        return monday;
    }

    public List<LunchItem> getTuesday() {
        return tuesday;
    }

    public List<LunchItem> getWednesday() {
        return wednesday;
    }

    public List<LunchItem> getThursday() {
        return thursday;
    }

    public List<LunchItem> getFriday() {
        return friday;
    }

    public void setMonday(List<LunchItem> monday) {
        this.monday = monday;
    }

    public void setTuesday(List<LunchItem> tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(List<LunchItem> wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(List<LunchItem> thursday) {
        this.thursday = thursday;
    }

    public void setFriday(List<LunchItem> friday) {
        this.friday = friday;
    }


    public void setLists() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        LunchItem[] list_arr = objectMapper.readValue(getJSON(), LunchItem[].class);
        List<LunchItem> arr = new ArrayList<>(Arrays.asList(list_arr));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String thisMonday;
        String thisTuesday;
        String thisWednesday;
        String thisThursday;
        String thisFriday;
        if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY) | LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            thisMonday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))).format(format);
            thisTuesday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY))).format(format);
            thisWednesday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))).format(format);
            thisThursday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY))).format(format);
            thisFriday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))).format(format);
        } else {
            thisMonday = (LocalDateTime.now().with(DayOfWeek.MONDAY)).format(format);
            thisTuesday = (LocalDateTime.now().with(DayOfWeek.TUESDAY)).format(format);
            thisWednesday = (LocalDateTime.now().with(DayOfWeek.WEDNESDAY)).format(format);
            thisThursday = (LocalDateTime.now().with(DayOfWeek.THURSDAY)).format(format);
            thisFriday = (LocalDateTime.now().with(DayOfWeek.FRIDAY)).format(format);
        }

        for (LunchItem i : arr) {
            if (i.date.equals(thisMonday)) {
                monday.add(i);
            }
            if (i.date.equals(thisTuesday)) {
                tuesday.add(i);
            }
            if (i.date.equals(thisWednesday)) {
                wednesday.add(i);
            }
            if (i.date.equals(thisThursday)) {
                thursday.add(i);
            }
            if (i.date.equals(thisFriday)) {
                friday.add(i);
            }
        }
    }

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        URL location = new URL();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + location.getLink() + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/lunch"))
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
