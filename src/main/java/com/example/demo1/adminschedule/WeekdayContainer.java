package com.example.demo1.adminschedule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public abstract class WeekdayContainer {
    private List<Weekday> weekdays;
    ShiftRequest shiftRequest;
    public WeekdayContainer(){
    }
    public WeekdayContainer(List<Weekday> weekdays, ShiftRequest shiftRequest){
        this.weekdays = weekdays;
        this.shiftRequest = shiftRequest;
    }
    boolean isLunch(String beginTime){
        return Integer.parseInt(beginTime.substring(0,2)) < 16;
    }
    public boolean isWeekend(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }
    int getIndexAtDate(String date, List<Weekday> weekdays){
        int i = 0;
        for(; i < weekdays.size(); i++){
            if(weekdays.get(i).getDate().contains(date)){
                break;
            }
        }
        return i;
    }
    public void addWeekday(Weekday weekday){
        weekdays.add(weekday);
    };

    public abstract void addShiftToWeekdays(List<Shift> shifts);

    public abstract void addShift(String ssn, String date) throws URISyntaxException, IOException, InterruptedException;
    public void addFreeEmployeesToWeekdays(List<Employee> employees){
        for(Weekday w : weekdays){
            w.setFreeEmployees(freeEmployeesFromShifts(w.getShifts(), employees));
        }
    }
    private List<Employee> freeEmployeesFromShifts(List<Shift> shifts, List<Employee> employees){
        List<Employee> freeEmployees = new ArrayList<>();
        for(Employee e : employees){
            boolean isFree = true;
            for(Shift s : shifts){
                if(s.getEmployee().getSsn().contains(e.getSsn())){
                    isFree = false;
                    break;
                }
            }
            if(isFree){
                freeEmployees.add(e);
            }
        }
        return freeEmployees;
    }

    public List<Weekday> getWeekdays() {
        return weekdays;
    }
    public void setWeekdays(List<Weekday> weekdays) {
        this.weekdays = weekdays;
    }

}
