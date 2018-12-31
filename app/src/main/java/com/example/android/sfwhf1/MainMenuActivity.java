package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
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


public class MainMenuActivity extends FragmentActivity {
    //Getting current date
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference  mDatabaseOrigin;


    TextView points;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        points = (TextView) findViewById(R.id.activepoints);
        menu = (Button) findViewById(R.id.buttonMenu);
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();


        mDatabaseOrigin = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
        mDatabaseOrigin.child("Reward Points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                int rewards = Integer.parseInt(dataSnapshot.getValue().toString());

                points.setText("Active Points: " + rewards );
                mDatabaseOrigin.child("Reward Points").setValue(rewards);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, Main.class));
            }
        });

        showFragment(MainMenuFragment.TAG);


    }

    private void showFragment(String fragmentTag) {
        if (MainMenuFragment.TAG.equals(fragmentTag)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();

            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fragmentTransaction.replace(R.id.layoutContainer, mainMenuFragment,
                    MainMenuFragment.TAG);

            fragmentTransaction.commit();
        }
    }
}
