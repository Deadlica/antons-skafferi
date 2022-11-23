package com.example.demo1;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.Serializable;

@Named(value = "FoodItem")
@ViewScoped
public class FoodItem implements Serializable {
    private String name = "UwU";
    private int i = 0;
    private String uwu;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void add() {
        this.i += 1;
    }

    public FoodItem() {
    }

    public void test() {
        System.out.println("omg lade du till en?");
        setName("Name");
    }

    public FoodItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
