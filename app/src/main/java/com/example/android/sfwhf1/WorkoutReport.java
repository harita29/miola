package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WorkoutReport extends WorkoutCalender implements View.OnClickListener {

    private TextView walkCal;
    private TextView runCal;
    private TextView swimCal;
    private TextView bikeCal;
    private TextView pushupCal;
    private TextView crunchCal;
    private TextView weightLCal;
    private TextView userInputCal;
    private TextView calTot;
    private ImageButton workoutMenu;
    private ImageButton backMilog;


    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    private static DecimalFormat df2 = new DecimalFormat(".##");

    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_report);

        walkCal = (TextView) findViewById(R.id.walkV);
        runCal = (TextView) findViewById(R.id.runV);
        swimCal = (TextView) findViewById(R.id.swimV);
        bikeCal = (TextView) findViewById(R.id.bikeV);
        pushupCal = (TextView) findViewById(R.id.pushupV);
        crunchCal = (TextView) findViewById(R.id.crunchV);
        weightLCal = (TextView) findViewById(R.id.weightLV);
        userInputCal = (TextView)findViewById(R.id.userInputV);
        calTot = (TextView) findViewById(R.id.totalCalV);
        workoutMenu = (ImageButton) findViewById(R.id.workoutMenu);
        backMilog = (ImageButton) findViewById(R.id.backMilog);
        workoutMenu.setOnClickListener(this);
        backMilog.setOnClickListener(this);
        //Calls previous date
        final String selectedDate = getIntent().getStringExtra("selectedDate");

        if(selectedDate == null) {
            //current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(date.getTime());
            setTitle("Work Out: " + formattedDate);
        }else {
            //depending on date chosen, show data
            String selectedDay = getIntent().getStringExtra("Day");
            String selectedMonth = getIntent().getStringExtra("Month");
            String selectedYear = getIntent().getStringExtra("Year");

            setTitle("Work Out: " + selectedMonth +"/"+selectedDay+"/"+selectedYear);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Workout");


        if(selectedDate==null){
            mDatabase.child(currentDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double tot=0.0;
                    if (dataSnapshot.exists()) {
                        if(dataSnapshot.child("Walking Calorie Burnt").getValue(String.class)==null){
                            walkCal.setText("0 Calorie");}
                        else{
                            walkCal.setText( dataSnapshot.child("Walking Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Walking Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Running Calorie Burnt").getValue(String.class)==null){
                            runCal.setText("0 Calorie");
                        }else{
                            runCal.setText(dataSnapshot.child("Running Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Running Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class)==null){
                            swimCal.setText("0 Calorie");
                        }else{
                            swimCal.setText(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class)  + " Calories");
                            tot+= Double.parseDouble(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class)==null) {
                            bikeCal.setText("0 Calorie");
                        }else{
                            bikeCal.setText(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class)==null) {
                            pushupCal.setText("0 Calorie");
                        }else{
                            pushupCal.setText(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class)==null) {
                            crunchCal.setText("0 Calorie");
                        }else{
                            crunchCal.setText(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class)==null) {
                            weightLCal.setText("0 Calorie");
                        }else{
                            weightLCal.setText(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class)==null) {
                            userInputCal.setText("0 Calorie");
                        }else{
                            userInputCal.setText(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class));
                        }
                        calTot.setText(tot+ " Calories");
                        mDatabase.child(currentDate).child("Total Burnt Calorie").setValue(tot);
                        // Toast.makeText(WorkoutReport.this, ""+tot,Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else{
            mDatabase.child(selectedDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double tot=0.0;
                    if (dataSnapshot.exists()) {
                        if(dataSnapshot.child("Walking Calorie Burnt").getValue(String.class)==null){
                            walkCal.setText("0 Calorie");}
                        else{
                            walkCal.setText(dataSnapshot.child("Walking Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Walking Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Running Calorie Burnt").getValue(String.class)==null){
                            runCal.setText("0 Calorie");
                        }else{
                            runCal.setText(dataSnapshot.child("Running Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Running Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class)==null){
                            swimCal.setText("0 Calorie");
                        }else{
                            swimCal.setText(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+= Double.parseDouble(dataSnapshot.child("Swimming Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class)==null) {
                            bikeCal.setText("0 Calorie");
                        }else{
                            bikeCal.setText(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Bicycling Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class)==null) {
                            pushupCal.setText("0 Calorie");
                        }else{
                            pushupCal.setText(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Pushup Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class)==null) {
                            crunchCal.setText("0 Calorie");
                        }else{
                            crunchCal.setText(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("Crunch Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class)==null) {
                            weightLCal.setText("0 Calorie");
                        }else{
                            weightLCal.setText(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("WeightLifting Calorie Burnt").getValue(String.class));
                        }
                        if(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class)==null) {
                            userInputCal.setText("0 Calorie");
                        }else{
                            userInputCal.setText(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class)+ " Calories");
                            tot+=Double.parseDouble(dataSnapshot.child("UserInput").child("Total Calorie Burnt").getValue(String.class));
                        }
                        calTot.setText(tot+ " Calories");
                    }
                    else{
                        Toast.makeText(WorkoutReport.this, "No Records", Toast.LENGTH_LONG).show();

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }
    //when calender icon on action bar clicked,displays calender for workout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(WorkoutReport.this, WorkoutCalender.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == workoutMenu){
            startActivity(new Intent(this, WorkOutTypeActivity.class));
        }
        if(v == backMilog){
            startActivity(new Intent(this, milogMenu.class));
        }
    }
}