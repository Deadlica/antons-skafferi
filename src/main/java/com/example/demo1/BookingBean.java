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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named(value = "BookingBean")
@RequestScoped
public class BookingBean implements Serializable {
    public static class Amount{
        int size=0;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
    Amount holder= new Amount();

String receivingMessage;
    String phoneNumber;
        String firstName;
        String lastName;
        int numberOfPeople;
        int tableNumber;
        String time;
        String date= String.valueOf(LocalDate.now());;
        int id;

    public Amount getHolder() {
        return holder;
    }

    public void setHolder(Amount holder) {
        this.holder = holder;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = (date);
        }

    public String getReceivingMessage() {
        return receivingMessage;
    }

    public void setReceivingMessage(String receivingMessage) {
        this.receivingMessage = receivingMessage;
    }

    public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getNumberOfPeople() {
            return numberOfPeople;
        }

        public void setNumberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(int tableNumber) {
            this.tableNumber = tableNumber;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


    public void setLists() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        holder = objectMapper.readValue(getJSON(), Amount.class);
        //holder.size=4;
    }

    public int getAvailablePlaces(){
        return (5-holder.size);
    }




    public String getJSON() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/booking/count?date="+date))
                .GET()
                .build();

        //10.82.231.15
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void makeBooking() throws URISyntaxException, IOException, InterruptedException {
        if (holder.size==5){
            receivingMessage="Ingen ledig plats";
            return;
        }
        if (verifyInputs()){
            //addBooking();
            receivingMessage="bra input";
            return ;
        }
        receivingMessage="inputs är ogilltiga!";
      return;
    }

public boolean verifyInputs(){


        if (phoneNumber==null || firstName==null){
            return false;
        }
        return true;
}


    public HttpResponse<String> addBooking() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/booking"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"id\": DEFAULT, \"phoneNumber\": "+phoneNumber+ ",\"firtsname\": \""+firstName+"\",\"lastsname\": \""+firstName+"\",\"numberOfPeople\": "+numberOfPeople+",\"numberOfPeople\": "+numberOfPeople+",\"tableNumber\": DEFAULT, \"date\": "+date+",\"time\":"+time))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

}
