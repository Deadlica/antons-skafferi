package com.example.demo1;

public class Dish {
    private int id;
    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Dish() {
        this.name = "";
        this.type = "";
    }

    public Dish(int id, String name) {
        this.id = id;
        this.name = name;
        this.type = "";
    }

    @Override
    public String toString() {
        return this.name;
    }
}
