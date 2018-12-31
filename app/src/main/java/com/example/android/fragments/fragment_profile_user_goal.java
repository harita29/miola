package com.example.android.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.android.sfwhf1.R;
import com.example.android.sfwhf1.UserProfileActivity;
import com.example.android.utils.GeneralFunctions;
import com.example.android.utils.Utils;
import com.example.android.view.CreateRoundedView;
import com.example.android.view.editBox.MaterialEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_profile_user_goal extends Fragment {


    View view;

    UserProfileActivity userProfileAct;
    GeneralFunctions generalFunc;
    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    Button submitBtn;
    /*MaterialEditText activityLevelEditBox;
    MaterialEditText goalEditBox;*/
    AppCompatSpinner goalSpinner;
    AppCompatSpinner activityLevelSpinner;

    View weightSpinnerArea;

    MaterialEditText goalWeightEditBox;
    View goalWeightArea;
    AppCompatSpinner weightSelectionSpinner;

    ArrayList<String> activityLevel = new ArrayList<>();
    ArrayList<String> goalDataList = new ArrayList<>();
    ArrayList<String> weightDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_profile_user_goal, container, false);

        userProfileAct = (UserProfileActivity) getActivity();
        generalFunc = new GeneralFunctions(userProfileAct);

        firebaseAuth = FirebaseAuth.getInstance();
        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        /*activityLevelEditBox = (MaterialEditText) view.findViewById(R.id.activityLevelEditBox);
        goalEditBox = (MaterialEditText) view.findViewById(R.id.goalEditBox);*/
        weightSpinnerArea = view.findViewById(R.id.weightSpinnerArea);
        goalWeightEditBox = (MaterialEditText) view.findViewById(R.id.goalWeightEditBox);
        goalSpinner = (AppCompatSpinner) view.findViewById(R.id.goalSpinner);
        activityLevelSpinner = (AppCompatSpinner) view.findViewById(R.id.activityLevelSpinner);
        goalWeightArea = view.findViewById(R.id.goalWeightArea);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        weightSelectionSpinner = (AppCompatSpinner) view.findViewById(R.id.weightSelectionSpinner);

//        if (userProfileAct.fragContainerView.getVisibility() == View.GONE) {
        weightSpinnerArea.setVisibility(View.GONE);
//        } else {
//            weightSpinnerArea.setVisibility(View.VISIBLE);
//        }

        submitBtn.setOnClickListener(new setOnClickList());

        goalWeightEditBox.setBothText("Desired weight", "Please enter your desired weight");
        goalWeightEditBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        buildGoalData();
        buildActivityLevelData();
        buildWeightSpinner();

        checkData();

        return view;
    }

    public void checkData(){
        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        Log.e("!_@@<DATA>", "" + dataUser.toString());

                        if (dataUser.get("GoalWeight") != null) {
                            goalWeightEditBox.setText(dataUser.get("GoalWeight"));
                        }

                        if (dataUser.get("GoalWeightUnit") != null) {
                            for (int i = 0; i < weightDataList.size(); i++) {
                                if (weightDataList.get(i).equals(dataUser.get("GoalWeightUnit"))) {
                                    weightSelectionSpinner.setSelection(i);
                                }
                            }
                            goalWeightEditBox.setHelperTextAlwaysShown(true);
                            goalWeightEditBox.setHelperText("Weight considered in '" + dataUser.get("GoalWeightUnit") + "'");
                        }

                        if (dataUser.get("ActivityLevel") != null) {
                            for (int i = 0; i < activityLevel.size(); i++) {
                                if (activityLevel.get(i).equals(dataUser.get("ActivityLevel"))) {
                                    activityLevelSpinner.setSelection(i);
                                }
                            }
                        }
                        if (dataUser.get("Goal") != null) {
                            for (int i = 0; i < goalDataList.size(); i++) {
                                if (goalDataList.get(i).equals(dataUser.get("Goal"))) {
                                    goalSpinner.setSelection(i);
                                }
                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buildWeightSpinner() {
        ArrayList<String> weightData = new ArrayList<String>();
        weightData.add("kg");
        weightData.add("lbs");

        weightDataList.clear();
        weightDataList.addAll(weightData);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, weightDataList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        weightSelectionSpinner.setAdapter(dataAdapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), weightSelectionSpinner);
    }

    public void buildGoalData() {
        ArrayList<String> goalData = new ArrayList<String>();
        goalData.add("Select your goal");
        goalData.add("Gain Weight");
        goalData.add("Lose Weight");
        goalData.add("Maintain Weight");
        goalData.add("Fitness");

        goalDataList.clear();
        goalDataList.addAll(goalData);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, goalData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        goalSpinner.setAdapter(dataAdapter);

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String goal = ((String) goalSpinner.getSelectedItem());

                if (goal.equalsIgnoreCase("Gain Weight") || goal.equalsIgnoreCase("Lose Weight")) {
                    goalWeightArea.setVisibility(View.VISIBLE);
                } else {
                    goalWeightArea.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), goalSpinner);
    }


    public void buildActivityLevelData() {
        ArrayList<String> activityLevelData = new ArrayList<String>();
        activityLevelData.add("Select your Activity Level");
        activityLevelData.add("Sedentary");
        activityLevelData.add("Lightly Active");
        activityLevelData.add("Moderately Active");
        activityLevelData.add("Very Active");
        activityLevelData.add("Extremely Active");

        activityLevel.clear();
        activityLevel.addAll(activityLevelData);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, activityLevelData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        activityLevelSpinner.setAdapter(dataAdapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), activityLevelSpinner);
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

        String activityLevel = ((String) activityLevelSpinner.getSelectedItem());
        String goal = ((String) goalSpinner.getSelectedItem());

        if (activityLevel.equalsIgnoreCase("Select your Activity Level")) {
//            activityLevelEditBox.setError("Please enter your activity level");
            generalFunc.showGeneralMessage("", "Please select your activity level.");
        }
        if (goal.equalsIgnoreCase("Select your goal")) {
            generalFunc.showGeneralMessage("", "Please select your goal.");
//            goalEditBox.setError("Please enter your goal");
        }

        if (activityLevel.equalsIgnoreCase("Select your Activity Level") || goal.equalsIgnoreCase("Select your goal")) {
            return;
        }

        Utils.showLoader(userProfileAct, "", "Setting your detail");

        String weightSelectionUnit = ((String) weightSelectionSpinner.getSelectedItem());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("ActivityLevel", activityLevel);
        dataMap.put("Goal", goal);
        dataMap.put("GoalWeight", goalWeightEditBox.getText().toString());
        dataMap.put("GoalWeightUnit", weightSelectionUnit);

        Utils.printLog("dataMap", "::" + dataMap.toString());
        homeDatabase.updateChildren(dataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Utils.dismissLoader();
                if (databaseError != null) {
                    generalFunc.showGeneralMessage("", "Data could not be saved. " + databaseError.getMessage());
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");

                    checkInitialProfileData();
                }
            }
        });


    }

    public void checkInitialProfileData(){

        Utils.showLoader(userProfileAct,"Checking Profile Data", "Please wait we are checking your profile.");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        Log.e("!_@@<DATA>", "" + dataUser.toString());

                        if (dataUser.get("Weight") == null || dataUser.get("Height") == null || dataUser.get("Age") == null || dataUser.get("Gender") == null || dataUser.get("Name") == null || dataUser.get("Measurement") == null || dataUser.get("ActivityLevel") == null || dataUser.get("Goal") == null || dataUser.get("GoalWeight") == null) {

                            generalFunc.showGeneralMessage("","Please set all other data first.");
                        }else{
                            generalFunc.storedata("PROFILE_DATA_SET", "Yes");
                            userProfileAct.restartActivity();
                        }
                    } else {
                        generalFunc.showGeneralMessage("","Please set all other data first.");

                    }

                } else {
                    generalFunc.showGeneralMessage("","Please set all other data first.");
                }

                Utils.dismissLoader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.dismissLoader();
            }
        });
    }
}
