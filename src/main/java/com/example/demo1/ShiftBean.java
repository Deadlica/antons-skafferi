package com.example.demo1;

import com.example.demo1.adminschedule.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@SessionScoped
@Named(value = "ShiftBean")
public class ShiftBean implements Serializable {
    private String date;
    private int week;
    private int year;
    private long newSSN;
    private final ShiftRequest shiftRequest = new ShiftRequest();
    private Employee selectedDeletedEmployee;
    private Employee selectedEditedEmployee;
    Employee newEmployee = new Employee();
    List<Employee> employees;
    private WeekdayContainer dinner;
    private WeekdayContainer lunch;
    private List<Shift> shifts;
    ShiftBean() throws IOException, URISyntaxException, InterruptedException {
        year = getCurrYear();
        week = getCurrWeek();
        employees = shiftRequest.fetchEmployees();
        updateLists();
        selectedDeletedEmployee = employees.get(0);
        selectedEditedEmployee = employees.get(0);
    }

    private Calendar getCurrDate() {
        return Calendar.getInstance();
    }

    public void decreaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week -= 1;
        if (this.week < 1) {
            this.week += 52;
            this.year -= 1;
        }
        updateLists();
    }

    public void increaseWeek() throws URISyntaxException, IOException, InterruptedException {
        this.week += 1;
        if (this.week > 52) {
            this.week -= 52;
            this.year += 1;
        }
        updateLists();
    }

    private void setWeekdays(){
        dinner.addWeekday(new DinnerWeekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new DinnerWeekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new DinnerWeekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new DinnerWeekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new LateWeekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new LateWeekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY), new ArrayList<>(), new ArrayList<>()));
        dinner.addWeekday(new DinnerWeekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY), new ArrayList<>(), new ArrayList<>()));

        lunch.addWeekday(new LunchWeekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY), new ArrayList<>(), new ArrayList<>()));
        lunch.addWeekday(new LunchWeekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY), new ArrayList<>(), new ArrayList<>()));
        lunch.addWeekday(new LunchWeekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY), new ArrayList<>(), new ArrayList<>()));
        lunch.addWeekday(new LunchWeekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY), new ArrayList<>(), new ArrayList<>()));
        lunch.addWeekday(new LunchWeekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY), new ArrayList<>(), new ArrayList<>()));
    }
    public int getCurrWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }
    public int getCurrYear(){
        return getCurrDate().get(Calendar.YEAR);
    }

    public String getText(int day) {
        Calendar c = getCurrDate();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        SimpleDateFormat todayFormat = new SimpleDateFormat("d/M");
        return getCurrDate().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) ? "Idag " + todayFormat.format(date) : dateFormat.format(date);
    }
    public String getDay(int day) {
        Calendar c = getCurrDate();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public Employee findEmployee(String ssn) {
        for (Employee e : employees) {
            if (e.getSsn().contains(ssn)) {
                return e;
            }
        }
        return null;
    }
    public String deleteStaff() throws URISyntaxException, IOException, InterruptedException {
        selectedDeletedEmployee = findEmployee(selectedDeletedEmployee.getSsn());
        String response = shiftRequest.deleteStaff(selectedDeletedEmployee);
        updateSession();
        return response;
    }

    public Shift findShift(int id){
        for(Shift s : shifts){
            if(s.getId() == id){
                return s;
            }
        }
        return null;
    }

    public String deleteShift(int id) throws URISyntaxException, IOException, InterruptedException {
        if(findShift(id) != null) {
            String response = shiftRequest.deleteShift(findShift(id));
            updateLists();
            return response;
        }
        return "Error";
    }

    public String updateStaff() throws URISyntaxException, IOException, InterruptedException {
        String response = shiftRequest.updateStaff(selectedEditedEmployee);
        updateSession();
        return response;
    }

    public String addStaff() throws URISyntaxException, IOException, InterruptedException {
        newEmployee.setSsn(String.valueOf(newSSN));
        employees.add(newEmployee);

        String response = shiftRequest.addStaff(newEmployee);

        updateSession();

        return response;
    }

    public void updateListener(ValueChangeEvent valueChangeEvent){
        selectedEditedEmployee = findEmployee(valueChangeEvent.getNewValue().toString());
    }

    public void updateSession() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
    }

    public void updateLists() throws IOException, URISyntaxException, InterruptedException {
        shifts = shiftRequest.getShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        dinner = new DinnerWeekdayContainer(new ArrayList<>(), shiftRequest);
        lunch = new LunchWeekdayContainer(new ArrayList<>(), shiftRequest);
        setWeekdays();
        dinner.addShiftToWeekdays(shifts);
        lunch.addShiftToWeekdays(shifts);
        dinner.addFreeEmployeesToWeekdays(employees);
        lunch.addFreeEmployeesToWeekdays(employees);
    }
    public Employee getSelectedDeletedEmployee() {
        return selectedDeletedEmployee;
    }
    public void setSelectedDeletedEmployee(Employee selectedDeletedEmployee) {
        this.selectedDeletedEmployee = selectedDeletedEmployee;
    }
    public Employee getSelectedEditedEmployee() {
        return selectedEditedEmployee;
    }
    public void setSelectedEditedEmployee(Employee selectedEditedEmployee) {
        this.selectedEditedEmployee = selectedEditedEmployee;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public Employee getNewEmployee() {
        return newEmployee;
    }
    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getWeek() {
        return week;
    }
    public void setWeek(int week) {
        this.week = week;
    }
    public WeekdayContainer getDinner() {
        return dinner;
    }
    public void setDinner(WeekdayContainer dinner) {
        this.dinner = dinner;
    }
    public WeekdayContainer getLunch() {
        return lunch;
    }
    public void setLunch(WeekdayContainer lunch) {
        this.lunch = lunch;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public long getNewSSN() {
        return newSSN;
    }
    public void setNewSSN(long newSSN) {
        this.newSSN = newSSN;
    }
}
