package com.example.android.sfwhf1;
//Written by Asami
import android.net.Uri;

/**
 * Created by pumehana on 9/19/17.
 */

public class User {

    private String name;
    private double height;
    private double weight;
    private int birth;
    private int gender;
    private int bmi;
    private Uri image;



    public User(){};

    public User(String name, double height, double weight, int birth, int gender, int bmi, Uri image){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.birth = birth;
        this.gender = gender;
        this.bmi = bmi;
        this.image = image;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
