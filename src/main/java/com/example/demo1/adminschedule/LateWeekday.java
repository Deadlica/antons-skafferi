package com.example.demo1.adminschedule;

import java.util.List;

public class LateWeekday extends Weekday{
    public LateWeekday(String text, String date, List<Shift> shifts, List<Employee> freeEmployees) {
        super(text, date, shifts, freeEmployees);
    }
    @Override
    public void pushShift(Shift shift) {
        getShifts().add(new LateShift(shift.getDate(), shift.getEmployee(), shift.getId()));
    }
    @Override
    public Shift createShift(String date, Employee employee, int id) {
        return new LateShift(date, employee, id);
    }
}
