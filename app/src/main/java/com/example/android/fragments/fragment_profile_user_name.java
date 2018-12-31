package com.example.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.sfwhf1.R;
import com.example.android.sfwhf1.UserProfileActivity;
import com.example.android.utils.GeneralFunctions;
import com.example.android.utils.Utils;
import com.example.android.view.editBox.MaterialEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_profile_user_name extends Fragment {


    View view;

    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    GeneralFunctions generalFunc;

    UserProfileActivity userProfileAct;

    MaterialEditText userNameEditBox;
    Button skipButton;
    Button submitButton;

    View seperatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_profile_user_name, container, false);

        userProfileAct = (UserProfileActivity) getActivity();

        userNameEditBox = (MaterialEditText) view.findViewById(R.id.userNameEditBox);
        skipButton = (Button) view.findViewById(R.id.skipButton);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        seperatorView = view.findViewById(R.id.seperatorView);

        skipButton.setOnClickListener(new setOnClickList());
        submitButton.setOnClickListener(new setOnClickList());
        generalFunc = new GeneralFunctions(userProfileAct);
        firebaseAuth = FirebaseAuth.getInstance();
        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");

        userNameEditBox.setBothText("UserName","Enter your user name");

        checkData();

        if(userProfileAct.fragContainerView.getVisibility() == View.VISIBLE){
            skipButton.setVisibility(View.GONE);
            seperatorView.setVisibility(View.GONE);
        }

        return view;
    }

    public void checkData(){
        homeDatabase.child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null && dataSnapshot.getValue() != null){
                    userNameEditBox.setText(dataSnapshot.getValue().toString());
                }
//                System.out.println(dataSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.skipButton:
                    userProfileAct.arrowImgView.performClick();
                    break;
                case R.id.submitButton:
                    checkValues();
                    break;
            }
        }
    }

    public void checkValues() {
        if (userNameEditBox.getText().toString().trim().equals("")) {
            userNameEditBox.setError("Please enter UserName");
        } else {
            Utils.showLoader(userProfileAct,"","Setting your User Name");

            homeDatabase.child("Name").setValue(userNameEditBox.getText().toString(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Utils.dismissLoader();
                    if (databaseError != null) {
                        generalFunc.showGeneralMessage("","Data could not be saved. "+ databaseError.getMessage());
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                    } else {
                        System.out.println("Data saved successfully.");
                        if(userProfileAct.fragContainerView.getVisibility() == View.VISIBLE){
                            userProfileAct.onBackPressed();
                        }else{
                            skipButton.performClick();
                        }
                    }
                }
            });
        }
    }
}
