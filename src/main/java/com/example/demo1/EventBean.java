package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import jakarta.validation.Path;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named(value = "EventBean")
@RequestScoped
@MultipartConfig(location = "src/main/webapp/resources/images/UPLOAD_DIRECTORY")
public class EventBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public Part getImage() {
        return eventImage;
    }

    public void setImage(Part image) throws IOException {
        eventImage = image;
        String fileName = eventItem.NAME + eventItem.Date + ".jpg";
        eventImage.write(fileName);
        Files.copy(Paths.get(System.getProperty("com.sun.aas.instanceRoot") + "/generated/jsp/antons-skafferi-1.0-SNAPSHOT/" + fileName), Paths.get("/Users/cankupeli/IdeaProjects/antons-skafferi/src/main/webapp/resources/images/" + fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    Part eventImage;


    public static class Event {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todaysDate = dateObj.format(formatter);
        int id;
        String NAME;
        String DESCRIPTION;
        String Date = todaysDate;
        int PRICE;

        public int getId() {
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

    List<Event> allEvents = new ArrayList<>();
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

    public List<Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(List<Event> allEvents) {
        this.allEvents = allEvents;
    }

    public List<Event> getTodayEvents() {
        return todayEvents;
    }

    public void setTodayEvents(List<Event> todayEvents) {
        this.todayEvents = todayEvents;
    }


    public void setLists() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Event[] list_arr = objectMapper.readValue(getFUTURE(), Event[].class);
        Event[] list_arr2 = objectMapper.readValue(getALL(), Event[].class);
        List<Event> arr = new ArrayList<>(Arrays.asList(list_arr));
        allEvents = new ArrayList<>(Arrays.asList(list_arr2));

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

    public String getFUTURE() throws IOException, InterruptedException, URISyntaxException {
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

    public String getALL() throws IOException, InterruptedException, URISyntaxException {
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

    String response = new String();

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
        response += String.valueOf(addEvent()) + " " + eventItem.NAME + " " + eventItem.Date + " " + eventItem.DESCRIPTION + " " + eventImage;
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
        String temp = "";
        for (Event i : allEvents) {
            if (i.id == id) {
                response = String.valueOf(removeEvent(i));
                ObjectMapper objectMapper = new ObjectMapper();
                temp = i.NAME + i.Date;
            }
        }

        File eventImageFile = new File("/Users/cankupeli/IdeaProjects/antons-skafferi/src/main/webapp/resources/images/" + temp + ".jpg");
        if (eventImageFile.exists()) {
            eventImageFile.delete();
        }
    }

    private HttpResponse<String> removeEvent(Event removeEvent) throws URISyntaxException, IOException, InterruptedException {
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


