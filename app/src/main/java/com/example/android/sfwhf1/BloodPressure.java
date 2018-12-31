package com.example.android.sfwhf1;
//Written by Asami on 10/5

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

public class BloodPressure extends AppCompatActivity implements View.OnClickListener{

    private EditText highBP;
    private EditText lowBP;
    private TextView next;
    private TextView back;
    private ImageButton backMenu;
    //setting up today's date
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        setTitle("Blood Pressure");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        highBP = (EditText) findViewById(R.id.high_body_pressure);
        lowBP = (EditText) findViewById(R.id.low_body_pressure);
        next = (TextView) findViewById(R.id.next);
        back = (TextView) findViewById(R.id.back);
        backMenu = (ImageButton) findViewById(R.id.backMenu);
        //Setting up Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        next.setOnClickListener(this);
        back.setOnClickListener(this);
        backMenu.setOnClickListener(this);

        //Showing data if there are data on Firebase
        mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("High BP").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String hBP = dataSnapshot.getValue().toString();
                if(hBP.equals("0")){

                }else{
                    highBP.setText(hBP);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Low BP").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String lBP = dataSnapshot.getValue().toString();
                    if (lBP.equals("0")) {

                    } else {
                        lowBP.setText(lBP);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    //Save the data on Firebase
    private void saveData(String name) {
        //get input data from activity
        final String high_BP = highBP.getText().toString();
        final String low_BP = lowBP.getText().toString();
        boolean result;
        if (!high_BP.isEmpty() && !low_BP.isEmpty()) {
            int highBP_int = Integer.parseInt(high_BP);
            int lowBP_int = Integer.parseInt(low_BP);
            result = checkTwoRange(highBP_int, lowBP_int);
            if(result == true){
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("High BP").setValue(highBP_int);
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Low BP").setValue(lowBP_int);
                activityCall(name);
            }else {
                dialog();
            }
        }
        if (high_BP.isEmpty() && !low_BP.isEmpty()) {
            int lowBP_int = Integer.parseInt(low_BP);
            boolean lBP = checkOneRange(lowBP_int);
            if(lBP == true){
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("High BP").setValue(0);
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Low BP").setValue(lowBP_int);
                activityCall(name);
            }else{
                dialog();
            }
        }
        if (!high_BP.isEmpty() && low_BP.isEmpty()) {
            int highBP_int = Integer.parseInt(high_BP);
            boolean hBP = checkOneRange(highBP_int);
            if(hBP == true){
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("High BP").setValue(highBP_int);
                mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Low BP").setValue(0);
                activityCall(name);
            }else{
                dialog();
            }
        }if(high_BP.isEmpty() && low_BP.isEmpty()){
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("High BP").setValue(0);
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Low BP").setValue(0);
            activityCall(name);
        }

    }
    //Checking range: if the number is higher than 300, showing dialog box and let users input the data again.
    //if highBP is larger than lowBP, showing dialog too.
    private boolean checkTwoRange(int bp, int lb){
        boolean result;
        if(bp >= 300 || lb >= 300){
            result = false;
        }
        else if(bp < lb){
            result = false;
        }
        else if(bp == lb){
            result = false;
        }else{
            result = true;
        }
        return result;
    }
    private boolean checkOneRange(int v){
        if(v >= 300){
            return false;
        }else{
            return true;
        }
    }
    private void dialog(){
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Range is not correct. Please check your input again.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                }).create().show();
    }

    //Intent heartRate
    private void callHeartRate(){
        startActivity(new Intent(BloodPressure.this, heartRate.class));
    }
    //Intent DailyVital
    private void callBodyTemp(){
        startActivity(new Intent(BloodPressure.this, bodyTemp.class));
    }
    private void callVitalMenu(){ startActivity(new Intent(BloodPressure.this, DailyVitalMenu.class));}


    //Calling next activity
    private void activityCall(String name){
        if(name.equals("next")){
            callHeartRate();
        }if(name.equals("back")){
            callBodyTemp();
        }if(name.equals("backMenu")){
            callVitalMenu();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == next){
            saveData("next");
        }
        if(view == back){
            saveData("back");
        }
        if(view == backMenu){
            saveData("backMenu");

        }

    }
}
