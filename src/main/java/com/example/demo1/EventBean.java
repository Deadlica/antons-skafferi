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

    public String getImageName(String name, String date) {
        return name + date;
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
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event/upcoming"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    String response;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void addNewEvent() throws URISyntaxException, IOException, InterruptedException {
        response= String.valueOf(addEvent())+ " "+eventItem.NAME+" "+eventItem.Date+" "+eventItem.DESCRIPTION;
    }
    private HttpResponse<String> addEvent() throws URISyntaxException, IOException, InterruptedException {


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(eventItem);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }
    public void removeEvent(int id) throws URISyntaxException, IOException, InterruptedException {
        for (Event i : todayEvents) {
            if (i.id==id){
                response= String.valueOf(removeEvent(i));
                ObjectMapper objectMapper = new ObjectMapper();
            }
        }
        for (Event i : futureEvents) {
            if (i.id==id){
                response= String.valueOf(removeEvent(i));
                ObjectMapper objectMapper = new ObjectMapper();
            }
        }


    }

    private HttpResponse<String> removeEvent(Event removeEvent) throws URISyntaxException, IOException, InterruptedException  {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(removeEvent);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;


    }
}


