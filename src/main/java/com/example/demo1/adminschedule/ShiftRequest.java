package com.example.demo1.adminschedule;

import com.example.demo1.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ShiftRequest {
    private final URL location = new URL();
    private final String link = location.getLink();
    private static volatile ShiftRequest instance;

    private ShiftRequest() throws URISyntaxException {
    }

    public static ShiftRequest getInstance() throws URISyntaxException {
        ShiftRequest result = instance;
        if(result == null){
            synchronized (ShiftRequest.class){
                result = instance;
                if(result == null){
                    instance = result = new ShiftRequest();
                }
            }
        }
        return result;
    }
    public String getJSONEmployees() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/working"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public List<Employee> fetchEmployees() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Employee[] list_arr = objectMapper.readValue(getJSONEmployees(), Employee[].class);
        List<Employee> arr = new ArrayList<>(Arrays.asList(list_arr));
        return new ArrayList<>(arr);
    }
    public String fetchShiftBetween(String startDate, String stopDate) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift/range?startDate=" + startDate + "&endDate=" + stopDate))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public List<Shift> getShiftBetween(String startDate, String stopDate) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Shift[] list_arr = objectMapper.readValue(fetchShiftBetween(startDate, stopDate), Shift[].class);
        List<Shift> arr = new ArrayList<>(Arrays.asList(list_arr));
        return new ArrayList<>(arr);
    }
    public String addShift(Shift shift) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + shift.getBeginTime() + "\",\"date\":\"" + shift.getDate() + "\",\"employee\":{\"email\":\"" + shift.getEmployee().getEmail() + "\",\"firstName\":\"" + shift.getEmployee().getFirstName() + "\",\"lastName\":\"" + shift.getEmployee().getLastName() + "\",\"phoneNumber\":\"" + shift.getEmployee().getPhoneNumber() + "\", \"ssn\":\"" + shift.getEmployee().getSsn() + "\"},\"endTime\":\"" + shift.getEndTime() + "\",\"id\":1}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public String deleteShift(Shift shift) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + shift.getBeginTime() + "\",\"date\":\"" + shift.getDate() + "\",\"employee\":{\"email\":\"" + shift.getEmployee().getEmail() + "\",\"firstName\":\"" + shift.getEmployee().getFirstName() + "\",\"lastName\":\"" + shift.getEmployee().getLastName() + "\",\"phoneNumber\":\"" + shift.getEmployee().getPhoneNumber() + "\", \"ssn\":\"" + shift.getEmployee().getSsn() + "\"},\"endTime\":\"" + shift.getEndTime() + "\",\"id\":" + shift.getId() + "}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String deleteStaff(Employee selectedDeletedEmployee) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedDeletedEmployee.getEmail() + "\",\"firstName\":\"" + selectedDeletedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedDeletedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedDeletedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedDeletedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getRetiredEmployees() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/retired"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public boolean isRetired(String ssn) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Employee[] list_arr = objectMapper.readValue(getRetiredEmployees(), Employee[].class);
        List<Employee> arr = new ArrayList<>(Arrays.asList(list_arr));

        for(Employee i : arr) {
            if (i.getSsn().contains(ssn)) {
                return true;
            }
        }
        return false;
    }

    public String updateStaff(Employee selectedEditedEmployee) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/update"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedEditedEmployee.getEmail() + "\",\"firstName\":\"" + selectedEditedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedEditedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedEditedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedEditedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
    public String addStaff(Employee newEmployee) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        if (isRetired(newEmployee.getSsn())) {
            request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/unretire"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
        } else {
            request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
        }
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
