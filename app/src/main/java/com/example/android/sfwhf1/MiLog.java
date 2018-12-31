package com.example.android.sfwhf1;
//Written by Asami on 10/22

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MiLog extends DisplayCalendar implements View.OnClickListener {


    private TextView bodyTemp;
    private TextView heartRate;
    private TextView highBP;
    private TextView lowBP;
    private TextView weight;
    private TextView bmr;
    private TextView dailyGoal;
    private TextView bmi;
    private TextView extra;
    private ImageButton backMain;
    private TextView rewards;
    //Create claendar instance
    private Calendar date = Calendar.getInstance();
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase, mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_log);

        //For displaying the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(date.getTime());
        SimpleDateFormat differentDateFormat = new SimpleDateFormat("MMddyyyy");
        final String currentDate = differentDateFormat.format(date.getTime());

        //Display date//

        //Calling date from previous activity
        final String selectedDate = getIntent().getStringExtra("selectedDate");

        if (selectedDate == null) {
            //if date is not chosen, showing currentdate
            setTitle("Daily Vitals: " + formattedDate);
            //selectedDate = null;
        } else {
            //if date is chosen, retrieve the date from intent
            String selectedDay = getIntent().getStringExtra("Day");
            String selectedMonth = getIntent().getStringExtra("Month");
            String selectedYear = getIntent().getStringExtra("Year");

            //Setting the title with selected date
            setTitle("Daily Vitals: " + selectedMonth + "/" + selectedDay + "/" + selectedYear);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bodyTemp = (TextView) findViewById(R.id.bodyTemp);
        heartRate = (TextView) findViewById(R.id.heratRate);
        highBP = (TextView) findViewById(R.id.highBP);
        lowBP = (TextView) findViewById(R.id.lowBP);
        weight = (TextView) findViewById(R.id.weight);
        bmr = (TextView) findViewById(R.id.bmr);
        bmi = (TextView) findViewById(R.id.bmi);
        rewards = (TextView) findViewById(R.id.rewards);
        dailyGoal = (TextView) findViewById(R.id.dailyGoal);
        extra = (TextView) findViewById(R.id.extra);
        backMain = (ImageButton) findViewById(R.id.backMain);
        backMain.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("UserProfile");


        if(selectedDate == null) {
            mDatabase.child(currentDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final String temp = dataSnapshot.child("Body Temp").getValue().toString();
                        final String heart = dataSnapshot.child("Heart Rate").getValue().toString();
                        final String high = dataSnapshot.child("High BP").getValue().toString();
                        final String low = dataSnapshot.child("Low BP").getValue().toString();
                        final String w = dataSnapshot.child("Weight").getValue().toString();
                        final double dweight = Double.parseDouble(dataSnapshot.child("Weight").getValue().toString());

                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int age = Integer.parseInt(dataSnapshot.child("Age").getValue().toString());
                                double height = Double.parseDouble(dataSnapshot.child("Height").getValue().toString());
                                String measurement = dataSnapshot.child("Measurement").getValue().toString();
                                String gender = dataSnapshot.child("Gender").getValue().toString();
                                if (!w.equals("0")) {
                                    String bmrResult = calculateBMR(dweight, height, age, measurement, gender);
                                    String bmiResult = calculateBMI(dweight, height, measurement);
                                    mDatabase.child(currentDate).child("BMI").setValue(bmiResult);
                                    mDatabase.child(currentDate).child("BMR").setValue(bmrResult);
                                    bodyTemp.setText(temp);
                                    heartRate.setText(heart);
                                    highBP.setText(high);
                                    lowBP.setText(low);
                                    int pointValue = checkReward(temp, heart, high, low, w);
                                    rewards.setText(""+pointValue);
                                    mDatabase.child(currentDate).child("Rewards").setValue(pointValue);
                                    if (measurement.equals("kg/cm")) {
                                        weight.setText(w + " kg");
                                    } else {
                                        weight.setText(w + " lb");
                                    }
                                    bmr.setText(bmrResult + " cal");
                                    bmi.setText(bmiResult);
                                }else{
                                    bmr.setText("");
                                    bmi.setText("");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if (dataSnapshot.child("Goal").child("Num").exists()) {
                            String checkDailyGoalNum = dataSnapshot.child("Goal").child("Num").getValue().toString();
                            //Fitness
                            if (checkDailyGoalNum.equals("1")) {
                                String goalFitnessType = dataSnapshot.child("Goal").child("Type").getValue().toString();
                                String goalFitnessAmount = dataSnapshot.child("Goal").child("Amount").getValue().toString();
                                dailyGoal.setText(goalFitnessType);
                                extra.setText("Amount: " + goalFitnessAmount);
                            }
                            //Intake
                            if (checkDailyGoalNum.equals("2")) {
                                String goalIntake = dataSnapshot.child("Goal").child("Calories").getValue().toString();
                                dailyGoal.setText("Toal intake calories");
                                extra.setText("The amount is " + goalIntake + " Calories");
                            }
                            //Another
                            if (checkDailyGoalNum.equals("3")) {
                                String goalAnother = dataSnapshot.child("Goal").child("Detail").getValue().toString();
                                dailyGoal.setText("Create own");
                                extra.setText(goalAnother);
                            }
                        }
                    }else {
                        bodyTemp.setText("");
                        heartRate.setText("");
                        highBP.setText("");
                        lowBP.setText("");
                        weight.setText("");
                        bmr.setText("");
                        bmi.setText("No Data");
                        rewards.setText("");
                        //if a user doesn't set up his/her daily goal, return "Not daily goal".
                        dailyGoal.setText("Not setting up");
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
                    if(dataSnapshot.exists()) {
                        final String temp = dataSnapshot.child("Body Temp").getValue().toString();
                        final String heart = dataSnapshot.child("Heart Rate").getValue().toString();
                        final String high = dataSnapshot.child("High BP").getValue().toString();
                        final String low = dataSnapshot.child("Low BP").getValue().toString();
                        final String w = dataSnapshot.child("Weight").getValue().toString();
                        final double dweight = Double.parseDouble(dataSnapshot.child("Weight").getValue().toString());
                        String reward = dataSnapshot.child("Rewards").getValue().toString();

                        bodyTemp.setText(temp);
                        heartRate.setText(heart);
                        highBP.setText(high);
                        lowBP.setText(low);
                        weight.setText(w);
                        rewards.setText(reward);
                        //Checking BMR exists or not
                        mDatabase.child(selectedDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if no data on Firebase
                                if (!dataSnapshot.child("BMR").exists()) {
                                    mDatabase2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int age = Integer.parseInt(dataSnapshot.child("Age").getValue().toString());
                                            double height = Double.parseDouble(dataSnapshot.child("Height").getValue().toString());
                                            String measurement = dataSnapshot.child("Measurement").getValue().toString();
                                            String gender = dataSnapshot.child("Gender").getValue().toString();

                                            if (w.equals("0")) {
                                                bmr.setText("");
                                                mDatabase.child("Daily Vital").child("BMR").setValue(0);
                                            } else {
                                                String bmrResult = calculateBMR(dweight, height, age, measurement, gender);
                                                mDatabase.child("Daily Vital").child("BMR").setValue(bmr);
                                                bmr.setText(bmrResult + " cal");
                                            }
                                            if (measurement.equals("kg/cm")) {
                                                weight.setText(w + " kg");
                                            } else {
                                                weight.setText(w + " lbs");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    String br = dataSnapshot.child("BMR").getValue().toString();
                                    bmr.setText(br + " cal");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //Checking BMI exists or not
                        mDatabase.child(selectedDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if no data on Firebase
                                if (!dataSnapshot.child("BMI").exists()) {
                                    mDatabase2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            double height = Double.parseDouble(dataSnapshot.child("Height").getValue().toString());
                                            String measurement = dataSnapshot.child("Measurement").getValue().toString();

                                            if (w.equals("0")) {
                                                bmi.setText("No weight data");
                                                mDatabase.child("Daily Vital").child("BMI").setValue(0);
                                            } else {
                                                String bmiResult = calculateBMI(dweight, height, measurement);
                                                bmi.setText(bmiResult);
                                            }
                                            if (measurement.equals("kg/cm")) {
                                                weight.setText(w + " kg");
                                            } else {
                                                weight.setText(w + " lbs");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    //if data exists
                                    String bi = dataSnapshot.child("BMI").getValue().toString();
                                    bmi.setText(bi);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if (dataSnapshot.child("Goal").child("Num").exists()) {
                            String checkDailyGoalNum = dataSnapshot.child("Goal").child("Num").getValue().toString();
                            //Fitness
                            if (checkDailyGoalNum.equals("1")) {
                                String goalFitnessType = dataSnapshot.child("Goal").child("Type").getValue().toString();
                                String goalFitnessAmount = dataSnapshot.child("Goal").child("Amount").getValue().toString();
                                dailyGoal.setText(goalFitnessType);
                                extra.setText("Amount: " + goalFitnessAmount);
                            }
                            //Intake
                            if (checkDailyGoalNum.equals("2")) {
                                String goalIntake = dataSnapshot.child("Goal").child("Calories").getValue().toString();
                                dailyGoal.setText("Total intake calories");
                                extra.setText("The amount is " + goalIntake + " Calories");
                            }
                            //Another
                            if (checkDailyGoalNum.equals("3")) {
                                String goalAnother = dataSnapshot.child("Goal").child("Detail").getValue().toString();
                                dailyGoal.setText("Create own");
                                extra.setText(goalAnother);
                            }
                        }
                    }else{
                        Toast.makeText(MiLog.this, "No Records", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //calcurate BMR
    private String calculateBMR(double weight, double height, int age, String measurement, String gender){
        //only getting year for calculating BMI
        final int year = date.get(Calendar.YEAR);
        int currentAgeYear = year - age;
        //initialized bmi as double
        double bmrD = 0.0;
        String bmr = "";

        if(measurement.equals("kg/cm")){
            if(gender.equals("Female")){
                bmrD = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * currentAgeYear);
            }
            if(gender.equals("Male")){
                bmrD = 66 + (13.7 * weight) + (5 * height) - (6.8 * currentAgeYear);
            }
        }
        if(measurement.equals("lb/inch")){
            if(gender.equals("Female")){
                bmrD = 655 + (4.35 * weight) + (4.7 * height) - (4.7 * currentAgeYear);
            }
            if(gender.equals("Male")){
                bmrD = 66 + (6.23 * weight) + (12.7 * height) - (6.8 * currentAgeYear);
            }
        }
        //convert double to int
        int bmrI = (int) bmrD;
        //convert int to String
        bmr = String.valueOf(bmrI);
        return bmr;
    }
    //Calculating BMI
    private String calculateBMI(double weight, double height, String measurement){
        double bmiNum = 0.0;
        String bmi = "";

        if(measurement.equals("kg/cm")){
            height = height * 0.01;
            bmiNum = weight / (height * height);
        }
        if(measurement.equals("lb/inch")){
            bmiNum = (weight / (height * height)) * 703;
        }
        bmi = resultBMI(bmiNum);
        return bmi;
    }
    //Convert BMR number to each categolry
    private String resultBMI(double bmi){
        String comment = "";
        if(bmi == 0){
            comment = "No data";
        }
        if(bmi < 15.0){
            comment = "Very severely underweight";
        }
        if(bmi > 15 && bmi < 16 ){
            comment = "Severely underweight";
        }
        if(bmi > 16 && bmi < 18.5){
            comment = "Underweight";
        }
        if(bmi > 18.5 && bmi < 25){
            comment = "Normal (healthy wiehgt)";
        }
        if(bmi > 25 && bmi < 30){
            comment = "Overweight";
        }
        if(bmi > 30 && bmi < 35){
            comment = "Obeses Class 1 (Moderately obese)";
        }
        if(bmi > 35 && bmi < 40){
            comment = "Obeses Class 2 (Severely obese)";
        }
        if(bmi > 40){
            comment = "Obese Class 3 (Vere severely obese)";
        }
        return comment;
    }

    private int checkReward(String bodyTemp, String HbloodP, String LbloodP, String heart, String weight){
        int point = 0;
        if(!bodyTemp.equals("0")){
            point += 1;
        }
        if(!HbloodP.equals("0")){
            point += 1;
        }
        if(!LbloodP.equals("0")){
            point += 1;
        }
        if(!heart.equals("0")){
            point += 1;
        }
        if(!weight.equals("0")){
            point += 1;
        }
        return point;
    }
    @Override
    public void onClick(View view) {
        if(view == backMain){
            startActivity(new Intent(this, milogMenu.class));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }
    //If user click a calendar icon, jump to the DailyVitalCalendar.class
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(MiLog.this, DailyVitalCalendar.class));
        return super.onOptionsItemSelected(item);
    }


}
