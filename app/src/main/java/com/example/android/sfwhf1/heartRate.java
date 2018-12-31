package com.example.android.sfwhf1;
//written by Asami on Oct 5

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

public class heartRate extends AppCompatActivity implements View.OnClickListener {

    private EditText heartRate;
    private TextView next;
    private TextView back;
    private ImageButton backMenu;
    //Setting up current date
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
        setContentView(R.layout.activity_heart_rate);
        setTitle("HEART RATE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        heartRate = (EditText) findViewById(R.id.heartRate);
        next = (TextView) findViewById(R.id.next);
        back = (TextView) findViewById(R.id.back);
        backMenu = (ImageButton) findViewById(R.id.backMain);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //mProgress = new ProgressDialog(this);
        uID = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital").child(currentDate);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        backMenu.setOnClickListener(this);

        //Showing data if there is on Firebase
        mDatabase.child("Heart Rate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String hr = dataSnapshot.getValue().toString();
                if(hr.equals("0")){

                }else{
                    heartRate.setText(hr);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





    //Saving data from user input
    private void saveData(){
        String heartR = heartRate.getText().toString().trim();
        if(!heartR.isEmpty()) {
            mDatabase.child("Heart Rate").setValue(heartR);
        }else{
            mDatabase.child("Heart Rate").setValue(0);
        }
    }

    private void callWeight(){
        startActivity(new Intent(this, weight.class));
    }
    private void callBloodPressure(){
        startActivity(new Intent(this, BloodPressure.class));
    }
    private void callVitalMenu(){ startActivity(new Intent(this, DailyVitalMenu.class));}

    @Override
    public void onClick(View view) {

        if(view == next){
            saveData();
            //call next input page
            callWeight();
        }
        if(view == back){
            saveData();
            //call BloodPressure
            callBloodPressure();
        }
        if(view == backMenu){
            saveData();
            //Call next activity, weight.class
            callVitalMenu();
        }
    }
}

