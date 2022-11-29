package com.example.demo1;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@RequestScoped
@Named(value = "DateBean")
public class DateBean implements Serializable {
    public String getDate() {
        return date;
    }

    public static class Weekday {
        private final String text;
        private final String date;
        Weekday(String text, String date){
            this.text = text;
            this.date = date;
        }
        public String getDate() {
            return date;
        }
        public String getText() {
            return text;
        }
    }

    DateBean(){
        week = getCurrWeek();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
        weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY)));
        weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY)));

        earlyweekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY)));
        earlyweekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY)));
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNow(){
        return new Date().toString();
    }

    public void decreaseWeek(){
        week = week - 1;
        if(week < 0){
            week = week + 52;
        }
        setWeek(week);
    }

    public void increaseWeek(){
        week = week + 1;
        if(week > 52){
            week = week - 52;
        }
        setWeek(week);
    }
    public int getCurrWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public String getText(int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        return dateFormat.format(date);
    }

    public String getDay(int day){
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
