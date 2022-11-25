package com.example.demo1;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;


/**
 * TODO: Lägga till så att denna fetchar datat från DBn i sin konstruktor!
 */
@Named(value = "LunchItems")
@SessionScoped
public class LunchItems extends HttpServlet {

    public LunchItems() {
        /*
         * Lägg till fetching till DB.
         *
         */
    }

    private static class Item {
        private String name;
        private String desc;
        private double price;
        private String date;

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private ArrayList<Item> list = new ArrayList<>();

    public ArrayList<Item> getList() {
        return list;
    }

    public void setList(ArrayList<Item> list) {
        this.list = list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}