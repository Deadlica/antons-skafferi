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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Named(value = "LunchBean")
@RequestScoped
public class LunchBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();
    private String week = "currentWeek";

    private List<String> dates = new ArrayList<>();

    private LunchItem lunchItem = new LunchItem();

    private List<Dish> foodItems;


    public static class LunchItem {

        public String date;
        String description;
        Dish dish = new Dish();
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

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

    }

    public LunchBean() throws IOException, URISyntaxException, InterruptedException {
        FoodItemsBean temp = new FoodItemsBean();
        foodItems = temp.getList();
        setDates();
        setLists();
    }

    List<LunchItem> monday = new ArrayList<>();
    List<LunchItem> tuesday = new ArrayList<>();
    List<LunchItem> wednesday = new ArrayList<>();
    List<LunchItem> thursday = new ArrayList<>();
    List<LunchItem> friday = new ArrayList<>();
    List<LunchItem> nextMonday = new ArrayList<>();
    List<LunchItem> nextTuesday = new ArrayList<>();
    List<LunchItem> nextWednesday = new ArrayList<>();
    List<LunchItem> nextThursday = new ArrayList<>();
    List<LunchItem> nextFriday = new ArrayList<>();

    private boolean isNextWeekLunchSet(List<LunchItem> arr, ArrayList<String> nextWeek) {
        for (LunchItem i : arr) {
            for (String nextWeekDay : nextWeek) {
                if (i.date.equals(nextWeekDay)) {
                    return true;
                }
            }
        }
        return false;
    }

    public HttpResponse<String> putItem(LunchItem item, List<LunchItem> list) throws IOException, InterruptedException {
        list.remove(item);
        return API.doPut("lunch", item);
    }

    public HttpResponse<String> postItem(String date) throws IOException, InterruptedException {
        lunchItem.dish = getDishFromId(lunchItem.dish.getId());
        lunchItem.date = date;
        HttpResponse<String> toReturn = API.doPost("lunch", lunchItem);
        lunchItem.description = "";
        lunchItem.price = 0;
        return toReturn;
    }

    public Dish getDishFromId(int id) {
        for (Dish i : foodItems) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public void setLists() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        LunchItem[] list_arr = objectMapper.readValue(getJSON(), LunchItem[].class);
        List<LunchItem> arr = new ArrayList<>(Arrays.asList(list_arr));
        /*DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        }*/


        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String thisMonday = (LocalDateTime.now().with(DayOfWeek.MONDAY)).format(format);
        String thisTuesday = (LocalDateTime.now().with(DayOfWeek.TUESDAY)).format(format);
        String thisWednesday = (LocalDateTime.now().with(DayOfWeek.WEDNESDAY)).format(format);
        String thisThursday = (LocalDateTime.now().with(DayOfWeek.THURSDAY)).format(format);
        String thisFriday = (LocalDateTime.now().with(DayOfWeek.FRIDAY)).format(format);

        String nextMonday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))).format(format);
        String nextTuesday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY))).format(format);
        String nextWednesday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))).format(format);
        String nextThursday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY))).format(format);
        String nextFriday = (LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))).format(format);
        ArrayList<String> nextWeek = new ArrayList<>(Arrays.asList(nextMonday, nextTuesday, nextWednesday, nextThursday, nextFriday));
        if ((LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY)
                | LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY))
                && isNextWeekLunchSet(arr, nextWeek)) {
            thisMonday = nextMonday;
            thisTuesday = nextTuesday;
            thisWednesday = nextWednesday;
            thisThursday = nextThursday;
            thisFriday = nextFriday;
            setWeek("nextWeek");
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
            if (i.date.equals(dates.get(5))) {
                this.nextMonday.add(i);
            }
            if (i.date.equals(dates.get(6))) {
                this.nextTuesday.add(i);
            }
            if (i.date.equals(dates.get(7))) {
                this.nextWednesday.add(i);
            }
            if (i.date.equals(dates.get(8))) {
                this.nextThursday.add(i);
            }
            if (i.date.equals(dates.get(9))) {
                this.nextFriday.add(i);
            }
        }

    }

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/lunch"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public List<LunchItem> getNextMonday() {
        return nextMonday;
    }

    public void setNextMonday(List<LunchItem> nextMonday) {
        this.nextMonday = nextMonday;
    }

    public List<LunchItem> getNextTuesday() {
        return nextTuesday;
    }

    public void setNextTuesday(List<LunchItem> nextTuesday) {
        this.nextTuesday = nextTuesday;
    }

    public List<LunchItem> getNextWednesday() {
        return nextWednesday;
    }

    public void setNextWednesday(List<LunchItem> nextWednesday) {
        this.nextWednesday = nextWednesday;
    }

    public List<LunchItem> getNextThursday() {
        return nextThursday;
    }

    public void setNextThursday(List<LunchItem> nextThursday) {
        this.nextThursday = nextThursday;
    }

    public List<LunchItem> getNextFriday() {
        return nextFriday;
    }

    public void setNextFriday(List<LunchItem> nextFriday) {
        this.nextFriday = nextFriday;
    }

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

    public String getWeek() {
        return week;
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

    public void setWeek(String week) {
        this.week = week;
    }

    public List<String> getDates() {
        return dates;
    }

    public List<Dish> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Dish> foodItems) {
        this.foodItems = foodItems;
    }

    public void setDates() {
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = startDate.plusDays(12);
        List<LocalDate> templist = startDate.datesUntil(endDate).collect(Collectors.toList());
        templist.remove(5);
        templist.remove(5);
        for (LocalDate date : templist) {
            dates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    public LunchItem getLunchItem() {
        return lunchItem;
    }

    public void setLunchItem(LunchItem lunchItem) {
        this.lunchItem = lunchItem;
    }
}
