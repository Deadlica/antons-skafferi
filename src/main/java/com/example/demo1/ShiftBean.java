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

@Named(value = "ShiftBean")
@RequestScoped
public class ShiftBean implements Serializable {
    public static class Shift{
        private int id;
        private String date;
        private String beginTime;
        private String endTime;
        private StaffBean.Employee employee;

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

        public StaffBean.Employee getEmployee() {
            return employee;
        }

        public void setEmployee(StaffBean.Employee employee) {
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

    public String getJSONLunchShift(String date) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift/lunch?date=" + date))
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
        for(ShiftBean.Shift i : arr){
            lunchShift.add(i);
        }
        return lunchShift;
    }

    public String getJSONDinnerShift(String date) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift/dinner?date=" + date))
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
        for(ShiftBean.Shift i : arr){
            dinnerShift.add(i);
        }
        return dinnerShift;
    }

}
