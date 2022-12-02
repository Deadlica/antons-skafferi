package com.example.demo1;

import java.io.IOException;
import java.net.URISyntaxException;

public class URL {
    public URL() throws IOException, URISyntaxException, InterruptedException {
        this.link = "10.82.231.15"; //School
        //this.link = "89.233.229.182"; //Home
        //this.link = "31.209.47.252"; //Can
    }

    public String getLink() {
        return link;
    }

    private String link;
}
