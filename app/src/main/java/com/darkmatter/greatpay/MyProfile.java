package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView edit,nameTv;
    private Uri filePath;
    private String email,username1,phone,namee,url,addrs;
    boolean selected =false;
    String state,myusername;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ImageView select;
    private TextView emailtv,mobileTv,AddresTv,usernameTv;
    private CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_null);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        emailtv=findViewById(R.id.emailId);
        edit=findViewById(R.id.edit_profile);
        nameTv=findViewById(R.id.nme);
        usernameTv=findViewById(R.id.usernameTv);
        select=findViewById(R.id.selection);
        imageView=findViewById(R.id.p_images);
        mobileTv=findViewById(R.id.mobile);
        AddresTv=findViewById(R.id.address);
        ref= FirebaseDatabase.getInstance().getReference("Users");




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this,EditProfile.class));
            }
        });




        ref.orderByChild("uId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds1) {
                for(DataSnapshot dataSnapshot1:ds1.getChildren()){
                    myusername=""+dataSnapshot1.child("username").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                        .start(MyProfile.this);
            }
        });





//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                state="Not";
//
//                final String addresss=address.getText().toString().trim();
//                final String Name=name.getText().toString().trim();
//                final String phone=mobile.getText().toString().trim();
//                final String UserName=username.getText().toString().trim();
//
//
//
//                            HashMap<String,Object> hashMap=new HashMap<>();
//                            hashMap.put("address",addresss);
//                            hashMap.put("name",Name);
//                            hashMap.put("phone",phone);
//                            ref.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(MyProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                            Drawable d = getResources().getDrawable(profile_edittext);
//                            name.setEnabled(false);
//                            address.setEnabled(false);
//                            mobile.setEnabled(false);
//                            selected=false;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                name.setBackground(d);
//                            }
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                address.setBackground(d);
//                            }
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                mobile.setBackground(d);
//                            }
//                            edit.setText("Edit Profile");
//                            edit.setTextColor(Color.WHITE);
//                            loadProfile();
//
//
//
//
//
//            }
//        });

        loadProfile();






//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(selected){
//                    Drawable d = getResources().getDrawable(profile_edittext);
//                    selected=false;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        name.setBackground(d);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        address.setBackground(d);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        mobile.setBackground(d);
//                    }
//                    edit.setText("Edit Profile");
//                    edit.setTextColor(Color.WHITE);
//                    loadProfile();
//
//                }else {
//                    Drawable d = getResources().getDrawable(register_edittext);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        name.setBackground(d);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        address.setBackground(d);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        mobile.setBackground(d);
//                    }
//                    name.setEnabled(true);
//                    address.setEnabled(true);
//                    mobile.setEnabled(true);
//                    edit.setText("Cancel");
//                    edit.setTextColor(Color.RED);
//                    selected = true;
//                }
//
//            }
//        });
    }

    private void loadProfile() {
        ref.orderByChild("uId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for(DataSnapshot ds2: dataSnapshot1.getChildren()){
                    String email=""+ds2.child("email").getValue();
                    String phone=""+ds2.child("phone").getValue();
                    String namee=""+ds2.child("name").getValue();
                    String addrs=""+ds2.child("address").getValue();
                    String url=""+ds2.child("image").getValue();
                    String username=""+ds2.child("username").getValue();


                    mobileTv.setText(phone);
                    AddresTv.setText(addrs);
                    emailtv.setText(email);
                    nameTv.setText(namee);
                    usernameTv.setText(username);

                    Log.d("url", "onDataChange: "+url);

                    if(!addrs.isEmpty()){
//                        address.setText(addrs);
                    }
                    if(!url.isEmpty()){
                        Glide.with(getApplicationContext()).load(url).placeholder(R.drawable.profile).into(imageView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + user.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            String downloadUri=uriTask.getResult().toString();
                            if(uriTask.isSuccessful()){

                                DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Users");
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("image",downloadUri);
                                reference.child(user.getUid()).updateChildren(hashMap);


                                Toast.makeText(MyProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MyProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

}
