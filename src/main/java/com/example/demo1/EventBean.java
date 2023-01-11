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
import java.util.function.Supplier;

@Named(value = "EventBean")
@RequestScoped
//@MultipartConfig(location = "src/main/webapp/resources/images/UPLOAD_DIRECTORY")
public class EventBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public Part getImage() {
        return eventImage;
    }

    public void setImage(Part image) throws IOException, URISyntaxException, InterruptedException {
        eventImage = image;
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
        String filename = name.replace(" ", "-");
        filename = filename.toLowerCase();
        filename += date + ".jpg";
        return filename;
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
        todayEvents.clear();
        futureEvents.clear();
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
        addEvent();
        if (eventImage != null) {
            uploadImage();
        }
        setLists();
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

    public static class ImageContent {
        private byte[] imageBytes;
        private String imageName;

        public byte[] getImageBytes() {
            return imageBytes;
        }

        public void setImageBytes(byte[] imageBytes) {
            this.imageBytes = imageBytes;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }
    }

    private HttpResponse<String> uploadImage() throws URISyntaxException, IOException, InterruptedException {
        ImageContent imageContent = new ImageContent();
        imageContent.setImageName(getImageName(eventItem.NAME, eventItem.Date));
        imageContent.setImageBytes(eventImage.getInputStream().readAllBytes());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(imageContent);
        response = json;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event/upload"))
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
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).id == id) {
                EventBean.Event e = allEvents.get(i);
                response = String.valueOf(removeEvent(e));
                temp = getImageName(e.NAME, e.Date);
                allEvents.remove(i);
                break;
            }
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/event/image"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "text/plain")
                .PUT(HttpRequest.BodyPublishers.ofString(temp))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
        return response;


    }
}


