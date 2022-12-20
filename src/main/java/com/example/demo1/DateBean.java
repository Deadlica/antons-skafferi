package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
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
@Named(value = "DateBean")
public class DateBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public String getDate() {
        return date;
    }

    public static class Weekday {
        private URL location = new URL();
        private String link = location.getLink();
        private final String text;
        private final String date;

        public List<ShiftBean.Shift> getLunchShift() {
            return lunchShift;
        }

        public void setLunchShift(List<ShiftBean.Shift> lunchShift) {
            this.lunchShift = lunchShift;
        }

        public List<ShiftBean.Shift> getDinnerShift() {
            return dinnerShift;
        }

        public void setDinnerShift(List<ShiftBean.Shift> dinnerShift) {
            this.dinnerShift = dinnerShift;
        }

        private String selected;

        private List<Employee> freeDinnerEmployees = new ArrayList<>();
        private List<Employee> freeLunchEmployees = new ArrayList<>();

        private List<ShiftBean.Shift> lunchShift = new ArrayList<>();
        private List<ShiftBean.Shift> dinnerShift = new ArrayList<>();

        Weekday(String text, String date) throws URISyntaxException, IOException, InterruptedException {
            this.text = text;
            this.date = date;
        }

        private void setFreeEmployees(boolean isDinner, List<Employee> employees){
            if(isDinner){
                freeDinnerEmployees.clear();
                for(Employee e : employees){
                    boolean isFree = true;
                    for(ShiftBean.Shift s : dinnerShift){
                        if(e.getSsn().contains(s.getEmployee().getSsn())){
                            isFree = false;
                            break;
                        }else{
                            isFree = true;
                        }
                    }
                    if(isFree){
                        freeDinnerEmployees.add(e);
                    }
                }
            }else{
                freeLunchEmployees.clear();
                for(Employee e : employees){
                    boolean isFree = true;
                    for(ShiftBean.Shift s : lunchShift){
                        if(e.getSsn().contains(s.getEmployee().getSsn())){
                            isFree = false;
                            break;
                        }else{
                            isFree = true;
                        }
                    }
                    if(isFree){
                        freeLunchEmployees.add(e);
                    }
                }
            }

        }

        public String getDate() {
            return date;
        }

        public String getText() {
            return text;
        }

        public List<Employee> getFreeDinnerEmployees() {
            return freeDinnerEmployees;
        }

        public void setFreeDinnerEmployees(List<Employee> freeDinnerEmployees) {
            this.freeDinnerEmployees = freeDinnerEmployees;
        }

        public List<Employee> getFreeLunchEmployees() {
            return freeLunchEmployees;
        }

        public void setFreeLunchEmployees(List<Employee> freeLunchEmployees) {
            this.freeLunchEmployees = freeLunchEmployees;
        }

        public String getSelected() { return selected; }
        public void setSelected(String s) {
            this.selected = s;
        }

        public void pushLunchShift(ShiftBean.Shift shift){
            lunchShift.add(shift);
        }

        public void pushDinnerShift(ShiftBean.Shift shift){
            dinnerShift.add(shift);
        }
    }

    DateBean() throws IOException, URISyntaxException, InterruptedException {
        year = getCurrYear();
        week = getCurrWeek();
        setShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        setJSONEmployees();
        setShift();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNow() {
        return new Date().toString();
    }

    public void decreaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week -= 1;
        if (this.week < 1) {
            this.week += 52;
            this.year -= 1;
        }
        setShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        setShift();
    }

    public void increaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week += 1;
        if (this.week > 52) {
            this.week -= 52;
            this.year += 1;
        }
        setShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        setShift();
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

    List<Employee> employees = new ArrayList<>();

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
    public void setShiftBetween(String startDate, String stopDate) throws IOException, URISyntaxException, InterruptedException {
        shift.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        ShiftBean.Shift[] list_arr = objectMapper.readValue(fetchShiftBetween(startDate, stopDate), ShiftBean.Shift[].class);
        List<ShiftBean.Shift> arr = new ArrayList<>(Arrays.asList(list_arr));
        shift.addAll(arr);
    }
    List<ShiftBean.Shift> shift = new ArrayList<>();
    public void setShift() throws URISyntaxException, IOException, InterruptedException {
        weekdays.clear();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
        weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY)));
        weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY)));

        earlyweekdays.clear();
        earlyweekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));

        for(ShiftBean.Shift s : shift){
            if(s.getDate().contains(getDay(Calendar.MONDAY))){
                if(Integer.parseInt(s.getBeginTime().substring(0,2)) < 16){
                    earlyweekdays.get(0).pushLunchShift(s);
                }else{
                    weekdays.get(0).pushDinnerShift(s);
                }
            }else if(s.getDate().contains(getDay(Calendar.TUESDAY))){
                if(Integer.parseInt(s.getBeginTime().substring(0,2)) < 16){
                    earlyweekdays.get(1).pushLunchShift(s);
                }else{
                    weekdays.get(1).pushDinnerShift(s);
                }
            }else if(s.getDate().contains(getDay(Calendar.WEDNESDAY))){
                if(Integer.parseInt(s.getBeginTime().substring(0,2)) < 16){
                    earlyweekdays.get(2).pushLunchShift(s);
                }else{
                    weekdays.get(2).pushDinnerShift(s);
                }
            }else if (s.getDate().contains(getDay(Calendar.THURSDAY))) {
                if(Integer.parseInt(s.getBeginTime().substring(0,2)) < 16){
                    earlyweekdays.get(3).pushLunchShift(s);
                }else{
                    weekdays.get(3).pushDinnerShift(s);
                }
            }else if(s.getDate().contains(getDay(Calendar.FRIDAY))){
                if(Integer.parseInt(s.getBeginTime().substring(0,2)) < 16){
                    earlyweekdays.get(4).pushLunchShift(s);
                }else{
                    weekdays.get(4).pushDinnerShift(s);
                }
            }else if(s.getDate().contains(getDay(Calendar.SATURDAY))){
                weekdays.get(5).pushDinnerShift(s);
            }else if(s.getDate().contains(getDay(Calendar.SUNDAY))){
                weekdays.get(6).pushDinnerShift(s);
            }
        }
        for(Weekday w : weekdays){
            w.setFreeEmployees(true, employees);
        }
        for(Weekday w : earlyweekdays){
            w.setFreeEmployees(false, employees);
        }
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
        return dateFormat.format(date);
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

    private String date;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
    private int week;

    private int year;
    public List<Weekday> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Weekday> weekdays) {
        this.weekdays = weekdays;
    }
    private List<Weekday> weekdays = new ArrayList<>();
    private List<Weekday> earlyweekdays = new ArrayList<>();

    public List<Weekday> getEarlyweekdays() {
        return earlyweekdays;
    }

    public void setEarlyweekdays(List<Weekday> earlyweekdays) {
        this.earlyweekdays = earlyweekdays;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
