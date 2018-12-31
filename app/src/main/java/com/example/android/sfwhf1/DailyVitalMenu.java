package com.example.android.sfwhf1;

//Written by Asami

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DailyVitalMenu extends AppCompatActivity implements View.OnClickListener {

    private ImageButton bodyTemp;
    private ImageButton heartRate;
    private ImageButton bloodPress;
    private ImageButton weight;
    private ImageButton goal;
    private ImageButton miLog;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_vital_menu);
        setTitle(("DAILY VITALS"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bodyTemp = (ImageButton) findViewById(R.id.bodyTemp);
        heartRate = (ImageButton) findViewById(R.id.heartRate);
        bloodPress = (ImageButton) findViewById(R.id.bloodPress);
        weight = (ImageButton) findViewById(R.id.weight);
        goal = (ImageButton) findViewById(R.id.goal);
        miLog = (ImageButton) findViewById(R.id.miLog);
        back = (TextView) findViewById(R.id.backMain);

        back.setOnClickListener(this);
        bodyTemp.setOnClickListener(this);
        heartRate.setOnClickListener(this);
        bloodPress.setOnClickListener(this);
        goal.setOnClickListener(this);
        miLog.setOnClickListener(this);
        weight.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == bodyTemp){
            startActivity(new Intent(this,bodyTemp.class));
        }
        if(view == heartRate){
            startActivity(new Intent(this, heartRate.class));
        }
        if(view == bloodPress){
            startActivity(new Intent(this, BloodPressure.class));
        }
        if(view == weight){
            startActivity(new Intent(this, weight.class));
        }
        if(view == goal){
            startActivity(new Intent(this, DailyGoal.class ));
        }
        if(view == miLog){
            startActivity(new Intent(this, MiLog.class));
        }
        if(view == back){
            startActivity(new Intent(this, Main.class));
        }

    }
}
