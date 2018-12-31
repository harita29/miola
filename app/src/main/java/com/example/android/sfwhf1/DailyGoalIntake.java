package com.example.android.sfwhf1;
//Written by Asami on 10/22

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyGoalIntake extends AppCompatActivity {

    private EditText calories;
    private ImageButton submit;
    private ImageButton back;
    //Getting current date
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal_intake);
        setTitle("Daily Goal Intake");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calories = (EditText) findViewById(R.id.calories);
        submit = (ImageButton) findViewById(R.id.next);
        back = (ImageButton) findViewById(R.id.backMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DailyGoalIntake.this, DailyGoal.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cal = calories.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                uID = mUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital").child(currentDate).child("Goal");

                if (cal.isEmpty()) {
                    Toast.makeText(DailyGoalIntake.this, "Please enter calories", Toast.LENGTH_SHORT).show();
                } else {
                    int totalCal = Integer.parseInt(cal);
                    mDatabase.child("Num").setValue("2");
                    mDatabase.child("Calories").setValue(totalCal);
                    startActivity(new Intent(DailyGoalIntake.this, DailyVitalMenu.class));
                }
            }
        });
    }
}
