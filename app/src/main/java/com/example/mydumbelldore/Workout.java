package com.example.mydumbelldore;

import androidx.core.widget.TextViewOnReceiveContentListener;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    public String name;
    public int numberOfExercises, timeBetweenExercises;
    //public List<Exercise> exercises;
    public List<Exercise> exercises;


    public Workout(){ //blank constructor.

    }
    //public Workout(String name, int numberOfExercises, int timeBetweenExercises, List<Exercise> exercisesList){
    public Workout(String name, int numberOfExercises, int timeBetweenExercises, List<Exercise> exercisesList){
        this.name = name;
        this.numberOfExercises = numberOfExercises;
        this.timeBetweenExercises = timeBetweenExercises;
        this.exercises = exercisesList;

    }
}
