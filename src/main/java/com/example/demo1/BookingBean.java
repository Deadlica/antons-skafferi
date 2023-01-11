package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Named(value = "BookingBean")
@RequestScoped
public class BookingBean implements Serializable {
    private URL location = new URL();
    private String link = location.getLink();

    public BookingBean() throws IOException, URISyntaxException, InterruptedException {
    }



        String receivingMessage;
        InfoBooking infobooking = new InfoBooking();

        public InfoBooking getInfobooking() {
            return infobooking;
        }

        public void setInfobooking(InfoBooking infobooking) {
            this.infobooking = infobooking;
        }


        public String getReceivingMessage() {
            return receivingMessage;
        }

        public void setReceivingMessage(String receivingMessage) {
            this.receivingMessage = receivingMessage;
        }
        @Inject
        CalendarBean calendarBean;
        public void makeBooking() throws URISyntaxException, IOException, InterruptedException {
            receivingMessage="Error: ";
            if (verifyInputs()) {
                receivingMessage = "bokat namn:"+infobooking.firstName+" "+infobooking.lastName+" datum:"+infobooking.date+" time:"+infobooking.time;
                ObjectMapper mapper = new ObjectMapper();
                String tempResponse ="   "+ addBooking();

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(tempResponse, Map.class);
                receivingMessage +=": "+ (String) jsonMap.get("firstName");

                if (!((String) jsonMap.get("firstName")).equals("Booking has been made!"))
                {
                    receivingMessage="Error: datum fullbokat!";
                }


                infobooking.date = "0000-00-00";
            }
            calendarBean.newBookingMade();

        }

        private boolean verifyInputs() {
            if (!infobooking.date.equals("0000-00-00")){
                if (infobooking.phoneNumber.length() == 0 || infobooking.firstName.length() == 0 || infobooking.numberOfPeople == 0 || infobooking.time.length() == 0) {
                    receivingMessage += "-alla inputs krävs";

                    return false;
                }
                if (checkName() && checkPhoneNumber() && checkNumberOfPeople() && checkTime()) {
                    return true;
                }
                return false;
            }
            receivingMessage+="-select date";
            return false;
        }

        private boolean checkNumberOfPeople(){

if (infobooking.numberOfPeople<5&&infobooking.numberOfPeople>=1)
{
    return true;
}
receivingMessage+="-ogiltigt antal";
            return false;
    }
        private boolean checkPhoneNumber(){
        infobooking.phoneNumber=infobooking.phoneNumber.replaceAll("[^0-9]", "");
        if (infobooking.phoneNumber.matches("^\\d{10}$")) {
            return true;
        }
        receivingMessage+="-ogiltigt telefon-nummer";
            return false;
    }
        private boolean checkName(){

if (infobooking.firstName.matches("^[a-zA-Z]+$")&& infobooking.lastName.matches("^[a-zA-Z]+$")){
return true;
}
        receivingMessage+="-ogiltigt för/efter-namn";
            return false;
    }

        private boolean checkTime(){

                if (infobooking.time.matches("^\\d\\d:\\d\\d$")) {

                    int hour = Integer.parseInt(infobooking.time.substring(0, 2));
                    int minute = Integer.parseInt(infobooking.time.substring(3, 5));
                    if ((hour >= 16 && hour < 21 && minute >= 0 && minute < 60) || (hour == 21 && minute == 0)) {
                        return true;
                    }
                    else {
                        receivingMessage+="-ogiltig tid";
                        return false;
                    }

                }
                receivingMessage+="-ogiltig tid(xx:xx)";
            return false;

        }


        private String addBooking() throws URISyntaxException, IOException, InterruptedException {


            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(infobooking);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/booking"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }


}
