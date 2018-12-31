package com.example.android.sfwhf1;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.adapter.profilePagesAdapter;
import com.example.android.fragments.fragment_profile_mesurement;
import com.example.android.fragments.fragment_profile_user_body_info;
import com.example.android.fragments.fragment_profile_user_goal;
import com.example.android.fragments.fragment_profile_user_name;
import com.example.android.fragments.fragment_profile_user_personal_info;
import com.example.android.utils.GeneralFunctions;
import com.example.android.utils.Utils;
import com.example.android.view.CirclePageIndicator;
import com.example.android.view.CreateRoundedView;
import com.example.android.view.GenerateAlertBox;
import com.example.android.view.SelectableRoundedImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY_NAME = "Temp";
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int SELECT_PICTURE = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private Uri fileUri;

    ViewPager pager;
    public ArrayList<Fragment> listFragment;
    TextView skipTxt;
    TextView doneTxt;

    ImageView userNameEditImgView;
    ImageView measurementEditImgView;
    ImageView weightEditImgView;
    ImageView heightEditImgView;
    ImageView birthYearEditImgView;
    ImageView genderEditImgView;
    ImageView activityLevelEditImgView;
    ImageView goalEditImgView;
    ImageView goalWEditImgView;

    TextView bmiTxtView;
    TextView userNameVTxtView;
    TextView measurementVTxtView;
    TextView weightVTxtView;
    TextView heightVTxtView;
    TextView birthYearVTxtView;
    TextView genderVTxtView;
    TextView activityLevelVTxtView;
    TextView goalVTxtView;
    TextView goalWVTxtView;

    View desiredWeightArea;

    public View fragContainerView;

    public ImageView arrowImgView;
    public ImageView leftArrowImgView;

    View profileView;
    public View pagerArea;

    profilePagesAdapter adapter;

    CirclePageIndicator circlePageIndictor;

    private FirebaseAuth firebaseAuth;
    DatabaseReference homeDatabase;

    GeneralFunctions generalFunc;


    SelectableRoundedImageView userProfileImgView;
    SelectableRoundedImageView editIconImgView;
    RelativeLayout userImgArea;

    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Profile");

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        circlePageIndictor = (CirclePageIndicator) findViewById(R.id.circlePageIndictor);
        skipTxt = (TextView) findViewById(R.id.skipTxt);
        doneTxt = (TextView) findViewById(R.id.doneTxt);
        fragContainerView = findViewById(R.id.fragContainerView);
        pagerArea = findViewById(R.id.pagerArea);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        userNameEditImgView = (ImageView) findViewById(R.id.userNameEditImgView);
        measurementEditImgView = (ImageView) findViewById(R.id.measurementEditImgView);
        weightEditImgView = (ImageView) findViewById(R.id.weightEditImgView);
        heightEditImgView = (ImageView) findViewById(R.id.heightEditImgView);
        birthYearEditImgView = (ImageView) findViewById(R.id.birthYearEditImgView);
        genderEditImgView = (ImageView) findViewById(R.id.genderEditImgView);
        activityLevelEditImgView = (ImageView) findViewById(R.id.activityLevelEditImgView);
        goalEditImgView = (ImageView) findViewById(R.id.goalEditImgView);
        goalWEditImgView = (ImageView) findViewById(R.id.goalWEditImgView);

        bmiTxtView = (TextView) findViewById(R.id.bmiTxtView);
        userNameVTxtView = (TextView) findViewById(R.id.userNameVTxtView);
        measurementVTxtView = (TextView) findViewById(R.id.measurementVTxtView);
        weightVTxtView = (TextView) findViewById(R.id.weightVTxtView);
        heightVTxtView = (TextView) findViewById(R.id.heightVTxtView);
        birthYearVTxtView = (TextView) findViewById(R.id.birthYearVTxtView);
        genderVTxtView = (TextView) findViewById(R.id.genderVTxtView);
        activityLevelVTxtView = (TextView) findViewById(R.id.activityLevelVTxtView);
        goalVTxtView = (TextView) findViewById(R.id.goalVTxtView);
        goalWVTxtView = (TextView) findViewById(R.id.goalWVTxtView);

        desiredWeightArea = findViewById(R.id.desiredWeightArea);

        arrowImgView = (ImageView) findViewById(R.id.arrowImgView);
        leftArrowImgView = (ImageView) findViewById(R.id.leftArrowImgView);

        userProfileImgView = (SelectableRoundedImageView) findViewById(R.id.userProfileImgView);
        editIconImgView = (SelectableRoundedImageView) findViewById(R.id.editIconImgView);
        userImgArea = (RelativeLayout) findViewById(R.id.userImgArea);

        profileView = findViewById(R.id.profileView);
        generalFunc = new GeneralFunctions(UserProfileActivity.this);


        leftArrowImgView.setOnClickListener(new setOnClickList());
        userNameEditImgView.setOnClickListener(new setOnClickList());
        measurementEditImgView.setOnClickListener(new setOnClickList());
        weightEditImgView.setOnClickListener(new setOnClickList());
        heightEditImgView.setOnClickListener(new setOnClickList());
        birthYearEditImgView.setOnClickListener(new setOnClickList());
        genderEditImgView.setOnClickListener(new setOnClickList());
        activityLevelEditImgView.setOnClickListener(new setOnClickList());
        goalEditImgView.setOnClickListener(new setOnClickList());
        goalWEditImgView.setOnClickListener(new setOnClickList());
        userImgArea.setOnClickListener(new setOnClickList());


        firebaseAuth = FirebaseAuth.getInstance();

        homeDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserProfile");


        checkDataExistOrNot();

    }


    public void checkDataExistOrNot() {
        loadingBar.setVisibility(View.VISIBLE);
        pagerArea.setVisibility(View.GONE);
        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        Log.e("!_@@<DATA>", "" + dataUser.toString());

                        if (dataUser.get("Weight") != null && dataUser.get("Height") != null && dataUser.get("Age") != null && dataUser.get("Gender") != null && dataUser.get("Name") != null && dataUser.get("Measurement") != null && dataUser.get("ActivityLevel") != null && dataUser.get("Goal") != null && dataUser.get("GoalWeight") != null) {

                            profileView.setVisibility(View.VISIBLE);
                            getSupportActionBar().setElevation(0);


                            userProfileImgView.setImageResource(R.mipmap.ic_no_pic_user);
                            new CreateRoundedView(getResources().getColor(R.color.colorAccent), Utils.dipToPixels(UserProfileActivity.this, 15), 0,
                                    Color.parseColor("#00000000"), editIconImgView);

                            editIconImgView.setColorFilter(Color.parseColor("#FFFFFF"));

                            setUserData();

                        } else {
                            // Data not exist
                            dataNotExist();
                        }
                    } else {
                        // Data not exist
                        dataNotExist();
                    }

                } else {
                    // Data not exist
                    dataNotExist();
                }

                loadingBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingBar.setVisibility(View.GONE);
                pagerArea.setVisibility(View.VISIBLE);
            }
        });
    }

    public void dataNotExist() {
        pagerArea.setVisibility(View.VISIBLE);
        listFragment = new ArrayList<Fragment>();

        listFragment.add(new fragment_profile_user_name());
        listFragment.add(new fragment_profile_mesurement());
        listFragment.add(new fragment_profile_user_body_info());
        listFragment.add(new fragment_profile_user_personal_info());
        listFragment.add(new fragment_profile_user_goal());

        adapter = new profilePagesAdapter(getSupportFragmentManager(), listFragment);

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setOffscreenPageLimit(1);
        pager.setAdapter(adapter);

        circlePageIndictor.setViewPager(pager);

        arrowImgView.setOnClickListener(new setOnClickList());
        skipTxt.setOnClickListener(new setOnClickList());
        doneTxt.setOnClickListener(new setOnClickList());


        leftArrowImgView.setVisibility(View.INVISIBLE);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == listFragment.size() - 1) {
//                    doneTxt.setVisibility(View.VISIBLE);
//                    skipTxt.setVisibility(View.INVISIBLE);
                    arrowImgView.setVisibility(View.INVISIBLE);
                    leftArrowImgView.setVisibility(View.VISIBLE);
                } else if (position == 0) {

                    arrowImgView.setVisibility(View.VISIBLE);
                    leftArrowImgView.setVisibility(View.INVISIBLE);
//                    if (skipTxt.getVisibility() != View.VISIBLE) {
//                        doneTxt.setVisibility(View.GONE);
//                        skipTxt.setVisibility(View.VISIBLE);
//                        arrowImgView.setVisibility(View.VISIBLE);
//                    }
                } else {

                    arrowImgView.setVisibility(View.VISIBLE);
                    leftArrowImgView.setVisibility(View.VISIBLE);
                }

                switch (position){
                    case 0:
                        fragment_profile_user_name userNameFrag = (fragment_profile_user_name) listFragment.get(position);
                        userNameFrag.checkData();
                        break;
                    case 1:
                        break;
                    case 2:
                        fragment_profile_user_body_info userBodyInfoFrag = (fragment_profile_user_body_info) listFragment.get(position);
                        userBodyInfoFrag.checkData();
                        break;
                    case 3:
                        fragment_profile_user_personal_info userPersonalInfoFrag = (fragment_profile_user_personal_info) listFragment.get(position);
                        userPersonalInfoFrag.checkData();
                        break;
                    case 4:
                        fragment_profile_user_goal userGoalFrag = (fragment_profile_user_goal) listFragment.get(position);
                        userGoalFrag.checkData();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setUserData() {

        homeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    HashMap<String, String> dataUser = (HashMap<String, String>) dataSnapshot.getValue();

                    if (dataUser != null) {
                        Log.e("!_@@<DATA>", "" + dataUser.toString());
                        double userWeightForBMI = 1;
                        double userHeightForBMI = 1;

                        if (dataUser.get("ImageUrl") != null) {
                            Picasso.with(UserProfileActivity.this)
                                    .load(dataUser.get("ImageUrl"))
                                    .placeholder(R.mipmap.ic_no_pic_user) // optional
                                    .error(R.mipmap.ic_no_pic_user)         // optional
                                    .into(userProfileImgView);
                        }

                        if (dataUser.get("Weight") != null) {
                            weightVTxtView.setText(dataUser.get("Weight") + " " + dataUser.get("WeightUnit"));

                            if (dataUser.get("WeightUnit").equalsIgnoreCase("kg")) {
                                userWeightForBMI = generalFunc.parseDouble(1, dataUser.get("Weight")) * 2.20462;
                            } else {
                                userWeightForBMI = generalFunc.parseDouble(1, dataUser.get("Weight"));
                            }
                            userWeightForBMI = userWeightForBMI * 0.45;
                        }
                        if (dataUser.get("Height") != null) {
                            heightVTxtView.setText(dataUser.get("Height") + " " + dataUser.get("HeightUnit"));

                            if (dataUser.get("HeightUnit").equalsIgnoreCase("cm")) {
                                userHeightForBMI = generalFunc.parseDouble(1, dataUser.get("HeightUnit")) * 0.393701;
                            } else {
                                userHeightForBMI = generalFunc.parseDouble(1, dataUser.get("HeightUnit"));
                            }

                            userHeightForBMI = userHeightForBMI * 0.025;

                            double finalBMI = userWeightForBMI / (userHeightForBMI * userHeightForBMI);
                            //  bmiTxtView.setText("BMI: " + String.format("%.0f", finalBMI));
                        }
                        if (dataUser.get("Age") != null) {
                            birthYearVTxtView.setText(dataUser.get("Age"));
                        }
                        if (dataUser.get("Gender") != null) {
                            genderVTxtView.setText(dataUser.get("Gender"));
                        }
                        if (dataUser.get("Name") != null) {
                            userNameVTxtView.setText(dataUser.get("Name"));
                        }
                        if (dataUser.get("Measurement") != null) {
                            measurementVTxtView.setText(dataUser.get("Measurement"));
                        }
                        if (dataUser.get("ActivityLevel") != null) {
                            activityLevelVTxtView.setText(dataUser.get("ActivityLevel"));
                        }
                        if (dataUser.get("Goal") != null) {
                            goalVTxtView.setText(dataUser.get("Goal"));

                            if (dataUser.get("Goal").equalsIgnoreCase("Gain Weight") || dataUser.get("Goal").equalsIgnoreCase("Lose Weight")) {
                                desiredWeightArea.setVisibility(View.VISIBLE);

                                if (dataUser.get("GoalWeight") != null) {
                                    goalWVTxtView.setText(dataUser.get("GoalWeight") + " " + dataUser.get("GoalWeightUnit"));
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

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.skipTxt:
                    break;
                case R.id.userImgArea:
                    Utils.printLog("Permission","::"+generalFunc.isCameraPermissionGranted());
                    Utils.printLog("Permission","::"+generalFunc.isStoragePermissionGranted());
                    if (generalFunc.isCameraPermissionGranted() && generalFunc.isStoragePermissionGranted()) {
                        new ImageSourceDialog().run();
                    } else {
                        generalFunc.showMessage(getCurrView(), "Allow this app to use camera and gallery.");
                    }
                    break;
                case R.id.userNameEditImgView:
                    openEditUserNameFragment();
                    break;
                case R.id.measurementEditImgView:
                    openEditMeasurementFragment();
                    break;
                case R.id.weightEditImgView:
                    openEditBodyInfoFragment();
                    break;
                case R.id.heightEditImgView:
                    openEditBodyInfoFragment();
                    break;
                case R.id.birthYearEditImgView:
                    openEditPersonalInfoFragment();
                    break;
                case R.id.genderEditImgView:
                    openEditPersonalInfoFragment();
                    break;
                case R.id.goalEditImgView:
                    openEditGoalFragment();
                    break;
                case R.id.activityLevelEditImgView:
                    openEditGoalFragment();
                    break;
                case R.id.goalWEditImgView:
                    openEditGoalFragment();
                    break;
                case R.id.leftArrowImgView:
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                    break;

                case R.id.doneTxt:
                    break;

                case R.id.arrowImgView:
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (fragContainerView.getVisibility() == View.VISIBLE) {
            fragContainerView.setVisibility(View.GONE);
            removeSubFragments();
            setUserData();
            return;
        }

        if(getIntent().getStringExtra("IS_DATA_AVAIL") != null && getIntent().getStringExtra("IS_DATA_AVAIL").equalsIgnoreCase("No")){
            checkProfileData();
            return;
        }

        super.onBackPressed();
    }

    public void checkProfileData(){

        Utils.showLoader(UserProfileActivity.this,"Checking Profile Data", "Please wait we are checking your profile.");
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
                            dataNotExistAlert();
                        }else{
                            Utils.printLog("IS_DATA_AVAIL","::MainStart::");
                            startActivity(new Intent(getApplicationContext(), Main.class));
                            finish();
                        }
                    } else {
                        dataNotExistAlert();
                    }

                } else {
                    dataNotExistAlert();
                }

                Utils.dismissLoader();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.dismissLoader();
            }
        });
    }

    public void dataNotExistAlert(){
        final GenerateAlertBox alertBox = new GenerateAlertBox(UserProfileActivity.this);
        alertBox.setContentMessage("","Your profile data is not set. Are you sure you want to exit?");
        alertBox.setNegativeBtn("NO");
        alertBox.setPositiveBtn("YES");
        alertBox.setCancelable(false);
        alertBox.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
            @Override
            public void handleBtnClick(int btn_id) {
                alertBox.closeAlertBox();
                if(btn_id == 1){
                    finish();
                }
            }
        });
        alertBox.showAlertBox();
    }

    public void removeSubFragments() {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragContainer)).commit();

    }

    public void openEditUserNameFragment() {

        fragContainerView.setVisibility(View.VISIBLE);

        Utils.runGC();
        fragment_profile_user_name editUsernameFrag = new fragment_profile_user_name();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, editUsernameFrag).commit();
    }

    public void openEditMeasurementFragment() {

        fragContainerView.setVisibility(View.VISIBLE);

        Utils.runGC();
        fragment_profile_mesurement profileMeasurement = new fragment_profile_mesurement();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, profileMeasurement).commit();
    }


    public void openEditGoalFragment() {

        fragContainerView.setVisibility(View.VISIBLE);

        Utils.runGC();
        fragment_profile_user_goal userGoalFrag = new fragment_profile_user_goal();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, userGoalFrag).commit();
    }


    public void openEditPersonalInfoFragment() {

        fragContainerView.setVisibility(View.VISIBLE);

        Utils.runGC();
        fragment_profile_user_personal_info personalInfoFrag = new fragment_profile_user_personal_info();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, personalInfoFrag).commit();
    }

    public void openEditBodyInfoFragment() {

        fragContainerView.setVisibility(View.VISIBLE);

        Utils.runGC();
        fragment_profile_user_body_info bodyInfoFrag = new fragment_profile_user_body_info();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, bodyInfoFrag).commit();
    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        mIntent.putExtra("IS_DATA_AVAIL",getIntent().getStringExtra("IS_DATA_AVAIL"));
        Utils.printLog("IS_DATA_AVAIL","::"+getIntent().getStringExtra("IS_DATA_AVAIL"));
        finish();
        startActivity(mIntent);
    }

    class ImageSourceDialog implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_img_update = new Dialog(UserProfileActivity.this, R.style.ImageSourceDialogStyle);

            dialog_img_update.setContentView(R.layout.design_image_source_select);

            TextView chooseImgHTxt = (TextView) dialog_img_update.findViewById(R.id.chooseImgHTxt);
            chooseImgHTxt.setText("Choose Category");

            SelectableRoundedImageView cameraIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.cameraIconImgView);
            SelectableRoundedImageView galleryIconImgView = (SelectableRoundedImageView) dialog_img_update.findViewById(R.id.galleryIconImgView);

            ImageView closeDialogImgView = (ImageView) dialog_img_update.findViewById(R.id.closeDialogImgView);

            closeDialogImgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (dialog_img_update != null) {
                        dialog_img_update.cancel();
                    }
                }
            });

            new CreateRoundedView(getResources().getColor(R.color.colorPrimaryDark), Utils.dipToPixels(UserProfileActivity.this, 25), 0,
                    Color.parseColor("#00000000"), cameraIconImgView);

            cameraIconImgView.setColorFilter(Color.parseColor("#FFFFFF"));

            new CreateRoundedView(getResources().getColor(R.color.colorPrimaryDark), Utils.dipToPixels(UserProfileActivity.this, 25), 0,
                    Color.parseColor("#00000000"), galleryIconImgView);

            galleryIconImgView.setColorFilter(Color.parseColor("#FFFFFF"));


            cameraIconImgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (dialog_img_update != null) {
                        dialog_img_update.cancel();
                    }

                    if (!isDeviceSupportCamera()) {
                        generalFunc.showMessage(getCurrView(), "Camera is not supported in your device.");
                    } else {
                        chooseFromCamera();
                    }

                }
            });

            galleryIconImgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (dialog_img_update != null) {
                        dialog_img_update.cancel();
                    }
                    chooseFromGallery();
                }
            });

            dialog_img_update.setCanceledOnTouchOutside(true);

            Window window = dialog_img_update.getWindow();
            window.setGravity(Gravity.BOTTOM);

            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog_img_update.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog_img_update.show();

        }

    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void chooseFromGallery() {
        // System.out.println("Gallery pressed");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void chooseFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public View getCurrView() {
        return generalFunc.getCurrentView(UserProfileActivity.this);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isStoragePermissionAvail = generalFunc.isStoragePermissionGranted();
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                String selPath = new ImageFilePath().getPath(UserProfileActivity.this, fileUri);

                if(isStoragePermissionAvail){
                    uploadImage(selPath);
                }
//                if (isValidImageResolution(fileUri.getPath()) == true && isStoragePermissionAvail) {
//                    uploadImage(fileUri.getPath());

//                } else {
//                    generalFunc.showGeneralMessage("", "Please select greater resolution image. Minimum is 256 * 256.");
//                }


            } else if (resultCode == RESULT_CANCELED) {

            } else {
                generalFunc.showMessage(getCurrView(), "Failed to capture image");
            }
        }
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {

                Uri selectedImageUri = data.getData();

                String selectedImagePath = new ImageFilePath().getPath(UserProfileActivity.this, selectedImageUri);

                if(isStoragePermissionAvail){

                    uploadImage(selectedImagePath);
                }
//                String selectedImagePath = (getPath(selectedImageUri) == null) ? selectedImageUri.getPath() : getPath(selectedImageUri);

//                if (isValidImageResolution(selectedImagePath) == true && isStoragePermissionAvail) {
//                    uploadImage(selectedImagePath);
//                } else {
//                    generalFunc.showGeneralMessage("", "Please select image which has minimum is 256 * 256 resolution.");
//                }

            }
        }
    }

    public void uploadImage(String path) {

        final Bitmap bmp = decodeSampledBitmap(path, 250, 250);
//        userProfileImgView.setImageBitmap(bmp);
        StorageReference userImageDbRef = FirebaseStorage.getInstance().getReference().child("UserImages/" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Utils.showLoader(UserProfileActivity.this, "", "Uploading image");

        final Bitmap bitTemp = bmp;
        UploadTask uploadTask = userImageDbRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Utils.dismissLoader();
                generalFunc.showGeneralMessage("", "Image could not be saved. ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Utils.dismissLoader();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Utils.printLog("downloadUrl:", ":" + downloadUrl);
                String content = downloadUrl.toString();
                Utils.printLog("downloadUrl:", "content:" + content);


                setUserURLToServerImage(content, bitTemp);
            }
        });
    }

    public void setUserURLToServerImage(final String imgUrl, final Bitmap bmp) {

        Utils.showLoader(UserProfileActivity.this, "", "Setting your image");

        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("ImageUrl", imgUrl);

        homeDatabase.updateChildren(dataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Utils.dismissLoader();
                if (databaseError != null) {
                    generalFunc.showGeneralMessage("", "Image could not be saved. " + databaseError.getMessage());
                    System.out.println("Image could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Image saved successfully.");

                    userProfileImgView.setImageBitmap(bmp);

                    Picasso.with(UserProfileActivity.this)
                            .load(imgUrl)
                            .placeholder(R.mipmap.ic_no_pic_user) // optional
                            .error(R.mipmap.ic_no_pic_user)         // optional
                            .into(userProfileImgView);
                }
            }
        });
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmap(String pathName,
                                       int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    //I added this to have a good approximation of the screen size:
    private Bitmap decodeSampledBitmap(String pathName) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return decodeSampledBitmap(pathName, width, height);
    }

    public String getPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        try {
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                return filePath;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    public boolean isValidImageResolution(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;

        if (width >= 0 && height >= 0) {
            return true;
        }
        return true;
    }


    public class ImageFilePath {


        /**
         * Method for return file path of Gallery image
         *
         * @param context
         * @param uri
         * @return path of the selected image file from gallery
         */

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public String getPath(final Context context, final Uri uri) {

            //check here to KITKAT or new version
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        public String getDataColumn(Context context, Uri uri, String selection,
                                    String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }

}
