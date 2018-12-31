package com.example.android.sfwhf1;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WalkActivity extends AppCompatActivity{
    private EditText wDuration;
    private TextView wCalBurnt;
    private ImageButton wSubmit;
    private ImageButton back;

    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());

    private static DecimalFormat df2 = new DecimalFormat(".##");
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private int bmr;
    //static double metD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        setTitle("Walking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wDuration = (EditText) findViewById(R.id.w_duration);
        wCalBurnt = (TextView) findViewById(R.id.w_calView);
        wSubmit = (ImageButton) findViewById(R.id.w_submit);
        back = (ImageButton) findViewById(R.id.back);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);


        wSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wSave();
                //startActivity(new Intent(WalkActivity.this, WorkOutTypeActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalkActivity.this, WorkOutTypeActivity.class));
            }
        });
    }

    public double calorieBurnt(double duration){
        return (bmr/24)*(3.8)*(duration/60);
    }

    //MET for walking is 3.8 per IEEE

    private void wSave() {
        final String wDur = wDuration.getText().toString().trim();
        if(!TextUtils.isEmpty(wDur)){
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString().equals("0")) {
                        bmr = Integer.parseInt(dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString());
                        double wBurnt = calorieBurnt(Double.parseDouble(wDur));
                        wCalBurnt.setText("Calorie Burnt: " + df2.format(wBurnt));

                        mDatabase.child("Workout").child(currentDate).child("walk").child("Time").setValue(wDur);
                        mDatabase.child("Workout").child(currentDate).child("Walking Calorie Burnt").setValue(df2.format(wBurnt));
                        //startActivity(new Intent(WalkActivity.this, WorkOutTypeActivity.class));

                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(WalkActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please Fill Out Your Daily Vital First!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(WalkActivity.this, Main.class));
                                    }
                                });
                        alertDialog.show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }
}

