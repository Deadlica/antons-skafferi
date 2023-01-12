package com.example.demo1.adminschedule;

import java.util.List;
public abstract class Weekday{
    private final String text;
    private final String date;
    private String selected;
    private List<Shift> shifts;
    private List<Employee> freeEmployees;
    public Weekday(String text, String date, List<Shift> shifts, List<Employee> freeEmployees) {
        this.text = text;
        this.date = date;
        this.shifts = shifts;
        this.freeEmployees = freeEmployees;
    }
    //public void pushShift(Shift shift){ shifts.add(shift); }
    public abstract void pushShift(Shift shift);
    public abstract Shift createShift(String date, Employee employee, int id);
    Employee findFreeEmployee(String ssn){
        for (Employee e : freeEmployees) {
            if (e.getSsn().contains(ssn)) {
                return e;
            }
        }
        return null;
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
