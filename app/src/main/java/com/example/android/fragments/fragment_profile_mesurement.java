package com.example.android.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_profile_mesurement extends Fragment {


    View view;

    TextView lbInchTxtView;
    TextView kgCmTxtView;

    UserProfileActivity userProfileAct;
    GeneralFunctions generalFunc;
    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_profile_mesurement, container, false);
        userProfileAct = (UserProfileActivity) getActivity();
        generalFunc = new GeneralFunctions(userProfileAct);
        lbInchTxtView = (TextView) view.findViewById(R.id.lbInchTxtView);
        kgCmTxtView = (TextView) view.findViewById(R.id.kgCmTxtView);

        lbInchTxtView.setOnClickListener(new setOnClickList());
        kgCmTxtView.setOnClickListener(new setOnClickList());
        firebaseAuth = FirebaseAuth.getInstance();
        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");
        new CreateRoundedView(Color.parseColor("#becdf0"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#333333"), lbInchTxtView);

        new CreateRoundedView(Color.parseColor("#becdf0"), Utils.dipToPixels(getContext(), 10), Utils.dipToPixels(getContext(), 1), Color.parseColor("#333333"), kgCmTxtView);


        return view;
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lbInchTxtView:
                    setMeasurementValue("lb/inch");
                    break;
                case R.id.kgCmTxtView:
                    setMeasurementValue("kg/cm");
                    break;
            }
        }
    }

    public void setMeasurementValue(String value) {

        Utils.showLoader(userProfileAct, "", "Setting your Measurement");

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("Measurement", value);
        if (value.equalsIgnoreCase("kg/cm")) {
            dataMap.put("WeightUnit", "kg");
            dataMap.put("GoalWeightUnit", "kg");
            dataMap.put("HeightUnit", "cm");
        } else {
            dataMap.put("WeightUnit", "lbs");
            dataMap.put("GoalWeightUnit", "lbs");
            dataMap.put("HeightUnit", "inch");
        }

        if (userProfileAct.fragContainerView.getVisibility() == View.VISIBLE) {
            configuringData(dataMap);
        } else {
            continueUpdate(dataMap);
        }
    }

    public void continueUpdate(HashMap<String, Object> dataMap) {
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

    public void configuringData(final HashMap<String, Object> dataMap) {
        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        if (dataMap.get("Measurement").equals(dataUser.get("Measurement"))) {
                            continueUpdate(dataMap);
                        } else {
                            if (dataUser.get("Weight") != null) {
                                double weightValue = 0.0;
                                if (dataUser.get("WeightUnit").equalsIgnoreCase("kg")) {
                                    weightValue = generalFunc.parseDouble(0.0, dataUser.get("Weight")) * 2.20462;
                                } else {

                                    weightValue = generalFunc.parseDouble(0.0, dataUser.get("Weight")) * 0.453592;
                                }
                                dataMap.put("Weight", String.format("%.2f", weightValue) );
                            }


                            if (dataUser.get("Height") != null) {
                                double heightValue = 0.0;
                                if (dataUser.get("HeightUnit").equalsIgnoreCase("cm")) {
                                    heightValue = generalFunc.parseDouble(0.0, dataUser.get("Height")) * 0.393701;
                                } else {

                                    heightValue = generalFunc.parseDouble(0.0, dataUser.get("Height")) * 2.54;
                                }
                                dataMap.put("Height", String.format("%.2f", heightValue) );
                            }

                            if (dataUser.get("GoalWeight") != null) {
                                double goalWeightValue = 0.0;
                                if (dataUser.get("GoalWeightUnit").equalsIgnoreCase("kg")) {
                                    goalWeightValue = generalFunc.parseDouble(0.0, dataUser.get("GoalWeight")) * 2.20462;
                                } else {

                                    goalWeightValue = generalFunc.parseDouble(0.0, dataUser.get("GoalWeight")) * 0.453592;
                                }
                                dataMap.put("GoalWeight",String.format("%.2f", goalWeightValue));
                            }
                            continueUpdate(dataMap);
                        }

                    }else{
                        continueUpdate(dataMap);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
