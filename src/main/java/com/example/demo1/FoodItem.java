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
    public FoodItem() {
    }

    public void test() {
        System.out.println("omg lade du till en?");
    }


}
