package com.example.demo1;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@SessionScoped
@Named(value = "ShiftBean")
public class ShiftBean extends HttpServlet {
    private String date;
    private int week;
    private int year;
    private long newSSN;
    private final ShiftRequest shiftRequest = new ShiftRequest();
    private Employee selectedDeletedEmployee;
    private Employee selectedEditedEmployee;
    Employee newEmployee = new Employee();
    List<Employee> employees;
    private List<Weekday> dinnerWeekdays;
    private List<Weekday> lunchWeekdays;
    ShiftBean() throws IOException, URISyntaxException, InterruptedException {
        year = getCurrYear();
        week = getCurrWeek();
        employees = shiftRequest.fetchEmployees();
        updateLists();
        selectedDeletedEmployee = employees.get(0);
        selectedEditedEmployee = employees.get(0);
    }

    public Calendar getTodaysDate() {
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
    private boolean isLunch(String beginTime){
        return Integer.parseInt(beginTime.substring(0,2)) < 16;
    }

    private int getIndexAtDate(String date, List<Weekday> weekdays){
        int i = 0;
        for(; i < weekdays.size(); i++){
            if(weekdays.get(i).getDate().contains(date)){
                break;
            }
        }
        return i;
    }
    public List<Weekday> putWeekdays(boolean isDinner, List<Shift> shifts, List<Employee> employees){
        List<Weekday> weekdays = new ArrayList<>();
        weekdays.add(new Weekday(getText(Calendar.MONDAY), getDay(Calendar.MONDAY), new ArrayList<>(), new ArrayList<>()));
        weekdays.add(new Weekday(getText(Calendar.TUESDAY), getDay(Calendar.TUESDAY), new ArrayList<>(), new ArrayList<>()));
        weekdays.add(new Weekday(getText(Calendar.WEDNESDAY), getDay(Calendar.WEDNESDAY), new ArrayList<>(), new ArrayList<>()));
        weekdays.add(new Weekday(getText(Calendar.THURSDAY), getDay(Calendar.THURSDAY), new ArrayList<>(), new ArrayList<>()));
        weekdays.add(new Weekday(getText(Calendar.FRIDAY), getDay(Calendar.FRIDAY), new ArrayList<>(), new ArrayList<>()));
        if(isDinner) {
            weekdays.add(new Weekday(getText(Calendar.SATURDAY), getDay(Calendar.SATURDAY), new ArrayList<>(), new ArrayList<>()));
            weekdays.add(new Weekday(getText(Calendar.SUNDAY), getDay(Calendar.SUNDAY), new ArrayList<>(), new ArrayList<>()));
        }

        for(Shift s : shifts) {
            if (isLunch(s.getBeginTime()) && !isDinner) {
                int i = getIndexAtDate(s.getDate(), weekdays);
                weekdays.get(i).pushShift(s);
            }else if(!isLunch(s.getBeginTime()) && isDinner){
                int i = getIndexAtDate(s.getDate(), weekdays);
                weekdays.get(i).pushShift(s);
            }
        }

        setFreeEmployeesOnWeekdays(weekdays, employees);

        return weekdays;
    }

    private void setFreeEmployeesOnWeekdays(List<Weekday> weekdays, List<Employee> employees){
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
    public int getCurrWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }
    public int getCurrYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    public String getText(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        SimpleDateFormat todayFormat = new SimpleDateFormat("d/M");
        return getTodaysDate().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) ? "Idag " + todayFormat.format(date) : dateFormat.format(date);
    }
    public String getDay(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, day);
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public Employee getEmployee(String ssn) {
        for (Employee e : employees) {
            if (e.getSsn().contains(ssn)) {
                return e;
            }
        }
        return null;
    }
    public boolean isWeekend(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    private String getBeginTime(boolean isDinner){
        return isDinner ? "16:00:00" : "11:00:00";
    }

    private String getEndTime(boolean isDinner, boolean isWeekend){
        if(isWeekend && isDinner){
            return "01:00:00";
        }
        return isDinner ? "23:00:00" : "14:00:00";
    }

    public String deleteStaff() throws URISyntaxException, IOException, InterruptedException {
        selectedDeletedEmployee = getEmployee(selectedDeletedEmployee.getSsn());
        String response = shiftRequest.deleteStaff(selectedDeletedEmployee);
        updateSession();
        return response;
    }

    public String addShift(String ssn, boolean isDinner, String date) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = getBeginTime(isDinner);
        String endTime = getEndTime(isDinner, isWeekend(date));

        Employee emp = getEmployee(ssn);
        String response = shiftRequest.addShift(emp, beginTime, endTime, date);
        updateLists();
        return response;
    }

    public String deleteShift(String ssn, boolean isDinner, String date, int id) throws URISyntaxException, IOException, InterruptedException {
        String beginTime = getBeginTime(isDinner);
        String endTime = getEndTime(isDinner, isWeekend(date));

        Employee emp = getEmployee(ssn);

        String response = shiftRequest.deleteShift(emp, beginTime, endTime, date, id);

        updateLists();
        return response;
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
        selectedEditedEmployee = getEmployee(valueChangeEvent.getNewValue().toString());
    }

    public void updateSession() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/admin/schedule.xhtml");
        ec.getSessionMap().clear();
    }

    public void updateLists() throws IOException, URISyntaxException, InterruptedException {
        List<Shift> shifts = shiftRequest.getShiftBetween(getDay(Calendar.MONDAY), getDay(Calendar.SUNDAY));
        dinnerWeekdays = putWeekdays(true, shifts, employees);
        lunchWeekdays = putWeekdays(false, shifts, employees);
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
    public List<Weekday> getDinnerWeekdays() {
        return dinnerWeekdays;
    }
    public void setDinnerWeekdays(List<Weekday> weekdays) {
        this.dinnerWeekdays = weekdays;
    }
    public List<Weekday> getLunchWeekdays() {
        return lunchWeekdays;
    }
    public void setLunchWeekdays(List<Weekday> lunchWeekdays) {
        this.lunchWeekdays = lunchWeekdays;
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
