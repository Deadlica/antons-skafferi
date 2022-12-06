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

@Named(value = "BookingBean")
@RequestScoped
public class BookingBean implements Serializable {

public static class infoBooking {
    String date = "0000-00-00";
    String firstName;
    int id=1;
    String lastName;


    int numberOfPeople;

    String phoneNumber;

    int tableNumber=1;
    String time;



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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


    String receivingMessage;
infoBooking infobooking=new infoBooking();
    public BookingBean() throws IOException, URISyntaxException, InterruptedException {
    }

    public infoBooking getInfobooking() {
        return infobooking;
    }

    public void setInfobooking(infoBooking infobooking) {
        this.infobooking = infobooking;
    }



    public String getReceivingMessage() {
        return receivingMessage;
    }

    public void setReceivingMessage(String receivingMessage) {
        this.receivingMessage = receivingMessage;
    }

    public void makeBooking() throws URISyntaxException, IOException, InterruptedException {

        if (verifyInputs()){
            addBooking();
            receivingMessage="bokat!";
            infobooking.date="0000-00-00";
            return ;
        }
        receivingMessage="inputs är ogilltiga!";
      return;
    }

private boolean verifyInputs(){


        if (infobooking.phoneNumber.length()==0 || infobooking.firstName.length()==0 || infobooking.date.equals("0000-00-00")|| infobooking.numberOfPeople==0|| infobooking.time.length()==0){
            return false;
        }
        return true;
}


    private HttpResponse<String> addBooking() throws URISyntaxException, IOException, InterruptedException {


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(infobooking);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/booking"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

}
