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

public class BikeActivity extends AppCompatActivity {
    private EditText bduration;
    private TextView bcalBurnt;
    private ImageButton bsubmit;
    private ImageButton back;
    private int bmr;
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
        setContentView(R.layout.activity_bike);
        setTitle("BiCycling");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bduration = (EditText)findViewById(R.id.b_duration);
        bcalBurnt = (TextView)findViewById(R.id.b_calView);
        bsubmit = (ImageButton)findViewById(R.id.b_submit);
        back = (ImageButton) findViewById(R.id.back);
        bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsave();
                //startActivity(new Intent(BikeActivity.this, WorkOutTypeActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BikeActivity.this, AerobicActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
    }
    //MET for bicycling is 8.5 per IEEE
    public double rcalburnt(double duration){

        //Calirie Burnt =(MBR/24) * MET * T

        return (bmr/24)*(8.5)*(duration/60);
    }


    private void bsave() {
        final String dur = bduration.getText().toString().trim();

        if(!TextUtils.isEmpty(dur)){
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString().equals("0")){
                        bmr = Integer.parseInt(dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString());
                        double wBurnt = rcalburnt(Double.parseDouble(dur));
                        bcalBurnt.setText("Calorie Burnt: " +df2.format(wBurnt));

                        mDatabase.child("Workout").child(currentDate).child("bike").child("Time").setValue(dur);
                        mDatabase.child("Workout").child(currentDate).child("Bicycling Calorie Burnt").setValue(df2.format(wBurnt));
                        //startActivity(new Intent(BikeActivity.this, WorkOutTypeActivity.class));

                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(BikeActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please Fill Out Your Daily Vital First!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(BikeActivity.this, Main.class));
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
