package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class DailyGoal extends AppCompatActivity implements View.OnClickListener {

    private ImageButton fitness;
    private ImageButton intake;
    private ImageButton another;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal);
        setTitle("Daily Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fitness = (ImageButton) findViewById(R.id.fitness);
        intake = (ImageButton) findViewById(R.id.intake);
        another = (ImageButton) findViewById(R.id.another);
        back = (ImageButton) findViewById(R.id.backMain);

        fitness.setOnClickListener(this);
        intake.setOnClickListener(this);
        another.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == fitness){
            startActivity(new Intent(this, DailyGoalFitness.class));
        }
        if(view == intake){
            startActivity(new Intent(this, DailyGoalIntake.class));
        }
        if(view == another){
            startActivity(new Intent(this, DailyGoalAnother.class));
        }
        if(view == back){
            startActivity(new Intent(this, DailyVitalMenu.class));
        }
    }
}
