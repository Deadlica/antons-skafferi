package com.example.demo1.adminschedule;

import com.example.demo1.ShiftBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LunchWeekdayContainer extends WeekdayContainer{
    public LunchWeekdayContainer(List<Weekday> weekdays, ShiftRequest shiftRequest) {
        super(weekdays, shiftRequest);
    }
    @Override
    public void addShiftToWeekdays(List<Shift> shifts) {
        for(Shift s : shifts) {
            if (isLunch(s.getBeginTime())) {
                int i = getIndexAtDate(s.getDate(), getWeekdays());
                getWeekdays().get(i).pushShift(s);
            }
        }
    }

    @Override
    public void addShift(String ssn, String date) throws URISyntaxException, IOException, InterruptedException {
        int i = getIndexAtDate(date, getWeekdays());
        Employee employee = getWeekdays().get(i).findFreeEmployee(ssn);
        shiftRequest.addShift(getWeekdays().get(i).createShift(date, employee, 0));

    }
}
