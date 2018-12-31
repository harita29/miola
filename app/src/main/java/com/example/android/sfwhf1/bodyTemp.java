package com.example.android.sfwhf1;
//Writen by Asami on 10/5

import android.content.Intent;
import android.os.Bundle;
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

public class bodyTemp extends AppCompatActivity implements View.OnClickListener {

    private EditText bodyTemp;
    private TextView next;
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
        setContentView(R.layout.activity_body_temp);
        setTitle("Body Temperature");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bodyTemp = (EditText) findViewById(R.id.bodyTemp);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uID = mUser.getUid();
        next = (TextView) findViewById(R.id.next);
        back = (ImageButton) findViewById(R.id.backMain);

        next.setOnClickListener(this);
        back.setOnClickListener(this);

        mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Body Temp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bodyT = dataSnapshot.getValue().toString();
                //If the data is "0", do nothing
                if(bodyT.equals("0")){
                }else{
                    //if there is already data, show the value
                    bodyTemp.setText(bodyT);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





    //Save the data on Firebase
    private void saveData() {
        String temp = bodyTemp.getText().toString().trim();
        if(!temp.isEmpty()) {
            //Getting user input as double
            double temp_double = Double.parseDouble(temp);
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Body Temp").setValue(temp_double);
        }else{
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Body Temp").setValue(0);
        }
    }

    private void callBloodPressure(){
        startActivity(new Intent(this, BloodPressure.class));
    }
    private void callDailyVitalMenu(){
        startActivity(new Intent(bodyTemp.this, DailyVitalMenu.class));
    }

    @Override
    public void onClick(View view) {
        if(view == next){
            //Call keepData() which save user's input
            saveData();
            //call next activity, BloodPressure.class
            callBloodPressure();
        }
        if(view == back){
            saveData();
            //call previous activity, DailyVitalMenu
            callDailyVitalMenu();
        }
    }
}
