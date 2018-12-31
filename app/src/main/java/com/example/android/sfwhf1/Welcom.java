package com.example.android.sfwhf1;
//Written by Asami

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Welcom extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonSignup;
    private ImageButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        setTitle("Welcome to MioLa");

        buttonSignup = (ImageButton) findViewById(R.id.buttonSignup);
        buttonLogin = (ImageButton) findViewById(R.id.buttonLogin);

        buttonSignup.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignup) {
            finish();
            startActivity(new Intent(this, Signup.class));
        }
        if(v == buttonLogin){
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
