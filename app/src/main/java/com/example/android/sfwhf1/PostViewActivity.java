package com.example.android.sfwhf1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostViewActivity extends AppCompatActivity {
    private EditText uName;
    private EditText rName;
    private EditText uComments;
    private ImageView uimage;
    private Uri imageUri;
    private Button saveBtn;
    private static final int GALARY_REQUEST = 1;

    private ProgressDialog mPrograss;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        setTitle("POST PAGE");


        uName = (EditText) findViewById(R.id.uname);
        rName = (EditText) findViewById(R.id.title);
        uComments = (EditText) findViewById(R.id.comments);
        uimage = (ImageButton) findViewById(R.id.imagebtn);
        saveBtn = (Button) findViewById(R.id.btnSave);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("post");
        mStorage = FirebaseStorage.getInstance().getReference();
        mPrograss = new ProgressDialog(this);
        uimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent galaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent, GALARY_REQUEST);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==GALARY_REQUEST && resultCode==RESULT_OK){
            imageUri = data.getData();
            uimage.setImageURI(imageUri);

        }
    }


    private void save() {
        mPrograss.setMessage("uploading...");
        mPrograss.show();
        final String name = uName.getText().toString().trim();
        final String title = rName.getText().toString().trim();
        final String comments = uComments.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(title)) {
            StorageReference filepath = mStorage.child("Post_images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String id = mDatabase.push().getKey();
                    mDatabase.child(id).child("Name").setValue(name);
                    mDatabase.child(id).child("Title").setValue(title);
                    mDatabase.child(id).child("Comments").setValue(comments);
                    mDatabase.child(id).child("PostImage").setValue(downloadUrl.toString());
                    //mDatabase.child(id).child("uid").setValue(FirebaseAuth.getCurrentuser.getUid());
                    mPrograss.dismiss();
                    Toast.makeText(PostViewActivity.this, "successfully saved!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PostViewActivity.this, MainPost.class));


                }
            });


        }
    }
}
