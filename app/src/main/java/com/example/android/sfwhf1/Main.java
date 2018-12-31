package com.example.android.sfwhf1;
//Written by Asami

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.utils.Utils;
import com.example.android.view.CreateRoundedView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Main extends AppCompatActivity implements View.OnClickListener{
    private ImageButton buttonDailyVital;
    private ImageButton buttonIntakes;
    private ImageButton buttonWorkout;
    private ImageButton buttonMilog;
    private ImageButton buttonGame;
    private ImageButton buttonUserProfile;
    private TextView textViewSignout;
    private TextView deleteAccount;
    //Getting current date
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase, mDatabaseOrigin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MAIN MENU");

        buttonDailyVital = (ImageButton) findViewById(R.id.buttonDailyVital);
        buttonIntakes = (ImageButton) findViewById(R.id.buttonIntakes);
        buttonWorkout = (ImageButton) findViewById(R.id.buttonWorkout);
        buttonMilog = (ImageButton) findViewById(R.id.buttonMilog);
        buttonGame = (ImageButton) findViewById(R.id.buttonGame);
        buttonUserProfile = (ImageButton) findViewById(R.id.buttonUserProfile);
        textViewSignout = (TextView) findViewById(R.id.textViewSignout);
        deleteAccount = (TextView) findViewById(R.id.deleteAccount);
        buttonDailyVital.setOnClickListener(this);
        buttonIntakes.setOnClickListener(this);
        buttonWorkout.setOnClickListener(this);
        buttonMilog.setOnClickListener(this);
        buttonGame.setOnClickListener(this);
        buttonUserProfile.setOnClickListener(this);
        textViewSignout.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Daily Vital").child(currentDate);
        //Using database for update reward points if users completed their data from the day before today
        mDatabaseOrigin = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
        Calendar oneDayBefore = Calendar.getInstance();
        oneDayBefore.add(Calendar.DATE, -1);
        final String yesterday = dateFormat.format(oneDayBefore.getTime());

        //Checking whether "Rewards" exist or not
        mDatabaseOrigin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Reward Points").exists()){
                    //If a user is new, create "Rewards" value and give 20 pts for default
                    mDatabaseOrigin.child("Reward Points").setValue("20");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        mDatabaseOrigin.child("Daily Vital").child(yesterday).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If there are RewardDone and Reward Points on Firebase, add reward points
                if(dataSnapshot.child("RewardDone").exists() && dataSnapshot.child("Reward Points").exists()){
                    //Retrieve "RewardDone" data from Firebase
                    String addPointCheck = dataSnapshot.child("RewardDone").getValue().toString();
                    //If Rewardpoints do not add to the Rewards, add the points
                    if(addPointCheck.equals("false")){
                        final int rewardP = Integer.parseInt(dataSnapshot.child("Rewards").getValue().toString());
                        mDatabaseOrigin.child("Reward Points").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    int totalRewardPoints = Integer.parseInt(dataSnapshot.getValue().toString());
                                    totalRewardPoints += rewardP;
                                    mDatabaseOrigin.child("Reward Points").setValue(totalRewardPoints);
                                    mDatabaseOrigin.child("Daily Vital").child(yesterday).child("RewardDone").setValue("true");
                                }else{
                                    //If the points are already added, do nothing
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{

                    }
                }else{
                    //If there are no data "reward points and RewardDone, create the data and set default values
                    mDatabaseOrigin.child("Daily Vital").child(yesterday).child("Rewards").setValue(0);
                    mDatabaseOrigin.child("Daily Vital").child(yesterday).child("RewardDone").setValue("true");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //Create data if there are nothing
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    mDatabase.child("Body Temp").setValue(0);
                    mDatabase.child("High BP").setValue(0);
                    mDatabase.child("Low BP").setValue(0);
                    mDatabase.child("Heart Rate").setValue(0);
                    mDatabase.child("Weight").setValue(0);
                    mDatabase.child("Goal").setValue(0);
                    mDatabase.child("BMR").setValue(0);
                    mDatabase.child("BMI").setValue(0);
                    mDatabase.child("Rewards").setValue(0);
                    mDatabase.child("RewardDone").setValue("false");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkProfileData();
    }

    public void checkProfileData(){

        Utils.showLoader(Main.this,"Checking Profile Data", "Please wait we are checking your profile.");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        Log.e("!_@@<DATA>", "" + dataUser.toString());

                        if (dataUser.get("Weight") == null || dataUser.get("Height") == null || dataUser.get("Age") == null || dataUser.get("Gender") == null || dataUser.get("Name") == null || dataUser.get("Measurement") == null || dataUser.get("ActivityLevel") == null || dataUser.get("Goal") == null || dataUser.get("GoalWeight") == null) {

                          Intent int_profile = new Intent(Main.this,UserProfileActivity.class);
                          int_profile.putExtra("IS_DATA_AVAIL","No");
                          startActivity(int_profile);

                          finish();
                        }
                    } else {
                        // Data not exist
                        Intent int_profile = new Intent(Main.this,UserProfileActivity.class);
                        int_profile.putExtra("IS_DATA_AVAIL","No");
                        startActivity(int_profile);
                        finish();
                    }

                } else {
                    // Data not exist
                    Intent int_profile = new Intent(Main.this,UserProfileActivity.class);
                    int_profile.putExtra("IS_DATA_AVAIL","No");
                    startActivity(int_profile);
                    finish();
                }

                Utils.dismissLoader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.dismissLoader();
            }
        });
    }

    private void deactivate(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Main.this, "Account is deactivated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Main.this, "Account could not be deactivated", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    @Override
    public void onClick(View v) {

        if(v == buttonDailyVital){
            Intent dailyVital = new Intent(this, DailyVitalMenu.class);
            startActivity(dailyVital);
        }
        if(v == buttonIntakes){
            Intent food = new Intent(this, Inputs.class);
            startActivity(food);
        }
        if(v == buttonWorkout){
            Intent work = new Intent(this, WorkOutTypeActivity.class);
            startActivity(work);
        }
        if(v == buttonMilog){
            startActivity(new Intent(this, milogMenu.class));
            //go to location activity
        }
        if(v == buttonGame){
            startActivity(new Intent(this, MainMenuActivity.class));
            //go to game activity
        }
        if(v == buttonUserProfile){
            Intent goToUserProfile = new Intent(this, UserProfileActivity.class);
            //goToUserProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToUserProfile);
        }
        if(v == textViewSignout){
            mAuth.signOut();
            Toast.makeText(this, "You successfully logout", Toast.LENGTH_SHORT).show();
            finish();
            Intent goToWelcom = new Intent(Main.this, Welcom.class);
            goToWelcom.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToWelcom);
        }
        if(v == deleteAccount){
            deactivate();
            startActivity(new Intent(this, Login.class));
        }
    }
}
