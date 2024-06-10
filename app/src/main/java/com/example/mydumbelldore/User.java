package com.example.mydumbelldore;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName, email, weight, height, activityLevel, age, gender;
    public List<Workout> userWorkouts; //hold the workout ids.

    public User(){ //blank constructor.

    }

    public User(String fullName, String email, String weight, String height, String activityLvl, String age, String gender, List<Workout> userWorkouts){
    //public User(String fullName, String email, String weight, String height, String activityLvl, String age, String gender){
        this.fullName = fullName;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.activityLevel = activityLvl;
        this.age = age;
        this.gender =  gender;
        this.userWorkouts = userWorkouts;

    }


}
