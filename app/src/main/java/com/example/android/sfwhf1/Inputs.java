package com.example.android.sfwhf1;
//Written by Archana

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.android.sfwhf1.R.id.typeCalorieInputManual;

/**
 * Author- Archana Yadawa
 */
public class Inputs extends AppCompatActivity {


    private Button dateDisplay;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uID;
    private Calendar date1 = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("MM/dd/yyyy");
    private String currentDate = dateFormat.format(date1.getTime());
    private String currentDateDisplay = dateFormatDisplay.format(date1.getTime());

    String name = "";
    Integer value;
    String name1 = "";
    Integer value2;
    Object value1;
    int sum= 0;
    int sumA = 0;
    int sum2= 0;
    int newSum = 0;
    ListView lv;
    ListView lv2;
    TextView t2;
    EditText search;
    EditText typeCalInput;
    SearchView sv;
    ImageButton totalButton;
    Button deletebutton;
    Button addbutton;
    Button minusbutton;
    ImageButton addInputManually;
    EditText inputFoodManual;

    final ArrayList<String> listItems = new ArrayList<String>();
    final ArrayList<Integer> listItems2 = new ArrayList<Integer>();
    final ArrayList<String> searchList = new ArrayList<String>();
    final ArrayList<Items> delete = new ArrayList<Items>();


    String[] items={"Apple", "Banana", "Bagel", "Beef", "Blackberries", "Carrot", "Cereal", "Cherry",
            "Chicken", "Cucumber", "Egg (1)", "Fish", "Grapes", "Ice Cream", "Macaroni", "Melon", "Milk",
            "Noodles", "Orange", "Pasta", "Pinapple", "Rice", "Spaghetti", "Toffee", "Tomato", "Turkey",
            "Yogurt"};

    //intialize hashmap to hold all item names and calorie amount
    final ArrayList<LinkedHashMap<String, Integer>> linkedhash=new ArrayList<LinkedHashMap<String, Integer>>();
    final LinkedHashMap<String, Integer> hashmap=new LinkedHashMap<String, Integer>();

    final HashMap<String, String> hashmap2 = new HashMap<String, String>();
    final HashMap<String, Integer> hashmap3 = new HashMap<String, Integer>();

    // ArrayList<HashMap<String,String>> items =
    //  new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //prevent softpush effects from keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Setting up Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Food Log").child(currentDate);
        // final ArrayList arrayList = new ArrayList();
        //  String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date()); 
        setTitle("FOOD LOG: " + currentDateDisplay);

        //inputs for hashmap
        hashmap.put("Apple", 44);
        hashmap.put("Banana", 107);
        hashmap.put("Bagel",140 );
        hashmap.put("Beef",300 );
        hashmap.put("Blackberries", 25);
        hashmap.put("Carrot", 16);
        hashmap.put("Cereal",130 );
        hashmap.put("Cherry", 35);
        hashmap.put("Chicken", 220);
        hashmap.put("Cucumber", 10);
        hashmap.put("Egg (1)", 90);
        hashmap.put("Fish", 200);
        hashmap.put("Grapes", 55);
        hashmap.put("Ice Cream",200 );
        hashmap.put("Macaroni", 238);
        hashmap.put("Melon", 14);
        hashmap.put("Milk", 175);
        hashmap.put("Noodles", 175);
        hashmap.put("Orange", 40);
        hashmap.put("Pasta", 330);
        hashmap.put("Pinapple", 40);
        hashmap.put("Rice", 400);
        hashmap.put("Spaghetti", 303);
        hashmap.put("Toffee", 100);
        hashmap.put("Tomato", 30);
        hashmap.put("Turkey", 200);
        hashmap.put("Yogurt", 90);



        linkedhash.add(hashmap);



        //Create and initalize adapters
        final ArrayAdapter adapter1;
        final ArrayAdapter adapter;
        final MyCustomAdapter adapter3;
        final ArrayAdapter adapter100;

        //final ArrayAdapter<LinkedHashMap<String,Integer>> ad = new ArrayAdapter<LinkedHashMap<String,Integer>>(this, android.R.layout.simple_list_item_1, listItems);

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        adapter100 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchList);

        final MyAdapter adapter6 = new MyAdapter(hashmap);

        final MyCustomAdapter adapter10 = new MyCustomAdapter(listItems);

        final HashMapAdapter hashMapAdapter = new HashMapAdapter(hashmap3);

        //initalization of Views
        lv = (ListView) findViewById(R.id.listView1);
        t2 = (TextView) findViewById(R.id.textView5);

        lv2 = (ListView) findViewById(R.id.listView2);
        totalButton = (ImageButton) findViewById(R.id.submit);
        addInputManually = (ImageButton) findViewById(R.id.addmanualinput);
        deletebutton = (Button) findViewById(R.id.delete_btn);
        addbutton = (Button) findViewById(R.id.plus_btn);
        minusbutton = (Button) findViewById(R.id.minus_btn);
        typeCalInput = (EditText) findViewById(typeCalorieInputManual);
        inputFoodManual = (EditText) findViewById(R.id.foodItemManualInput);
        // search = (EditText) findViewById(R.id.search);


        //lv.setAdapter(adapter1);
        lv.setAdapter(adapter1);

/**
 //Search Listener action
 sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
@Override
public boolean onQueryTextSubmit(String text) {
// TODO Auto-generated method stub
return false;
}

@Override
public boolean onQueryTextChange(String text) {

adapter100.getFilter().filter(text);
return false;
}
});
 */

        //Listview 1 action - to display all item choices
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.activity_inputs, null);

                name = (new ArrayList<String>(hashmap.keySet())).get(position);
                value = (new ArrayList<Integer>(hashmap.values())).get(position);
                listItems2.add(value);

                listItems.add(name + " = " + value + " Calories");

                lv2.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //update sum
                sum += Integer.parseInt(value.toString());
                t2.setText(" Total =" + sum + " Calories");
                t2.setTextSize(20);

            }
        });

        lv2.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.activity_inputs, null);

                name1 = (listItems.get(position));
                value2 = (listItems2.get(position));
                listItems.add(name1);
                listItems2.add(value2);
                adapter.notifyDataSetChanged();
                //update sum
                sum += Integer.parseInt(value2.toString());
                t2.setText(" Total =" + sum + " Calories");
                t2.setTextSize(20);

            }
        });

        //Listview 2 action - to display all picked items from Listview 1
        lv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        Inputs.this);
                alert.setTitle("Alert Message");
                alert.setMessage("Delete this Item?");
                //if yes....
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        //set adapter to remove item
                        adapter.remove(adapter.getItem(i));
                        //update sum
                        final Integer value10 = (listItems2.get(i));
                        // final Integer value10 = (new ArrayList<Integer>(hashmap.values())).get(i);


                        sum -= Integer.parseInt(value10.toString());


                        t2.setText(" Total =" + sum + " Calories");
                        listItems2.remove(i);

                        //notify adapter to change view based on item deleted
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                //if no....
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

// Code for calendar


        totalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save(sum);
                //startActivity(new Intent(Inputs.this, DisplayFoodItems.class));


            }
        });


        addInputManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String food = inputFoodManual.getText().toString().replace("[", "").replace("]", "");

                String cal10 = typeCalInput.getText().toString().replace("[", "").replace("]", "");

                hashmap2.put(food, cal10);
                ArrayList<String>   food3 = (new ArrayList<String>(hashmap2.keySet()));
                ArrayList<String>  value3 = (new ArrayList<String>(hashmap2.values()));
                Integer bb = Integer.parseInt(cal10);

                listItems.add(food3 + " : =" + value3 + " Calories ");
                listItems2.add(bb);

                adapter.notifyDataSetChanged();
                sumA = Integer.parseInt(cal10.toString());
                sum += sumA;
                t2.setText(" Total =" + sum + " Calories");

                //After input an item, clear the text fields
                //Modify by Asami on 11/24
                inputFoodManual.setText("");
                typeCalInput.setText("");


            }
        });
    }

    private void save(int i)
    {
        mDatabase.child("Total Calories").setValue(sum);
        mDatabase.child("Item Inputs").setValue(listItems);
        startActivity(new Intent(this, DisplayFoodItems.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(Inputs.this, DisplayCalendar.class));


        return super.onOptionsItemSelected(item);
    }
}