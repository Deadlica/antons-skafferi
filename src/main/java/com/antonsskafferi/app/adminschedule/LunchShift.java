package com.antonsskafferi.app.adminschedule;

public class LunchShift extends Shift {
    @Override
    public void setWorkingHours(){
        setBeginTime("11:00:00");
        setEndTime("14:00:00");
    }
    public LunchShift(String date, Employee employee, int id){
        super(date, employee, id);
        setWorkingHours();
    }
}
