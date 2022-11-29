package com.example.demo1;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named(value = "StaffBean")
@RequestScoped
public class StaffBean implements Serializable {

}
