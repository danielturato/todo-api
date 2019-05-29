package com.teamtreehouse.techdegrees.models;

public class Todo {
    private int id;
    private String name;
    private Boolean isCompleted;

    public Todo(String name, Boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }
}
