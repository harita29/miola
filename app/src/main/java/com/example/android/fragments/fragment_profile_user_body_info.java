package com.example.android.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.util.Log;
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
public class fragment_profile_user_body_info extends Fragment {


    View view;

    UserProfileActivity userProfileAct;
    GeneralFunctions generalFunc;
    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    Button submitBtn;
    MaterialEditText weightEditBox;
    MaterialEditText heightEditBox;

    View heightSpinnerArea;
    View weightSpinnerArea;

    AppCompatSpinner weightSelectionSpinner;
    AppCompatSpinner heightSelectionSpinner;
    ArrayList<String> weightDataList = new ArrayList<>();
    ArrayList<String> heightDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_profile_user_body_info, container, false);

        userProfileAct = (UserProfileActivity) getActivity();
        generalFunc = new GeneralFunctions(userProfileAct);

        firebaseAuth = FirebaseAuth.getInstance();
        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        weightEditBox = (MaterialEditText) view.findViewById(R.id.weightEditBox);
        heightEditBox = (MaterialEditText) view.findViewById(R.id.heightEditBox);
        weightSelectionSpinner = (AppCompatSpinner) view.findViewById(R.id.weightSelectionSpinner);
        heightSelectionSpinner = (AppCompatSpinner) view.findViewById(R.id.heightSelectionSpinner);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);

        heightSpinnerArea = view.findViewById(R.id.heightSpinnerArea);
        weightSpinnerArea = view.findViewById(R.id.weightSpinnerArea);

        weightEditBox.setBothText("Weight", "Enter your weight");
        heightEditBox.setBothText("Height", "Enter your height");

//        if (userProfileAct.fragContainerView.getVisibility() == View.GONE) {
        weightSpinnerArea.setVisibility(View.GONE);
        heightSpinnerArea.setVisibility(View.GONE);
//        } else {
//            weightSpinnerArea.setVisibility(View.VISIBLE);
//            heightSpinnerArea.setVisibility(View.VISIBLE);
//        }

        weightEditBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        heightEditBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        submitBtn.setOnClickListener(new setOnClickList());

        buildWeightSpinner();
        buildHeightSpinner();

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

                        if (dataUser.get("Weight") != null) {
                            weightEditBox.setText(dataUser.get("Weight"));
                        }

                        if (dataUser.get("WeightUnit") != null) {
                            for (int i = 0; i < weightDataList.size(); i++) {
                                if (weightDataList.get(i).equals(dataUser.get("WeightUnit"))) {
                                    weightSelectionSpinner.setSelection(i);
                                }
                            }
                            weightEditBox.setHelperTextAlwaysShown(true);
                            weightEditBox.setHelperText("Weight considered in '" + dataUser.get("WeightUnit") + "'");

                        }
                        if (dataUser.get("Height") != null) {
                            heightEditBox.setText(dataUser.get("Height"));

                            for (int i = 0; i < heightDataList.size(); i++) {
                                if (heightDataList.get(i).equals(dataUser.get("HeightUnit"))) {
                                    heightSelectionSpinner.setSelection(i);
                                }
                            }
                        }

                        if (dataUser.get("HeightUnit") != null) {
                            for (int i = 0; i < heightDataList.size(); i++) {
                                if (heightDataList.get(i).equals(dataUser.get("HeightUnit"))) {
                                    heightSelectionSpinner.setSelection(i);
                                }
                            }
                            heightEditBox.setHelperTextAlwaysShown(true);
                            heightEditBox.setHelperText("Height considered in '" + dataUser.get("HeightUnit") + "'");
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

    public void buildHeightSpinner() {
        ArrayList<String> heightData = new ArrayList<String>();
        heightData.add("inch");
        heightData.add("cm");

        heightDataList.clear();
        heightDataList.addAll(heightData);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, heightDataList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        heightSelectionSpinner.setAdapter(dataAdapter);

        new CreateRoundedView(Color.parseColor("#FFFFFF"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#DEDEDE"), heightSelectionSpinner);
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

        String weight = weightEditBox.getText().toString();
        String height = heightEditBox.getText().toString();
        String weightUnit = ((String) weightSelectionSpinner.getSelectedItem());
        String heightUnit = ((String) heightSelectionSpinner.getSelectedItem());

        if (weight.trim().equals("")) {
            weightEditBox.setError("Please enter your weight");
        }
        if (height.trim().equals("")) {
            heightEditBox.setError("Please enter your height");
        }

        if (weight.trim().equals("") && height.trim().equals("")) {
            return;
        }

        Utils.showLoader(userProfileAct, "", "Setting your detail");

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("Weight", weight);
        dataMap.put("WeightUnit", weightUnit);
        dataMap.put("Height", height);
        dataMap.put("HeightUnit", heightUnit);

        Utils.printLog("dataMap","::"+dataMap.toString());
        homeDatabase.updateChildren(dataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Utils.dismissLoader();
                if (databaseError != null) {
                    generalFunc.showGeneralMessage("", "Data could not be saved. " + databaseError.getMessage());
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
//                    userProfileAct.arrowImgView.performClick();
                    if (userProfileAct.fragContainerView.getVisibility() == View.VISIBLE) {
                        userProfileAct.onBackPressed();
                    } else {
                        userProfileAct.arrowImgView.performClick();
                    }
                }
            }
        });


    }
}
