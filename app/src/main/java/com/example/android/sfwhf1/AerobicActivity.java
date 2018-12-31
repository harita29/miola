package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AerobicActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton walk;
    private ImageButton run;
    private ImageButton bike;
    private ImageButton swim;
    private ImageButton back;
    private Calendar date1 = Calendar.getInstance();
    private SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("MM/dd/yyyy");
    private String currentDateDisplay = dateFormatDisplay.format(date1.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aerobic);
        setTitle("Aerobics: " + currentDateDisplay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        walk = (ImageButton) findViewById(R.id.walk);
        run = (ImageButton)findViewById(R.id.run);
        bike = (ImageButton)findViewById(R.id.bike);
        swim = (ImageButton)findViewById(R.id.swim);
        back = (ImageButton)findViewById(R.id.back);

        walk.setOnClickListener(this);
        run.setOnClickListener(this);
        bike.setOnClickListener(this);
        swim.setOnClickListener(this);
        back.setOnClickListener(this);



    }
    //public double caloryBurn(int durations){
     //   return (BMR/24)*(MET)*(duations);
    //}


    @Override
    public void onClick(View view) {
        if(view == walk){
            startActivity(new Intent(this,WalkActivity.class));
        }
        if(view == run){
            startActivity(new Intent(this, RunActivity.class));
        }
        if(view == bike){
            startActivity(new Intent(this, BikeActivity.class));
        }
        if(view == swim){
            startActivity(new Intent(this, SwimActivity.class));
        }
        if(view == back){
            startActivity(new Intent(this, WorkOutTypeActivity.class));
        }
    }
}
