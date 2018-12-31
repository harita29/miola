package com.example.android.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.android.sfwhf1.R;
import com.example.android.sfwhf1.UserProfileActivity;
import com.example.android.utils.GeneralFunctions;
import com.example.android.utils.Utils;
import com.example.android.view.CreateRoundedView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_profile_user_personal_info extends Fragment {


    View view;

    UserProfileActivity userProfileAct;
    GeneralFunctions generalFunc;
    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    Button submitBtn;
    AppCompatSpinner ageSpinner;
    AppCompatSpinner genderSpinner;

    ArrayList<String> ageData = new ArrayList<>();
    ArrayList<String> genderData = new ArrayList<>();
    /*MaterialEditText ageEditBox;
    //    EditText ageEditBox;
    MaterialEditText genderEditBox;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_profile_user_personal_info, container, false);
        userProfileAct = (UserProfileActivity) getActivity();
        generalFunc = new GeneralFunctions(userProfileAct);

        firebaseAuth = FirebaseAuth.getInstance();
        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        ageSpinner = (AppCompatSpinner) view.findViewById(R.id.ageSpinner);
        genderSpinner = (AppCompatSpinner) view.findViewById(R.id.genderSpinner);
//        genderEditBox = (MaterialEditText) view.findViewById(R.id.genderEditBox);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);

//        ageEditBox.setBothText("Birth year", "Enter your birth year");
//        genderEditBox.setBothText("Gender", "Enter your gender");

        submitBtn.setOnClickListener(new setOnClickList());

        buildGenderSpinner();
        buildAgeData();
        checkData();

        return view;
    }

    public void checkData(){
        homeDatabase.child("Age").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//                    ageEditBox.setText(dataSnapshot.getValue().toString());
                    for (int i = 0; i < ageData.size(); i++) {
                        if (ageData.get(i).equals(dataSnapshot.getValue().toString())) {
                            ageSpinner.setSelection(i);
                        }
                    }
                }
//                System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        homeDatabase.child("Gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//                    genderEditBox.setText(dataSnapshot.getValue().toString());
                    for (int i = 0; i < genderData.size(); i++) {
                        if (genderData.get(i).equals(dataSnapshot.getValue().toString())) {
                            genderSpinner.setSelection(i);
                        }
                    }
                }
//                System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void buildGenderSpinner() {
        ArrayList<String> genderListData = new ArrayList<String>();
        genderListData.add("Select your gender");
        genderListData.add("Male");
        genderListData.add("Female");

        genderData.clear();
        genderData.addAll(genderListData);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genderData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        genderSpinner.setAdapter(dataAdapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), genderSpinner);
    }

    public void buildAgeData() {
        ArrayList<String> yearData = new ArrayList<String>();
        yearData.add("Select your Birth year");

        for (int i = 1950; i < Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearData.add(""+i);
        }

        ageData.clear();
        ageData.addAll(yearData);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ageSpinner.setAdapter(dataAdapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), ageSpinner);
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.submitBtn:
                    checkValues();
                    break;
            }
        }
    }

    public void checkValues() {

        String age = ((String) ageSpinner.getSelectedItem());
        String gender = ((String) genderSpinner.getSelectedItem());

//        String age = ageEditBox.getText().toString();
//        String gender = genderEditBox.getText().toString();

        if (age.trim().equals("Select your Birth year")) {
//            ageEditBox.setError("Please enter your age");
            generalFunc.showGeneralMessage("","Please select your birth year");
            return;
        }
        if (gender.trim().equals("Select your gender")) {
//            genderEditBox.setError("Please enter your gender");
            generalFunc.showGeneralMessage("","Please select your gender");
            return;
        }

        if (age.trim().equals("") && gender.trim().equals("")) {
            return;
        }

        Utils.showLoader(userProfileAct, "", "Setting your detail");

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("Age", age);
        dataMap.put("Gender", gender);

        homeDatabase.updateChildren(dataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Utils.dismissLoader();
                if (databaseError != null) {
                    generalFunc.showGeneralMessage("", "Data could not be saved. " + databaseError.getMessage());
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                    if(userProfileAct.fragContainerView.getVisibility() == View.VISIBLE){
                        userProfileAct.onBackPressed();
                    }else{
                        userProfileAct.arrowImgView.performClick();
                    }
                }
            }
        });


    }
}
