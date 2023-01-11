package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@SessionScoped
@Named(value = "ShiftBean")
public class ShiftBean implements Serializable {
    private final URL location = new URL();
    private final String link = location.getLink();

    public static class Weekday {
        private final String text;
        private final String date;
        private String selected;
        private List<Shift> shifts;
        private List<Employee> freeEmployees;
        //Dependency injection
        Weekday(String text, String date, List<Shift> shifts, List<Employee> freeEmployees) {
            this.text = text;
            this.date = date;
            this.shifts = shifts;
            this.freeEmployees = freeEmployees;
        }
        public void pushShift(Shift shift){ shifts.add(shift); }
        public String getDate() {
            return date;
        }

        public String getText() {
            return text;
        }
        public String getSelected() { return selected; }
        public void setSelected(String s) {
            this.selected = s;
        }
        public List<Shift> getShifts() {
            return shifts;
        }
        public void setShifts(List<Shift> shifts) {
            this.shifts = shifts;
        }

        public List<Employee> getFreeEmployees() {
            return freeEmployees;
        }

        public void setFreeEmployees(List<Employee> freeEmployees) {
            this.freeEmployees = freeEmployees;
        }
    }

    ShiftBean() throws IOException, URISyntaxException, InterruptedException {
        year = getCurrYear();
        week = getCurrWeek();
        employees = fetchEmployees();
        updateLists();
        selectedDeletedEmployee = employees.get(0);
        selectedEditedEmployee = employees.get(0);
    }
    private String date;
    private int week;
    private int year;
    private long newSSN;
    private Employee selectedDeletedEmployee;
    private Employee selectedEditedEmployee;
    Employee newEmployee = new Employee();
    List<Employee> employees;
    private List<Weekday> dinnerWeekdays;
    private List<Weekday> lunchWeekdays;

    public Calendar getTodaysDate() {
        return Calendar.getInstance();
    }

    public void decreaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week -= 1;
        if (this.week < 1) {
            this.week += 52;
            this.year -= 1;
        }
        updateLists();
    }

    public void increaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week += 1;
        if (this.week > 52) {
            this.week -= 52;
            this.year += 1;
        }
        updateLists();
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
        List<Employee> allEmployees = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Employee[] list_arr = objectMapper.readValue(getJSONEmployees(), Employee[].class);
        List<Employee> arr = new ArrayList<>(Arrays.asList(list_arr));
        allEmployees.addAll(arr);
        return allEmployees;
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
        List<Shift> shiftsBetween = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Shift[] list_arr = objectMapper.readValue(fetchShiftBetween(startDate, stopDate), Shift[].class);
        List<Shift> arr = new ArrayList<>(Arrays.asList(list_arr));

        shiftsBetween.addAll(arr);
        return shiftsBetween;
    }

    private boolean isLunch(String beginTime){
        return Integer.parseInt(beginTime.substring(0,2)) < 16;
    }

    private int getIndexAtDate(String date, List<Weekday> weekdays){
        int i = 0;
        for(; i < weekdays.size(); i++){
            if(weekdays.get(i).getDate().contains(date)){
                break;
            }
        }
        return i;
    }

    public List<Weekday> putWeekdays(boolean isDinner, List<Shift> shifts, List<Employee> employees) throws URISyntaxException, IOException, InterruptedException {
        List<Weekday> weekdays = new ArrayList<>();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        if(isDinner) {
            weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
            weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY), new ArrayList<Shift>(), new ArrayList<Employee>()));
        }

        for(Shift s : shifts) {
            if (isLunch(s.getBeginTime()) && !isDinner) {
                int i = getIndexAtDate(s.getDate(), weekdays);
                weekdays.get(i).pushShift(s);
            }else if(!isLunch(s.getBeginTime()) && isDinner){
                int i = getIndexAtDate(s.getDate(), weekdays);
                weekdays.get(i).pushShift(s);
            }
        }

        setFreeEmployeesOnWeekdays(weekdays, employees);

        return weekdays;
    }

    private void setFreeEmployeesOnWeekdays(List<Weekday> weekdays, List<Employee> employees){
        for(Weekday w : weekdays){
            w.setFreeEmployees(freeEmployeesFromShifts(w.getShifts(), employees));
        }
    }

    private List<Employee> freeEmployeesFromShifts(List<Shift> shifts, List<Employee> employees){
        List<Employee> freeEmployees = new ArrayList<>();
        for(Employee e : employees){
            boolean isFree = true;
            for(Shift s : shifts){
                if(s.getEmployee().getSsn().contains(e.getSsn())){
                    isFree = false;
                    break;
                }
            }
            if(isFree){
                freeEmployees.add(e);
            }
        }
        return freeEmployees;
    }
    public int getCurrWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }
    public int getCurrYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    public String getText(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        SimpleDateFormat todayFormat = new SimpleDateFormat("d/M");
        return getTodaysDate().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) ? "Idag " + todayFormat.format(date) : dateFormat.format(date);
    }
    public String getDay(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public Employee getEmployee(String ssn) {
        for (Employee e : employees) {
            if (e.getSsn().contains(ssn)) {
                return e;
            }
        }
        return null;
    }
    public boolean isWeekend(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        return false;
    }

    private String getBeginTime(boolean isDinner){
        return isDinner ? "16:00:00" : "11:00:00";
    }

    private String getEndTime(boolean isDinner, boolean isWeekend){
        if(isWeekend && isDinner){
            return "01:00:00";
        }
        return isDinner ? "23:00:00" : "14:00:00";
    }
    public String addShift(String ssn, boolean isDinner, String date) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = getBeginTime(isDinner);
        String endTime = getEndTime(isDinner, isWeekend(date));

        Employee emp = getEmployee(ssn);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\":1}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        updateLists();

        return response.body();
    }

    public String deleteShift(String ssn, boolean isDinner, String date, int id) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = getBeginTime(isDinner);
        String endTime = getEndTime(isDinner, isWeekend(date));

        Employee emp = getEmployee(ssn);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/shift"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\":" + id + "}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        updateLists();

        return "{\"beginTime\":\"" + beginTime + "\",\"date\":\"" + date + "\",\"employee\":{\"email\":\"" + emp.getEmail() + "\",\"firstName\":\"" + emp.getFirstName() + "\",\"lastName\":\"" + emp.getLastName() + "\",\"phoneNumber\":\"" + emp.getPhoneNumber() + "\", \"ssn\":\"" + emp.getSsn() + "\"},\"endTime\":\"" + endTime + "\",\"id\"" + id + "}";
    }

    public String deleteStaff() throws URISyntaxException, IOException, InterruptedException {
        selectedDeletedEmployee = getEmployee(selectedDeletedEmployee.getSsn());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedDeletedEmployee.getEmail() + "\",\"firstName\":\"" + selectedDeletedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedDeletedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedDeletedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedDeletedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        updateSession();

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

    public String updateStaff() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/update"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + selectedEditedEmployee.getEmail() + "\",\"firstName\":\"" + selectedEditedEmployee.getFirstName() + "\",\"lastName\":\"" + selectedEditedEmployee.getLastName() + "\",\"phoneNumber\":\"" + selectedEditedEmployee.getPhoneNumber() + "\", \"ssn\":\"" + selectedEditedEmployee.getSsn() + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //return "{\"email\":\"" + email + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"phoneNumber\":\"" + phoneNumber + "\", \"ssn\":\"" + ssn + "\"}";
        updateSession();
        return response.body();
    }

    public void updateListener(ValueChangeEvent valueChangeEvent){
        selectedEditedEmployee = getEmployee(valueChangeEvent.getNewValue().toString());
    }

    public void updateSession() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
    }

    public void updateLists() throws IOException, URISyntaxException, InterruptedException {
        List<Shift> shifts = getShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        dinnerWeekdays = putWeekdays(true, shifts, employees);
        lunchWeekdays = putWeekdays(false, shifts, employees);
    }

    public String addStaff() throws URISyntaxException, IOException, InterruptedException {
        newEmployee.setSsn(String.valueOf(newSSN));
        employees.add(newEmployee);
        HttpClient client = HttpClient.newHttpClient();
        if (isRetired(newEmployee.getSsn())) {
            HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/unretire"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            updateSession();
            return response.body();
        } else {
            HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + newEmployee.getEmail() + "\",\"firstName\":\"" + newEmployee.getFirstName() + "\",\"lastName\":\"" + newEmployee.getLastName() + "\",\"phoneNumber\":\"" + newEmployee.getPhoneNumber() + "\", \"ssn\":\"" + newEmployee.getSsn() + "\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            updateSession();
            return response.body();
        }
    }

    public Employee getSelectedDeletedEmployee() {
        return selectedDeletedEmployee;
    }
    public void setSelectedDeletedEmployee(Employee selectedDeletedEmployee) {
        this.selectedDeletedEmployee = selectedDeletedEmployee;
    }
    public Employee getSelectedEditedEmployee() {
        return selectedEditedEmployee;
    }
    public void setSelectedEditedEmployee(Employee selectedEditedEmployee) {
        this.selectedEditedEmployee = selectedEditedEmployee;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public Employee getNewEmployee() {
        return newEmployee;
    }
    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getWeek() {
        return week;
    }
    public void setWeek(int week) {
        this.week = week;
    }
    public List<Weekday> getDinnerWeekdays() {
        return dinnerWeekdays;
    }
    public void setDinnerWeekdays(List<Weekday> weekdays) {
        this.dinnerWeekdays = weekdays;
    }
    public List<Weekday> getLunchWeekdays() {
        return lunchWeekdays;
    }
    public void setLunchWeekdays(List<Weekday> lunchWeekdays) {
        this.lunchWeekdays = lunchWeekdays;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public long getNewSSN() {
        return newSSN;
    }

    public void setNewSSN(long newSSN) {
        this.newSSN = newSSN;
    }
}
