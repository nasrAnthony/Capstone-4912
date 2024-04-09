package com.example.mydumbelldore;

public class Exercise {

    public String name, intensityLevel, experienceLevel, description;
    public int imgID;

    public Exercise(){ //blank constructor.
    }
    public Exercise(String name, String intensityLevel, String experienceLevel, String description, int imgID){
        this.name = name;
        this.intensityLevel = intensityLevel;
        this.experienceLevel = experienceLevel;
        this.description = description;
        this.imgID = imgID;
    }


}
