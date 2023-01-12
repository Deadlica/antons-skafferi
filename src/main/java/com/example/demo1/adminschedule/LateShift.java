package com.example.demo1.adminschedule;

public class LateShift extends Shift {
    @Override
    public void setWorkingHours(){
        setBeginTime("16:00");
        setEndTime("01:00");
    }

    public LateShift(String date, Employee employee, int id){
        super(date, employee, id);
        setWorkingHours();
    }
}
