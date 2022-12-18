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
import java.util.HashMap;
import java.util.List;

@Named(value = "ShiftBean")
@RequestScoped
public class ShiftBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public ShiftBean() throws IOException, URISyntaxException, InterruptedException {

    }

    public static class Shift {
        private int id;
        private String date;
        private String beginTime;
        private String endTime;
        private Employee employee;

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
            this.date = date;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }
    }

    List<ShiftBean.Shift> lunchShift = new ArrayList<>();
    List<ShiftBean.Shift> dinnerShift = new ArrayList<>();

    public List<Shift> getLunchShift() {
        return lunchShift;
    }

    public void setLunchShift(List<Shift> lunchShift) {
        this.lunchShift = lunchShift;
    }

    public List<Shift> getDinnerShift() {
        return dinnerShift;
    }

    public void setDinnerShift(List<Shift> dinnerShift) {
        this.dinnerShift = dinnerShift;
    }
/*
    public HttpResponse<String> addShift(String ssn, boolean isLate, String date) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = isLate ? "16:00:00" : "14:00:00";
        String endTime = isLate ? "23:00:00" : "14:00:00";
        Employee emp = isLate ?
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\""+ beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\""+ emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() +"\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\""+ emp.getSsn() +"\"},\"endTime\":\"" + endTime + "\",\"id\":1}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }*/
    public String getJSONLunchShift(String date) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift/lunch?date=" + date))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public List<ShiftBean.Shift> setJSONLunchShift(String date) throws IOException, URISyntaxException, InterruptedException {
        lunchShift.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        ShiftBean.Shift[] list_arr = objectMapper.readValue(getJSONLunchShift(date), ShiftBean.Shift[].class);
        List<ShiftBean.Shift> arr = new ArrayList<>(Arrays.asList(list_arr));
        for (ShiftBean.Shift i : arr) {
            lunchShift.add(i);
        }
        return lunchShift;
    }

    public String getJSONDinnerShift(String date) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift/dinner?date=" + date))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public List<ShiftBean.Shift> setJSONDinnerShift(String date) throws IOException, URISyntaxException, InterruptedException {
        dinnerShift.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        ShiftBean.Shift[] list_arr = objectMapper.readValue(getJSONDinnerShift(date), ShiftBean.Shift[].class);
        List<ShiftBean.Shift> arr = new ArrayList<>(Arrays.asList(list_arr));
        for (ShiftBean.Shift i : arr) {
            dinnerShift.add(i);
        }
        return dinnerShift;
    }

}
