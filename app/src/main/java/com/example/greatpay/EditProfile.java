package com.example.greatpay;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class EditProfile extends AppCompatActivity {
    private EditText name,phone,email,address;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private CircleImageView imageTv;
    private Button update,cancel;
    private Uri filePath;
    private ImageView select;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String nameDb,emailDb,phoneDb,addressDb,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        overridePendingTransition(R.anim.anim_up,R.anim.anim_null);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        name=findViewById(R.id.pro_name);
        phone=findViewById(R.id.pro_phone);
        update=findViewById(R.id.btn_update);
        imageTv=findViewById(R.id.p_images);
        select=findViewById(R.id.selection1);
        cancel=findViewById(R.id.cancel);
        email=findViewById(R.id.pro_email);
        address=findViewById(R.id.pro_address);

        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                     nameDb=""+ds.child("name").getValue();
                     emailDb=""+ds.child("email").getValue();
                     phoneDb=""+ds.child("phone").getValue();
                     addressDb=""+ds.child("address").getValue();
                     image=""+ds.child("image").getValue();





                }
                name.setText(nameDb);
                email.setText(emailDb);
                phone.setText(phoneDb);
                address.setText(addressDb);

                Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.profile).into(imageTv);

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
                        .start(EditProfile.this);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
//                state="Not";

                                          final String addresss = address.getText().toString().trim();
                                          final String Name = name.getText().toString().trim();
                                          final String Phone = phone.getText().toString().trim();
//                final String UserName=username.getText().toString().trim();


                                          HashMap<String, Object> hashMap = new HashMap<>();
                                          hashMap.put("address", addresss);
                                          hashMap.put("name", Name);
                                          hashMap.put("phone", Phone);
                                          DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                          ref.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if (task.isSuccessful()) {
                                                      Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                      finish();
                                                  }
                                              }
                                          });
                                      }
                                  });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.anim_null,R.anim.anim_down);
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
                imageTv.setImageBitmap(bitmap);
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


                                Toast.makeText(EditProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_null,R.anim.anim_down);
    }
}
