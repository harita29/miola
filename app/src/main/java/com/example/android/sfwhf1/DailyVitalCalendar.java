package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

//Written by Asami on 11/10

public class DailyVitalCalendar extends AppCompatActivity {
    private CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_vital_calendar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Daily Vitals");
            calendarView = (CalendarView) findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView calendarView, final int year, final int month, final int day) {
                    //Getting day from listener
                    String dayString = Integer.toString(day);
                    //if day is less than 10, add "0" in front of the number
                    if(day < 10){
                        dayString = "0" + dayString;
                    }
                    //Getting month from Listener
                    String monthString = Integer.toString(month+1);
                    //Getting year from Listener
                    String yearString = Integer.toString(year);
                    //Making date
                    String selectDay = monthString+dayString+yearString;
                    //Creating intent which passes to DisplayFoodItems
                    Intent next = new Intent(DailyVitalCalendar.this, MiLog.class);
                    //Passing the selected day to next intent
                    next.putExtra("selectedDate",selectDay);
                    //Passing month to next intent
                    next.putExtra("Month",monthString);
                    //Passing day to next intent
                    next.putExtra("Day", dayString);
                    //Passing year to next intent
                    next.putExtra("Year", yearString);
                    //Calling intent
                    startActivity(next);

                }
            });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(DailyVitalCalendar.this, MiLog.class));
        return super.onOptionsItemSelected(item);
    }
}
