package com.example.android.sfwhf1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText uName;
    private EditText rName;
    private EditText uComments;
    private Button saveBtn;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        uName = (EditText) findViewById(R.id.uname);
        rName = (EditText) findViewById(R.id.restorant);
        uComments = (EditText) findViewById(R.id.comments);
        saveBtn = (Button) findViewById(R.id.btnSave);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Share");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }


    private void save() {
        final String name = uName.getText().toString().trim();
        final String restorant = rName.getText().toString().trim();
        final String comments = uComments.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(restorant)) {
            String id = mDatabase.push().getKey();
            mDatabase.child(id).child("Name").setValue(name);
            mDatabase.child(id).child("Restorant").setValue(restorant);
            mDatabase.child(id).child("Comments").setValue(comments);
            Toast.makeText(MainActivity.this, "success!!", Toast.LENGTH_LONG).show();


        }
    }
}
