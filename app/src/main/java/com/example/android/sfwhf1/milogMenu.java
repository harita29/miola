package com.example.android.sfwhf1;
//modified by Asami

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class milogMenu extends AppCompatActivity implements View.OnClickListener {

    private ImageButton vital;
    private ImageButton intake;
    private ImageButton workout;
    //showing total calories values
    private TextView BMRshowData;
    private static Double bmr;
    private TextView burntCalsShowData;
    private static Double burnCalNum;
    private TextView intakeCalData;
    private static Double intakeCalNum;
    private TextView totalDayCalData;
    private static Double total;

    //back to a previous page view value
    private TextView back;
    //Create claendar instance
    private Calendar date = Calendar.getInstance();

    //getting current date
    SimpleDateFormat differentDateFormat = new SimpleDateFormat("MMddyyyy");
    final String currentDate = differentDateFormat.format(date.getTime());

    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milog_menu);
        setTitle("Daily MiLog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vital = (ImageButton) findViewById(R.id.vitalBtn);
        intake = (ImageButton) findViewById(R.id.intakeBtn);
        workout = (ImageButton) findViewById(R.id.workoutBtn);
        BMRshowData = (TextView) findViewById(R.id.BMRshowData);
        burntCalsShowData = (TextView) findViewById(R.id.burntCalsShowData);
        intakeCalData = (TextView) findViewById(R.id.intakeCalData);
        totalDayCalData = (TextView) findViewById(R.id.totalDayCalData);
        back = (TextView) findViewById(R.id.back);

        //Firebase sets up
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        //Retriving each data (BMR, burnt Cal, intake Cal) from Firebase
        //Retriving BMR
        mDatabase.child("Daily Vital").child(currentDate).child("BMR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //if there is a data, showing the value and convert from String to Double
                    bmr = Double.parseDouble(dataSnapshot.getValue().toString());
                    BMRshowData.setText(""+bmr);
                }else{
                    //if there is no data, to show "No data" and sets "0.0" as the value
                    BMRshowData.setText("No data");
                    bmr= 0.0;
                }
                //Retriving intake Cals
                mDatabase.child("Food Log").child(currentDate).child("Total Calories").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //if there is a data, showing the value and convert from String to Double
                            intakeCalNum = Double.parseDouble(dataSnapshot.getValue().toString());
                            intakeCalData.setText("" + intakeCalNum);
                        } else {
                            //if there is no data, to show "No data" and sets "0.0" as the value
                            intakeCalData.setText("No data");
                            intakeCalNum = 0.0;
                        }
                        //Retriving burnt Cals
                        mDatabase.child("Workout").child(currentDate).child("Total Burnt Calorie").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    //if there is a data, showing the value and convert from String to Double
                                    burnCalNum = Double.parseDouble(dataSnapshot.getValue().toString());
                                    burntCalsShowData.setText(""+burnCalNum);
                                }else{
                                    //if there is no data, to show "No data" and sets "0.0" as the value
                                    burntCalsShowData.setText("No data");
                                    burnCalNum = 0.0;
                                }
                                //Calculate total daily calories
                                total = (intakeCalNum) - (bmr + burnCalNum);
                                totalDayCalData.setText(""+total);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        vital.setOnClickListener(this);
        intake.setOnClickListener(this);
        workout.setOnClickListener(this);
        back.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        //if user wants to check a daily vital, jump to summary of vitals
        if(view == vital){
            startActivity(new Intent(this, MiLog.class));
        }
        //if user wants to check intake, jump to summary of intakes
        if(view == intake){
            startActivity(new Intent(this, DisplayFoodItems.class));
        }
        //if user wants to check workout, jump to summary of workout
        if(view == workout) {
            startActivity(new Intent(this, WorkoutReport.class));
        }
        if(view == back){
            startActivity(new Intent(this, Main.class));
        }
    }
}
