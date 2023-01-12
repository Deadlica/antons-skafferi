package com.example.demo1.adminschedule;

public class Shift {
    private int id;
    private String date;
    private String beginTime;
    private String endTime;
    private Employee employee;
    public Shift(){};
    public Shift(String date, Employee employee){
        this.date = date;
        this.employee = employee;
    }
    public Shift(String date, Employee employee, int id){
        this.date = date;
        this.employee = employee;
        this.id = id;
    }

    public Shift(int id, String date, String beginTime, String endTime, Employee employee){
        this.date = date;
        this.employee = employee;
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
    public void setWorkingHours(){
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
