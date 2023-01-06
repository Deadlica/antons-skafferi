package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Named(value = "CommentsBean")
@RequestScoped
public class CommentsBean {
    Comment[] holdAllReviews;

    public Comment[] getHoldAllReviews() {
        return holdAllReviews;
    }

    public void setHoldAllReviews(Comment[] holdAllReviews) {
        this.holdAllReviews = holdAllReviews;
    }



    public static class  Comment{
        String firstName ="";

        int id=1;

        String lastName ="nått";
        int ranking=0;

        String text ="";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }
    }

    public CommentsBean() throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        holdAllReviews= objectMapper.readValue(getJsonReview(), Comment[].class);

    }
    private URL location = new URL();
    private String link = location.getLink();

    Comment comment=new Comment();

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

String response;
    URI uri;

    {
        try {
            uri = new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/review");
            //uri = new URI("http://10.82.231.15:8080/antons-skafferi-db-1.0-SNAPSHOT/api/dish");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

   public void removeComment(int id) throws URISyntaxException, IOException, InterruptedException {
       for (Comment i : holdAllReviews) {
           if (i.id==id){
               response= String.valueOf(removeReview(i));
               ObjectMapper objectMapper = new ObjectMapper();
               holdAllReviews= objectMapper.readValue(getJsonReview(), Comment[].class);

           }
       }
    }

    public void comfirmReview() throws URISyntaxException, IOException, InterruptedException {
//check grejer

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(comment);
        response=addReview()+"      namn:"+comment.firstName +" komentar: "+comment.text +" rating: "+comment.ranking+"-----------"+json;
        comment=new Comment();
        ObjectMapper objectMapper = new ObjectMapper();
        holdAllReviews= objectMapper.readValue(getJsonReview(), Comment[].class);
    }

    private HttpResponse<String> addReview() throws URISyntaxException, IOException, InterruptedException {


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(comment);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + this.link + ":8080/antons-skafferi-db-1.0-SNAPSHOT/api/review"))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }


    private HttpResponse<String> removeReview(Comment removeComment) throws URISyntaxException, IOException, InterruptedException  {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(removeComment);

        HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response;


    }





    private String getJsonReview() throws URISyntaxException, IOException, InterruptedException  {
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://"+ this.link+":8080/antons-skafferi-db-1.0-SNAPSHOT/api/review"))
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
