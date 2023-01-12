package com.example.demo1.adminschedule;

public class DinnerShift extends Shift {
    @Override
    public void setWorkingHours(){
        setBeginTime("16:00");
        setEndTime("23:00");
    }
    public DinnerShift(String date, Employee employee, int id){
        super(date, employee, id);
        setWorkingHours();
    }
}
