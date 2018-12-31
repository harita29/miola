package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class StrengthActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton pushUp;
    private ImageButton crunch;
    private ImageButton weightLift;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pushUp = (ImageButton)findViewById(R.id.pushup);
        crunch = (ImageButton)findViewById(R.id.crunch);
        weightLift = (ImageButton)findViewById(R.id.weightlift);
        back = (ImageButton)findViewById(R.id.back);

        pushUp.setOnClickListener(this);
        crunch.setOnClickListener(this);
        weightLift.setOnClickListener(this);
        back.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if(view == pushUp){
            startActivity(new Intent(this,PushupActivity.class));
        }
        if(view == crunch){
            startActivity(new Intent(this,CrunchActivity.class));
        }
        if(view == weightLift){
            startActivity(new Intent(this,WeightLiftingActivity.class));
        }
        if(view == back){
            startActivity(new Intent(this,WorkOutTypeActivity.class));
        }
    }
}
