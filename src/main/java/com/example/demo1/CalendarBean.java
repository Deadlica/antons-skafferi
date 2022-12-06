package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




@Named(value = "CalendarBean")
@ApplicationScoped
public class CalendarBean implements Serializable {




    public class day {
    int datum;
    int id;
    String type="null";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDatum() {
        return datum;
    }

    public void setDatum(int datum) {
        this.datum = datum;
    }
}

BookingBean bookingBean=new BookingBean();

    List<day> amountDays=new ArrayList<>();


    public BookingBean getBookingBean() {
        return bookingBean;
    }

    public void setBookingBean(BookingBean bookingBean) {
        this.bookingBean = bookingBean;
    }

    BookingBean.infoBooking[] holdAllBookings;

    private int shownMonth;
    private int monthIndex =0;

    private int shownYear;
    private int startDayIndex;

    private int thisDay;
    private int thisMonth;
    private int thisYear;
    private int[] days_of_month={31,getFebDays(shownYear),31,30,31,30,31,31,30,31,30,31};

    public CalendarBean() throws IOException, URISyntaxException, InterruptedException {

        String[] localTimeHolder= String.valueOf(LocalDate.now()).split("-",3);
        shownYear = Integer.parseInt(localTimeHolder[0]);
        shownMonth = Integer.parseInt(localTimeHolder[1]);
        thisYear= Integer.parseInt(localTimeHolder[0]);
        thisMonth = Integer.parseInt(localTimeHolder[1]);
        thisDay = Integer.parseInt(localTimeHolder[2]);


        ObjectMapper objectMapper = new ObjectMapper();
         holdAllBookings= objectMapper.readValue(getJsonBookings(), BookingBean.infoBooking[].class);
         startDayIndex = new Date(shownYear, shownMonth -1, 0).getDay();
       generateCalendar();
    }

        public void nextMonth(){
            if (monthIndex <2 ) {
                monthIndex++;
                shownMonth++;
                if (shownMonth == 13) {
                    shownYear++;
                    shownMonth = 1;
                }
                startDayIndex = new Date(shownYear, shownMonth - 1, 0).getDay();

                generateCalendar();
            }
    }
   public void ConfirmAndSubmit() throws URISyntaxException, IOException, InterruptedException {
       bookingBean.makeBooking();
       ObjectMapper objectMapper = new ObjectMapper();
       holdAllBookings= objectMapper.readValue(getJsonBookings(), BookingBean.infoBooking[].class);
       generateCalendar();
    }

    public void prevMonth(){
if (monthIndex >0 ) {
    monthIndex--;
    shownMonth--;
    if (shownMonth == 0) {
        shownYear--;
        shownMonth = 12;
    }
    startDayIndex = new Date(shownYear, shownMonth - 1, 0).getDay();

    generateCalendar();
}
    }

    public int getShownYear() {
        return shownYear;
    }

    public void setShownYear(int shownYear) {
        this.shownYear = shownYear;
    }
    private boolean isLeapYear(int thisYear){
       if ((thisYear % 400 == 0) || ((thisYear % 4 == 0) && (thisYear % 100 != 0))) {
           return true;
       } else {
           return false;
       }
    }
    private int getFebDays(int thisYear){

        if (!isLeapYear(thisYear))
            return 29;
        else
            return 28;
    }

    private void generateCalendar(){
        if (startDayIndex ==0){
            startDayIndex =7;
        }
        amountDays=new ArrayList<>();
        int count=1;
        for (int i=0;i<42;i++)
        {

            day temp=new day();
            temp.id=i;
            temp.datum=42+count;
            if (startDayIndex -1<=i) {
                temp.datum=count;
                if (counting(shownYear,shownMonth,count)<1)
                {
                    if(thisDay>=count+1&&thisYear==shownYear&&thisMonth==shownMonth) {
                    temp.type ="pastDay";
                    }
                    else {
                        temp.type = "notFull";
                    }
                }
                else {
                    if(thisDay>=count+1&&thisYear==shownYear&&thisMonth==shownMonth) {
                        temp.type ="pastDay";
                    }
                    else {
                        temp.type = "full";
                    }

                }
                count++;
            }
            amountDays.add(temp);

            if (days_of_month[shownMonth -1]<count)
            {
                break;
            }
        }
    }




    public List<day> getAmountDays() {
        return amountDays;
    }

    public void setAmountDays(List<day> amountDays) {
        this.amountDays = amountDays;
    }



    public String getMonthName(){
        String[] month_names= {"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};

        return month_names[shownMonth -1];
    }

    private int counting(int yearCount, int monthCount, int dayCount)  {
        int amount=0;
        for (BookingBean.infoBooking var:holdAllBookings) {
            String[] holder = String.valueOf(var.date).split("-", 3);

            if(Integer.parseInt(holder[0]) == yearCount&&Integer.parseInt(holder[1]) == monthCount&&Integer.parseInt(holder[2]) == dayCount){
                amount++;
            }

        }
        return amount ;
    }
    private static String getJsonBookings() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://31.209.47.252:8080/antons-skafferi-db-1.0-SNAPSHOT/api/booking"))
                .GET()
                .build();

        //10.82.231.15
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request2, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
