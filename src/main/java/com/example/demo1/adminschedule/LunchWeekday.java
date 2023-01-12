package com.example.demo1.adminschedule;

import java.util.List;

public class LunchWeekday extends Weekday{
    public LunchWeekday(String text, String date, List<Shift> shifts, List<Employee> freeEmployees) {
        super(text, date, shifts, freeEmployees);
    }

    @Override
    public void pushShift(Shift shift) {
        getShifts().add(new LunchShift(shift.getDate(), shift.getEmployee(), shift.getId()));
    }

    @Override
    public Shift createShift(String date, Employee employee, int id) {
        return new LunchShift(date, employee, id);
    }
}
