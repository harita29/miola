package com.example.android.sfwhf1;
//Written by Asami on 10/22

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyGoalFitness extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton button;
    private ImageButton submit;
    private ImageButton back;
    private EditText amount;
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
        setContentView(R.layout.activity_daily_goal_fitness);
        setTitle("Daily Goal Fitness");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkRadioButton();
        back = (ImageButton) findViewById(R.id.backMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DailyGoalFitness.this, DailyGoal.class));
            }
        });

    }

    private void checkRadioButton(){
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        submit = (ImageButton) findViewById(R.id.next);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = radioGroup.getCheckedRadioButtonId();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                uID = mUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital").child(currentDate).child("Goal");

                button = (RadioButton) findViewById(selected);
                amount = (EditText) findViewById(R.id.amount);
                String amountOfDailyGoal = amount.getText().toString().trim();
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(DailyGoalFitness.this, "Please select one activity", Toast.LENGTH_SHORT).show();
                }else {
                    if (amountOfDailyGoal.isEmpty()) {
                        Toast.makeText(DailyGoalFitness.this, "Please enter amout of your goal", Toast.LENGTH_SHORT).show();
                    } else {

                        mDatabase.child("Num").setValue("1");
                        mDatabase.child("Type").setValue(button.getText().toString());
                        mDatabase.child("Amount").setValue(amountOfDailyGoal);
                        startActivity(new Intent(DailyGoalFitness.this, DailyVitalMenu.class));
                    }
                }

            }
        });
    }
}
