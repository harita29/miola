package com.example.android.sfwhf1;
//Written by Asami

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private ImageButton buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editSecondTextPassword;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Welcome to MiOLa!!");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = (ImageButton) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editSecondTextPassword = (EditText) findViewById(R.id.editSecondTextPassword);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String secondPassword = editSecondTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(secondPassword)){
            Toast.makeText(this, "Passwords are not the same, please enter them again.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals(secondPassword)) {
            progressDialog.setMessage("Registering ...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //user is successfully registered and logged in
                        //we will start the profile activity here right now lets display a toast only
                        progressDialog.dismiss();
                        Toast.makeText(Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

//                        startActivity(new Intent(getApplicationContext(), Main.class));
//                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                        Intent int_profile = new Intent(getApplicationContext(),UserProfileActivity.class);
                        int_profile.putExtra("IS_DATA_AVAIL","No");
                        startActivity(int_profile);
                        finish();
                        return;

                    }
                    if (firebaseAuth.getCurrentUser() != null) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        builder.setMessage("You already have account")
                                .setTitle("Exsiting")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    /*
                    Toast.makeText(Signup.this, "You already have your account", Toast.LENGTH_SHORT).show();
                    finish();*/
//                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        startActivity(new Intent(getApplicationContext(), Main.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Signup.this, "Could not register. Please try again", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewLogin) {
            //will open sign in activity
            finish();
            startActivity(new Intent(this, Login.class));
        }

    }
}