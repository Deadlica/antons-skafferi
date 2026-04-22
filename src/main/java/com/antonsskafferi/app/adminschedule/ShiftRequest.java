package com.antonsskafferi.app.adminschedule;

import com.antonsskafferi.app.API;
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
import java.util.List;

public class ShiftRequest {
    private static volatile ShiftRequest instance;
    private final ObjectMapper mapper = new ObjectMapper();

    private ShiftRequest() {}

    public static ShiftRequest getInstance() {
        ShiftRequest result = instance;
        if (result == null) {
            synchronized (ShiftRequest.class) {
                result = instance;
                if (result == null) instance = result = new ShiftRequest();
            }
        }
        return result;
    }

    private String get(String path) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest req = HttpRequest.newBuilder().uri(new URI(API.link + path)).GET().build();
        return HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build()
                .send(req, HttpResponse.BodyHandlers.ofString()).body();
    }

    public List<Employee> fetchEmployees() throws IOException, URISyntaxException, InterruptedException {
        return new ArrayList<>(Arrays.asList(mapper.readValue(get("employee/working"), Employee[].class)));
    }

    public List<Shift> getShiftBetween(String startDate, String stopDate) throws IOException, URISyntaxException, InterruptedException {
        return new ArrayList<>(Arrays.asList(mapper.readValue(
                get("shift/range?startDate=" + startDate + "&endDate=" + stopDate), Shift[].class)));
    }

    public String addShift(Shift shift) throws IOException, InterruptedException {
        return API.doPost("shift", shift).body();
    }

    public String deleteShift(Shift shift) throws IOException, InterruptedException {
        return API.doPut("shift", shift).body();
    }

    public String deleteStaff(Employee e) throws IOException, InterruptedException {
        return API.doPut("employee", e).body();
    }

    public boolean isRetired(String ssn) throws IOException, URISyntaxException, InterruptedException {
        Employee[] arr = mapper.readValue(get("employee/retired"), Employee[].class);
        for (Employee i : arr) if (i.getSsn().equals(ssn)) return true;
        return false;
    }

    public String updateStaff(Employee e) throws IOException, InterruptedException {
        return API.doPut("employee/update", e).body();
    }

    public String addStaff(Employee e) throws IOException, URISyntaxException, InterruptedException {
        return (isRetired(e.getSsn()) ? API.doPut("employee/unretire", e) : API.doPost("employee", e)).body();
    }
}
