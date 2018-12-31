package com.example.android.sfwhf1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
    private TextView uName;
    private TextView rName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        uName = (TextView)findViewById(R.id.nameView);
        rName = (TextView)findViewById(R.id.restorantView);

    }
}
