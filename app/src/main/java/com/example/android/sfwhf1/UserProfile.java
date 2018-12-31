package com.example.android.sfwhf1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UserProfile extends AppCompatActivity {

    private EditText mName;
    private EditText mWeight;
    private EditText mHeight;
    private EditText mDob;
    private RadioGroup radio_g;
    private RadioButton radio_b;
    private ImageView image;
    private Spinner wUnits;
    private Spinner hUnits;
    private static final double ftToCm = 0.025; // convert ft to cm to calculate BMI
    private static final double lbToKg = 0.45; // convert lbs to kg to calculate BMI
    private DecimalFormat formater = new DecimalFormat("#.#");

    private Button submitBtn;
    private Button browseBtn;
    //IMAGE HOLD URI
    Uri imageHoldUri = null;
    private static final int CAMERA_REQUEST = 3;
    private static final int SELECT_FILE = 2;

    //Firebase Database fields;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        mName = (EditText) findViewById(R.id.nameField);
        mWeight = (EditText) findViewById(R.id.weightField);
        mHeight = (EditText) findViewById(R.id.heightField);
        mDob = (EditText) findViewById(R.id.dobField);
        radio_g = (RadioGroup) findViewById(R.id.rg_gender);
        wUnits = (Spinner) findViewById(R.id.w_units);
        hUnits = (Spinner) findViewById(R.id.h_units);
        int selectedid = radio_g.getCheckedRadioButtonId();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        radio_b = (RadioButton) findViewById(selectedid);
        image = (ImageView) findViewById(R.id.imageV);

        /*
        wUnits=(Spinner)findViewById(R.id.w_units);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(UserProfile.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.w_units) );
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wUnits.setAdapter(myadapter);

        hUnits=(Spinner)findViewById(R.id.h_units);
        ArrayAdapter<String> myadapter1 = new ArrayAdapter<String>(UserProfile.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.h_units) );
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hUnits.setAdapter(myadapter1);
*/

        browseBtn = (Button) findViewById(R.id.browse_btn);
        submitBtn = (Button) findViewById(R.id.submitbtn);

        storageRef = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        //String userId= Base64.encodeToString(Utils.getText(mName).getBytes(), Base64.NO_WRAP | Base64.NO_PADDING | Base64.URL_SAFE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        // storageRef= storage.getReferenceFromUrl("gs://sfwhf-60fe0.appspot.com/images");
        // mProgress=new ProgressDialog(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startRegister();

            }

        });
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chooseImage();
            }
        });
    }

    private void startRegister() {
        final String name = mName.getText().toString().trim();
        String uWeight = mWeight.getText().toString().trim();
        String uHeight = mHeight.getText().toString().trim();
        String dob = mDob.getText().toString().trim();
        String gender = radio_b.getText().toString().trim();
        final int weight = Integer.parseInt(uWeight);
        final int height = Integer.parseInt(uHeight);
        final int uDob = Integer.parseInt(dob);
        String weightUnit = wUnits.getSelectedItem().toString().trim();
        String heightUnit = hUnits.getSelectedItem().toString().trim();
        final double wUnit;
        final double hUnit;
        final int genderK;
        //convert each unit to int
        wUnit = weightMeasurement(weightUnit);
        hUnit = heightMeasurement(heightUnit);
        genderK = genderCheck(gender);
        int bmi = calculateBMI(wUnit,hUnit);

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(uWeight) && !TextUtils.isEmpty(uHeight) && !TextUtils.isEmpty(dob)) {
            if(imageHoldUri!=null){
                mProgress.setMessage("Creating profile...");
                mProgress.show();



                StorageReference mChildStorage = storageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        userId = mUser.getUid();
                        final Uri imageUrl = taskSnapshot.getDownloadUrl();

                        mDatabase.child("users").child(userId).child("imageurl").setValue(imageUrl.toString());
                        /*User user = new User(name,wUnit,hUnit,uDob,genderK, bmi, imageUrl);
                        mDatabase.child("users").child(userId).push().setValue(user);
                        mProgress.dismiss();
*/
                        finish();
                        Intent moveToHome = new Intent(UserProfile.this, Main.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);


                    }
                });
            }else{

                Toast.makeText(UserProfile.this, "Please select the profile picture", Toast.LENGTH_LONG).show();

            }

        }else{

            Toast.makeText(UserProfile.this, "Please enter username and status", Toast.LENGTH_LONG).show();

        }

    }

/*

    private void chooseImage() {
        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            //call Firebase Reference
            StorageReference filepath = storageRef.child("users").child("Photos").child(imageUri.getLastPathSegment());
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                image.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
*/
    public double weightMeasurement(String weight){
        double convertDouble = Double.parseDouble(weight);
        double result = 0;

        //If user chooses lb, measurement = 1, otherwise 2
        if(weight.equals("lb")){
            result = convertDouble * lbToKg;
        }else{
            result = convertDouble;
        }
        formater.format(result);
        return result;
    }

    public double heightMeasurement(String height){
        double convertInt = Double.parseDouble(height);
        double result = 0;

        //If user chooses ft, measurement = 1, otherwise 2
        if(height.equals("ft")){
            result = convertInt * ftToCm;
        }else{
            result = convertInt;
        }
        formater.format(result);
        return result;
    }

    public int genderCheck(String gender){
        int genderKind = 0;

        //convert gender following:
        //Female = 1
        //Male = 2
        //Undefined = 3

        if(gender.equals("Female")){
            genderKind = 1;
        }else if(gender.equals("Male")){
            genderKind = 2;
        }else{
            genderKind = 3;
        }
        return genderKind;
    }

    public int calculateBMI(double wUnit, double hUnit){
        int result = 0;
        double doubleHeight = hUnit * hUnit;
        double heightAndWeight = 0;
        heightAndWeight = wUnit / doubleHeight;
        String middle = Double.toString(heightAndWeight);
        result = Integer.parseInt(middle);

        return result;
    }
}







