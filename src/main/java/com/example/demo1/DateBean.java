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

    public String getDate() {
        return date;
    }

    public static class Weekday {
        private URL location = new URL();
        private String link = location.getLink();
        private final String text;
        private final String date;
        private String selected;

        private List<Employee> freeDinnerEmployees;
        private List<Employee> freeLunchEmployees;

        Weekday(String text, String date) throws URISyntaxException, IOException, InterruptedException {
            this.text = text;
            this.date = date;
            freeDinnerEmployees = setFreeJSONEmployees(date, "dinner");
            freeLunchEmployees = setFreeJSONEmployees(date, "lunch");
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

        public String getFreeJSONEmployees(String date, String url) throws IOException, InterruptedException, URISyntaxException {
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/employee/" + url + "/available?date=" + date))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request2, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }

        public List<Employee> setFreeJSONEmployees(String date, String url) throws IOException, URISyntaxException, InterruptedException {
            List<Employee> freeEmployees = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            Employee[] list_arr = objectMapper.readValue(getFreeJSONEmployees(date, url), Employee[].class);
            List<Employee> arr = new ArrayList<>(Arrays.asList(list_arr));

            freeEmployees.addAll(arr);

            return freeEmployees;
        }
    }

    DateBean() throws IOException, URISyntaxException, InterruptedException {
        year = getCurrYear();
        week = getCurrWeek();
        setLunchShift();
        setDinnerShift();
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
        setLunchShift();
        setDinnerShift();
    }

    public void increaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week += 1;
        if (this.week > 52) {
            this.week -= 52;
            this.year += 1;
        }
        setLunchShift();
        setDinnerShift();
    }

    public void setDinnerShift() throws URISyntaxException, IOException, InterruptedException {
        weekdays.clear();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
        weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY)));
        weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY)));
    }

    public void setLunchShift() throws URISyntaxException, IOException, InterruptedException {
        earlyweekdays.clear();
        earlyweekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
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
