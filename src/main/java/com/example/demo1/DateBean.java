package com.example.demo1;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@RequestScoped
@Named(value = "DateBean")
public class DateBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public String getDate() {
        return date;
    }

    public static class Weekday {
        private final String text;
        private final String date;
        private String selected;

        Weekday(String text, String date) {
            this.text = text;
            this.date = date;
            this.selected = "199905063641";
        }

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
    }

    DateBean() throws IOException, URISyntaxException, InterruptedException {
        week = getCurrWeek();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
        weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY)));
        weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY)));
        //setIndexes()
        earlyweekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
    }

    public void setIndexes(){
        for(Weekday w : weekdays){
            selectedShiftEmployeeDate.put(w.getDate(), "");
        }
    }
    HashMap<String, String> selectedShiftEmployeeDate = new HashMap<>();


    public HashMap<String, String> getSelectedShiftEmployeeDate() {
        return selectedShiftEmployeeDate;
    }
    public void setSelectedShiftEmployeeDate(HashMap<String, String> selectedShiftEmployeeDate) {
        this.selectedShiftEmployeeDate = selectedShiftEmployeeDate;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getNow() {
        return new Date().toString();
    }

    public void decreaseWeek() {
        week = week - 1;
        if (week < 0) {
            week = week + 52;
        }
        setWeek(week);
    }

    public void increaseWeek(int week) {
        this.week = week + 1;
        if (this.week > 52) {
            this.week = week - 52;
        }
        //setWeek(this.week);
    }

    public int getCurrWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public String getText(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        return dateFormat.format(date);
    }

    public String getDay(int day) {
        Calendar c = Calendar.getInstance();
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
}
