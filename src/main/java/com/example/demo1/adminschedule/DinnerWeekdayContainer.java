package com.example.demo1.adminschedule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class DinnerWeekdayContainer extends WeekdayContainer{
    public DinnerWeekdayContainer(){
        super();
    }
    public DinnerWeekdayContainer(List<Weekday> weekdays, ShiftRequest shiftRequest) {
        super(weekdays, shiftRequest);
    }
    @Override
    public void addShiftToWeekdays(List<Shift> shifts) {
        for(Shift s : shifts) {
            if (!isLunch(s.getBeginTime())) {
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
