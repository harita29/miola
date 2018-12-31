package com.example.android.sfwhf1;

/**
 * Created by bedryabalema on 11/3/17.
 */

public class Workout {
    public Double duration;
    private Double calBurnt;
    //private Double totCalBurnt;

    public Workout(Double duration, Double calBurnt) {
        this.duration = duration;
        this.calBurnt = calBurnt;
    }

    public Workout(Double calBurnt) {
        this.calBurnt = calBurnt;
    }

    public Double getDuration() {
        return duration;
    }

    public Double getCalBurnt() {
        return calBurnt;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setCalBurnt(Double calBurnt) {
        this.calBurnt = calBurnt;
    }

}

