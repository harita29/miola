package com.example.android.sfwhf1;
//Written by Asami

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class Login extends AppCompatActivity implements View.OnClickListener{
    private ImageButton buttonLogin;
    private ImageButton forget;
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private TextView textViewSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("MioLa");

        buttonLogin = (ImageButton) findViewById(R.id.buttonLogin);
        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        forget = (ImageButton) findViewById(R.id.forget);
        forget.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //go to main menu
            finish();
            startActivity(new Intent(getApplicationContext(), Main.class));
        }
        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);



    }
    private void userLogin(){
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            editTextLoginEmail.setError("Please enter your email");
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            editTextLoginPassword.setError("Please enter your password");
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Progessing...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //Go to main menu
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Main.class));
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Log in error", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    //Reset password by email
    private void sendPassword(){
        String email = editTextLoginEmail.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(Login.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "We have sent you instructions to reset your password. Please check your email.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(Login.this, "Failed to reset your password.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            userLogin();
        }
        if(v == textViewSignUp){
            finish();
            startActivity(new Intent(this, Signup.class));
        }
        if(v == forget){
            sendPassword();
        }

    }
}
