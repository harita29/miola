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

public class WeightLiftingActivity extends AppCompatActivity {
    private EditText weduration;
    private TextView wecalBurnt;
    private ImageButton wesubmit, back;
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
        setContentView(R.layout.activity_weight_lifting);
        setTitle("Weight Lifting");
        //String ct = DateFormat.getDateInstance().format(new Date());
        //date.setText(ct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        weduration = (EditText)findViewById(R.id.we_duration);
        wecalBurnt = (TextView)findViewById(R.id.we_calView);
        wesubmit = (ImageButton)findViewById(R.id.we_submit);
        back = (ImageButton)findViewById(R.id.back);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        wesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wesave();
                //startActivity(new Intent(WeightLiftingActivity.this, WorkOutTypeActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeightLiftingActivity.this, WorkOutTypeActivity.class));
            }
        });
    }
    //MET for weight lifting is 4 per IEEE
    public double calorieBurnt(double duration){
        //Calorie Burnt =(MBR/24) * MET * T
        return (bmr/24)*(4)*(duration/60);
    }

    private void wesave() {
        final String wedur = weduration.getText().toString().trim();
        if(!TextUtils.isEmpty(wedur)){
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString().equals("0")){
                        bmr = Integer.parseInt(dataSnapshot.child("Daily Vital").child(currentDate).child("BMR").getValue().toString());
                        double wBurnt = calorieBurnt(Double.parseDouble(wedur));
                        wecalBurnt.setText("Calorie Burnt: " +df2.format(wBurnt));

                        mDatabase.child("Workout").child(currentDate).child("weightLifting").child("Time").setValue(wedur);
                        mDatabase.child("Workout").child(currentDate).child("WeightLifting Calorie Burnt").setValue(df2.format(wBurnt));
                        //startActivity(new Intent(WeightLiftingActivity.this, WorkOutTypeActivity.class));
                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(WeightLiftingActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please Fill Out Your Daily Vital First!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(WeightLiftingActivity.this, Main.class));
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
