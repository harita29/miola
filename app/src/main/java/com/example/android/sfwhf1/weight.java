package com.example.android.sfwhf1;
//written by Asami on Oct 6

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

public class weight extends AppCompatActivity implements View.OnClickListener {

    private EditText weight;
    private TextView next;
    private TextView back;
    private ImageButton backMenu;
    private static String measure;

    //Setting up the current date
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
        setContentView(R.layout.activity_weight);
        setTitle("Daily Weight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weight = (EditText) findViewById(R.id.weight);
        next = (TextView) findViewById(R.id.next);
        back = (TextView) findViewById(R.id.back);
        backMenu = (ImageButton) findViewById(R.id.backMenu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uID = mUser.getUid();

        mDatabase.child("Users").child(uID).child("UserProfile").child("Measurement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                measure = dataSnapshot.getValue().toString();

                if(measure.equals("kg/cm")){
                    weight.setHint("Weight (kg)");
                }
                else{
                    weight.setHint("Weight (lbs)");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Showing data if there is on Firebase
        mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Weight").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String w = dataSnapshot.getValue().toString();
                if (w.equals("0")) {
                }else{
                    if(measure.equals("kg/cm")){
                        weight.setText(w + " kg");
                    }else {
                        weight.setText(w + " lbs");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        backMenu.setOnClickListener(this);
        back.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    //Saving data on Firebase
    private void saveData(){
        String uWeight = weight.getText().toString().trim();
        if(!uWeight.isEmpty()) {
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Weight").setValue(uWeight);
        }else{
            mDatabase.child("Users").child(uID).child("Daily Vital").child(currentDate).child("Weight").setValue(0);
        }
    }

    //Intent DailyGoal.class
    private void callDailyGoal(){
        startActivity(new Intent(this, DailyGoal.class));
    }
    //Intent heartRate
    private void callHeartRate(){
        startActivity(new Intent(weight.this, heartRate.class));
    }

    private void callVitalMenu(){
        startActivity(new Intent(weight.this, DailyVitalMenu.class));
    }

    //onClick listeners
    @Override
    public void onClick(View view) {
        if(view == next) {
            saveData();
            //call DailyGoal page
            callDailyGoal();
        }
        if(view == back){
            saveData();
            //Call next activity, heartRate.class
            callHeartRate();
        }
        if(view == backMenu){
            //If user doesn't input any data, call saveNullData() to save the data as "0"
            saveData();
            //Call next activity, MiLog.class
            callVitalMenu();
        }
    }
}
