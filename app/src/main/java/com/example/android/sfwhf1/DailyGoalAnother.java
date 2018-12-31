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

public class DailyGoalAnother extends AppCompatActivity implements View.OnClickListener {

    private EditText enter;
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
        setContentView(R.layout.activity_daily_goal_another);
        setTitle("Daily Goal Another");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enter = (EditText) findViewById(R.id.enter);
        submit = (ImageButton) findViewById(R.id.next);
        back = (ImageButton) findViewById(R.id.backMain);

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    private void submitData(){
        String createGoal = enter.getText().toString();
        if(createGoal.isEmpty()){
            Toast.makeText(this, "Please enter your own goal.", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            uID = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital").child(currentDate).child("Goal");
            mDatabase.child("Num").setValue("3");
            mDatabase.child("Detail").setValue(createGoal);
        }

    }
    @Override
    public void onClick(View view) {
        if(view == submit){
            submitData();
            startActivity(new Intent(this, DailyVitalMenu.class));
        }
        if(view == back){
            startActivity(new Intent(this, DailyGoal.class));
        }
    }
}
