package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIOutput;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Named(value = "StaffBean")
@RequestScoped
public class StaffBean implements Serializable {
    public Employee getSelectedDeletedEmployee() {
        return selectedDeletedEmployee;
    }

    public void setSelectedDeletedEmployee(Employee selectedDeletedEmployee) {
        this.selectedDeletedEmployee = selectedDeletedEmployee;
    }

    private Employee selectedDeletedEmployee;

    private Employee selectedEditedEmployee;

    public Employee getSelectedEditedEmployee() {
        return selectedEditedEmployee;
    }

    public void setSelectedEditedEmployee(Employee selectedEditedEmployee) {
        this.selectedEditedEmployee = selectedEditedEmployee;
    }

    private Employee selectedShiftEmployee;

    public Employee getSelectedShiftEmployee() {
        return selectedShiftEmployee;
    }

    public void setSelectedShiftEmployee(Employee selectedShiftEmployee) {
        this.selectedShiftEmployee = selectedShiftEmployee;
    }

    private URL location = new URL();
    private String link = location.getLink();

    public StaffBean() throws IOException, URISyntaxException, InterruptedException {
        setJSONEmployees();
        selectedDeletedEmployee = employees.get(0);
        selectedEditedEmployee = employees.get(0);
    }

    Employee newEmployee = new Employee();

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }

    List<Employee> employees = new ArrayList<>();

    List<Employee> freeEmployees = new ArrayList<>();

    public List<Employee> getFreeEmployees() {
        return freeEmployees;
    }

    public void setFreeEmployees(List<Employee> freeEmployees) {
        this.freeEmployees = freeEmployees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
/*
    public String updateStaff(Employee e) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/update"))
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + e.getEmail() + "\",\"firstName\":\"" + e.getFirstName() + "\",\"lastName\":\"" + e.getLastName() + "\",\"phoneNumber\":\"" + e.getPhoneNumber() + "\", \"ssn\":\"" + e.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //return "{\"email\":\"" + email + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\", \"ssn\":\"" + ssn + "\"}";
        //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        //ec.getSessionMap().clear();
        return response.body();
    }
    public String updateStaff(String ssn, String firstName, String lastName, String email, String phoneNumber) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/update"))
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + email + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\", \"ssn\":\"" + ssn + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //return "{\"email\":\"" + email + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\", \"ssn\":\"" + ssn + "\"}";
        //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        //ec.getSessionMap().clear();
        return response.body();
    }*/

    public String updateStaff() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/update"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedEditedEmployee.getEmail() + "\",\"firstName\":\"" + selectedEditedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedEditedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedEditedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedEditedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //return "{\"email\":\"" + email + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\", \"ssn\":\"" + ssn + "\"}";
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
        return response.body();
    }

    public void updateListener(ValueChangeEvent valueChangeEvent){
        selectedEditedEmployee = getEmployee(valueChangeEvent.getNewValue().toString());
    }

    public String addStaff() throws URISyntaxException, IOException, InterruptedException {
        employees.add(newEmployee);
        HttpClient client = HttpClient.newHttpClient();
        if (isRetired(newEmployee.getSsn())) {
            HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/unretire"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
            ec.getSessionMap().clear();
            return response.body();
        } else {
            HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
            ec.getSessionMap().clear();
            return response.body();
        }
    }

    //Varöfr inte bara använda hashmap lmao?
    public Employee getEmployee(String ssn) {
        for (Employee e : employees) {
            if (e.getSsn().contains(ssn)) {
                return e;
            }
        }
        return null;
    }

    public String deleteStaff() throws URISyntaxException, IOException, InterruptedException {
        //employee = employees.get(employees.size() - 1);
        selectedDeletedEmployee = getEmployee(selectedDeletedEmployee.getSsn());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedDeletedEmployee.getEmail() + "\",\"firstName\":\"" + selectedDeletedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedDeletedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedDeletedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedDeletedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
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
        for (Employee i : arr) {
            if (i.getSsn().contains(ssn)) {
                return true;
            }
        }
        return false;
    }

    public String addShift(String ssn, boolean isLate, String date) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = isLate ? "16:00:00" : "11:00:00";
        String endTime = isLate ? "23:00:00" : "14:00:00";
        if(isWeekend(date)){
            endTime = "01:00:00";
        }

        Employee emp = getEmployee(ssn);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\":1}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
        return response.body();
        //return "";
    }

    public String deleteShift(String ssn, boolean isLate, String date, int id) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = isLate ? "16:00:00" : "11:00:00";
        String endTime = isLate ? "23:00:00" : "14:00:00";
        if(isWeekend(date)){
            endTime = "01:00:00";
        }

        Employee emp = getEmployee(ssn);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\":" + id + "}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
        return "{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\"" + id + "}";
    }

    public boolean isWeekend(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        return false;
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

    public void setJSONEmployees() throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Employee[] list_arr = objectMapper.readValue(getJSONEmployees(), Employee[].class);
        List<Employee> arr = new ArrayList<>(Arrays.asList(list_arr));
        employees.addAll(arr);
    }
}
