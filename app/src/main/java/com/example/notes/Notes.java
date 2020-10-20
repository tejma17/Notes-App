package com.example.notes;

public class Notes {
    private String name;
    private String date;

    public Notes(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return date;
    }

    public void setDescription(String description) {
        this.date = description;
    }
}
