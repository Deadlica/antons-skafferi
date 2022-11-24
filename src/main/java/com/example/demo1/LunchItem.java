package com.example.demo1;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * TODO: Lägga till så att denna fetchar datat från DBn i sin konstruktor!
 */
@Named(value = "LunchItem")
@SessionScoped
public class LunchItem extends HttpServlet {

    public LunchItem() {
        /*
         * Lägg till fetching till DB.
         *
         */
    }

    private class Item {
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

    private ArrayList<Item> list = new ArrayList<Item>();

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