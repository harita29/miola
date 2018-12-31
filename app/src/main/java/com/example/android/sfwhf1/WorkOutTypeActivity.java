package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WorkOutTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton aerobics;
    private ImageButton strength;
    private ImageButton report;
    private EditText userInput;
    private ImageButton uSubmit;
    private TextView back;

    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());

    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aerobics = (ImageButton)findViewById(R.id.aerobic);
        strength = (ImageButton)findViewById(R.id.strength);
        report = (ImageButton)findViewById(R.id.report);
        userInput = (EditText)findViewById(R.id.userinput);
        uSubmit = (ImageButton)findViewById(R.id.uSubmit);
        back = (TextView)findViewById(R.id.back);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Workout").child(currentDate);


        aerobics.setOnClickListener(this);
        strength.setOnClickListener(this);
        report.setOnClickListener(this);
        back.setOnClickListener(this);
        uSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calSave();
            }
        });

    }


    private void calSave() {
        final String userInputCal = userInput.getText().toString().trim();
        if(!TextUtils.isEmpty(userInputCal)){
            mDatabase.child("UserInput").child("Total Calorie Burnt").setValue(userInputCal);
            userInput.setText("");

        }
    }

    @Override
    public void onClick(View view) {
        if (view == aerobics){
            startActivity(new Intent(this, AerobicActivity.class));
        }
        if(view == strength){
            startActivity(new Intent(this, StrengthActivity.class));
        }
        if(view == report){
            startActivity(new Intent(this, WorkoutReport.class));
        }
        if(view == back){
            startActivity(new Intent(this, Main.class));
        }
    }

}
