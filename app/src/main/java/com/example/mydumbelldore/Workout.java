package com.example.mydumbelldore;

import androidx.core.widget.TextViewOnReceiveContentListener;

import java.util.ArrayList;

public class Workout {
    public String name;
    public int numberOfExercises, timeBetweenExercises;
    public ArrayList<String> exerciseNames;

    public Workout(){ //blank constructor.

    }
    public Workout(String name, int numberOfExercises, int timeBetweenExercises, ArrayList<String> exerciseNames){
        this.name = name;
        this.numberOfExercises = numberOfExercises;
        this.timeBetweenExercises = timeBetweenExercises;
        this.exerciseNames = exerciseNames;

    }
}
