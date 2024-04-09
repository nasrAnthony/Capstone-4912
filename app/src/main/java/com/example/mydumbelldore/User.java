package com.example.mydumbelldore;

public class User {
    public String fullName, email, weight, height, activityLevel, age, gender;

    public User(){ //blank constructor.

    }
    public User(String fullName, String email, String weight, String height, String activityLvl, String age, String gender){
        this.fullName = fullName;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.activityLevel = activityLvl;
        this.age = age;
        this.gender =  gender;
    }
}
