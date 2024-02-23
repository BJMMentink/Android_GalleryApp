package edu.bjm.galleryapp;

public class Game {
    public Game(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setCapital(String description) {
        this.description = description;
    }

    private String name;
    private String description;
}
