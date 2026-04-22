package com.antonsskafferi.app;

import com.antonsskafferi.app.adminschedule.*;
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
    private String newSSN;
    private final ShiftRequest shiftRequest = ShiftRequest.getInstance();
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
        if (!employees.isEmpty()) {
            selectedDeletedEmployee = employees.get(0);
            selectedEditedEmployee = employees.get(0);
        } else {
            selectedDeletedEmployee = new Employee();
            selectedEditedEmployee = new Employee();
        }
    }

    private Calendar getCurrDate() {
        return Calendar.getInstance();
    }

    public void decreaseWeek() throws URISyntaxException, IOException, InterruptedException {
        LocalDate prev = LocalDate.now()
                .with(java.time.temporal.IsoFields.WEEK_BASED_YEAR, year)
                .with(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(java.time.DayOfWeek.MONDAY)
                .minusWeeks(1);
        this.year = prev.get(java.time.temporal.IsoFields.WEEK_BASED_YEAR);
        this.week = prev.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        updateLists();
    }

    public void increaseWeek() throws URISyntaxException, IOException, InterruptedException {
        LocalDate next = LocalDate.now()
                .with(java.time.temporal.IsoFields.WEEK_BASED_YEAR, year)
                .with(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(java.time.DayOfWeek.MONDAY)
                .plusWeeks(1);
        this.year = next.get(java.time.temporal.IsoFields.WEEK_BASED_YEAR);
        this.week = next.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
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
        return LocalDate.now().get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
    public int getCurrYear(){
        return getCurrDate().get(Calendar.YEAR);
    }

    public String getText(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(java.sql.Date.valueOf(getDay(day)));
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d/M");
        SimpleDateFormat todayFormat = new SimpleDateFormat("d/M");
        return getCurrDate().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) ? "Idag " + todayFormat.format(date) : dateFormat.format(date);
    }
    public String getDay(int day) {
        int[] map = {0, 6, 0, 1, 2, 3, 4, 5}; // Calendar.SUNDAY(1)..SATURDAY(7) -> Mon-based offset
        LocalDate monday = LocalDate.now()
                .with(java.time.temporal.IsoFields.WEEK_BASED_YEAR, year)
                .with(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(java.time.DayOfWeek.MONDAY);
        return monday.plusDays(map[day]).toString();
    }
    public Employee findEmployee(String ssn) {
        for (Employee e : employees) {
            if (e.getSsn().equals(ssn)) {
                return e;
            }
        }
        return null;
    }
    public String deleteStaff() throws URISyntaxException, IOException, InterruptedException {
        if (selectedDeletedEmployee == null || selectedDeletedEmployee.getSsn() == null) return null;
        Employee found = findEmployee(selectedDeletedEmployee.getSsn());
        if (found == null) return null;
        selectedDeletedEmployee = found;
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
        if (!isValidSSN(newSSN)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR,
                            "Ogiltigt personnummer (YYYYMMDDXXXX)", null));
            return null;
        }
        newEmployee.setSsn(newSSN);
        employees.add(newEmployee);

        String response = shiftRequest.addStaff(newEmployee);

        updateSession();

        return response;
    }

    static boolean isValidSSN(String ssn) {
        if (ssn == null || !ssn.matches("\\d{12}")) return false;
        try {
            LocalDate.of(Integer.parseInt(ssn.substring(0, 4)),
                    Integer.parseInt(ssn.substring(4, 6)),
                    Integer.parseInt(ssn.substring(6, 8)));
        } catch (Exception e) { return false; }
        // Luhn over last 10 digits (YYMMDDXXXC)
        String last10 = ssn.substring(2);
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int d = last10.charAt(i) - '0';
            if (i % 2 == 0) { d *= 2; if (d > 9) d -= 9; }
            sum += d;
        }
        return sum % 10 == 0;
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

    public void addLunchShift(String ssn, String date) throws IOException, URISyntaxException, InterruptedException {
        lunch.addShift(ssn, date);
        updateLists();
    }

    public void addDinnerShift(String ssn, String date) throws IOException, URISyntaxException, InterruptedException {
        dinner.addShift(ssn, date);
        updateLists();
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
    public String getNewSSN() {
        return newSSN;
    }
    public void setNewSSN(String newSSN) {
        this.newSSN = newSSN;
    }
}
