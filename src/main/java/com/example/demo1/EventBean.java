package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named(value = "EventBean")
@RequestScoped
public class EventBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public static class Event {
        int id;
        String NAME;
        String DESCRIPTION;
        String Date;
        int PRICE;

        int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getDESCRIPTION() {
            return DESCRIPTION;
        }

        public void setDESCRIPTION(String DESCRIPTION) {
            this.DESCRIPTION = DESCRIPTION;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public int getPRICE() {
            return PRICE;
        }

        public void setPRICE(int PRICE) {
            this.PRICE = PRICE;
        }
    }

    Event eventItem = new Event();

    public Event getEventItem() {
        return eventItem;
    }

    public void setEventItem(Event eventItem) {
        this.eventItem = eventItem;
    }

    public EventBean() throws IOException, URISyntaxException, InterruptedException {
        setLists();
    }

    List<Event> futureEvents = new ArrayList<>();
    List<Event> todayEvents = new ArrayList<>();

    public List<Event> getFutureEvents() {
        return futureEvents;
    }

    public void setFutureEvents(List<Event> futureEvents) {
        this.futureEvents = futureEvents;
    }

    public List<Event> getTodayEvents() {
        return todayEvents;
    }

    public void setTodayEvents(List<Event> todayEvents) {
        this.todayEvents = todayEvents;
    }


    public void setLists() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Event[] list_arr = objectMapper.readValue(getJSON(), Event[].class);
        List<Event> arr = new ArrayList<>(Arrays.asList(list_arr));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        for (Event i :
                arr) {
            if (i.Date.equals(formatter.format(date))) {
                todayEvents.add(i);
            } else {
                futureEvents.add(i);
            }

        }

    }

    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event"))
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
