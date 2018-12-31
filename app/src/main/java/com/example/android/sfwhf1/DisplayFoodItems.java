package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//Written by Archana
//Modified by Asami on 11/10/2017
public class DisplayFoodItems extends DisplayCalendar implements View.OnClickListener {
    Button dateButton;
    private ListView showFoodItem;
    private TextView showFoodCalories;
    private ImageButton intakesMenu, backMilog;
    final ArrayList<String> arrayList = new ArrayList<>();
    TextView showDate;
    //Getting current date
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private String currentDate = dateFormat.format(date.getTime());

    Intent intent = this.getIntent();
    String strdata;
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;


    String selectedDay;
    String selectedMonth;
    String selectedYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food_items);


        showFoodCalories = (TextView) findViewById(R.id.showfoodCalories);
        showFoodItem = (ListView) findViewById(R.id.foodItem);
        intakesMenu = (ImageButton) findViewById(R.id.intakesMenu);
        backMilog = (ImageButton) findViewById(R.id.backMilog);
        intakesMenu.setOnClickListener(this);
        backMilog.setOnClickListener(this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        showFoodItem.setAdapter(arrayAdapter);

        //Using only for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(date.getTime());
        //setTitle("FOOD LOG");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        SimpleDateFormat differentDateFormat = new SimpleDateFormat("MMddyyyy");
        final String currentDate = differentDateFormat.format(date.getTime());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Food Log");
        //Display date
        //Calling date from previous activity
        final String selectedDate = getIntent().getStringExtra("selectedDate");

        if (selectedDate == null) {
            //if date is not chosen, showing currentdate
            setTitle("Food Log: " + formattedDate);

        } else {
            //if date is chosen, retrieve the date from intent
            selectedDay = getIntent().getStringExtra("Day");
            selectedMonth = getIntent().getStringExtra("Month");
            selectedYear = getIntent().getStringExtra("Year");
            //Setting the title with selected date
            setTitle("Food Log: " + selectedMonth + "/" + selectedDay + "/" + selectedYear);


        }
        String pickedDate = selectedMonth + "/" + selectedDay + "/" + selectedYear;

        Query query = mDatabase.orderByChild(currentDate).equalTo(pickedDate);

        if (selectedDate != null) {
            mDatabase.child(selectedDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        mDatabase.child(selectedDate).child("Item Inputs").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String itemInputs = dataSnapshot.getValue(String.class);
                                arrayAdapter.add(itemInputs);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        String totalCalories = dataSnapshot.child("Total Calories").getValue().toString();
                        showFoodCalories.setText("Total Calories: " + totalCalories + " Calories" );

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            mDatabase.child(currentDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String totalCalories = dataSnapshot.child("Total Calories").getValue().toString();
                        mDatabase.child(currentDate).child("Item Inputs").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String itemInputs = dataSnapshot.getValue().toString();
                                arrayAdapter.add(itemInputs);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        showFoodCalories.setText("Total Calories: " + totalCalories + " Calories");
                    } else {
                        //showFoodItem.setText("Inputs: ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(DisplayFoodItems.this, DisplayCalendar.class));


        return super.onOptionsItemSelected(item);
    }

    private String checkNull(String value){
        String nullValue = "0";
        if(value.isEmpty()){
            return nullValue;
        }
        else{
            return value;
        }
    }


    @Override
    public void onClick(View v) {
        if(v == intakesMenu){
            startActivity(new Intent(this, Inputs.class));
        }
        if(v == backMilog){
            startActivity(new Intent(this, milogMenu.class));

        }

    }
}
