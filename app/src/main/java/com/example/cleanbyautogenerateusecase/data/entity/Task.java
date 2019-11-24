package com.example.cleanbyautogenerateusecase.data.entity;

public class Task {
    private int id;
    private String name;

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task() {
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

    public void setName(String name) {
        this.name = name;
    }
}
