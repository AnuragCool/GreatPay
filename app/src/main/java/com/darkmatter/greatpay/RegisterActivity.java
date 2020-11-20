package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private String state="notFound";
    private EditText name,email,mobile,pass,cnf_pass,username1;
    private CheckBox privacy;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name=findViewById(R.id.reg_name);
        email=findViewById(R.id.reg_email);
        mobile=findViewById(R.id.reg_phone);
        username1=findViewById(R.id.username);
        pass=findViewById(R.id.reg_password);
        cnf_pass=findViewById(R.id.reg_cnf_password);
        register=findViewById(R.id.register_button);
        progressBar=findViewById(R.id.register_progressBar);

        mAuth=FirebaseAuth.getInstance();


        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        //Add Validation for Name


        awesomeValidation.addValidation(this,R.id.username,
                RegexTemplate.NOT_EMPTY,R.string.invalid_username);

        awesomeValidation.addValidation(this,R.id.reg_name,
                RegexTemplate.NOT_EMPTY,R.string.invalid_name);

        //For Mobile Number
        awesomeValidation.addValidation(this,R.id.reg_phone,
                "[5-9]{1}[0-9]{9}$",R.string.invalid_mobile);


        //for Email
        awesomeValidation.addValidation(this,R.id.reg_email,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);

        //for username


        //for password
        awesomeValidation.addValidation(this,R.id.reg_password,
                ".{8,}",R.string.invalid_password);

        //for confirm password
        awesomeValidation.addValidation(this,R.id.reg_cnf_password,
                R.id.reg_password,R.string.invalid_cpassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                progressBar.setVisibility(View.VISIBLE);



                    final String Name = name.getText().toString();
                    final String Email = email.getText().toString();
                    final String phone = mobile.getText().toString();
                    final String password = pass.getText().toString();
                    final String user_name = username1.getText().toString().toLowerCase();
                    final String cnf_password = cnf_pass.getText().toString();



                //check Validation
                if (awesomeValidation.validate()) {

                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(checkUsername(user_name,dataSnapshot)){
                                username1.setError("Username Exists.Try Another");
                                username1.setFocusable(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
                                mAuth.createUserWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            user=mAuth.getCurrentUser();
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(RegisterActivity.this, "Registered Succesfully.Please verify your email.", Toast.LENGTH_SHORT).show();


//                                                             final DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
//
//                                                             Query query=reference.child("Users").orderByChild("username").equalTo(user_name);
//                                                             query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                 @Override
//                                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                                         String username = "" + ds.child("username").getValue();
//
//                                                                         if(username.equals(user_name)) {
//                                                                             state = "found";
//                                                                             Log.d("found1", "onDataChange: "+state);
//                                                                         }

//                                                                             if(state.equals("found")){
//                                                                                 username1.setError("Username Exists!!");
//                                                                                 username1.setFocusable(true);
//                                                                                 Log.d("found1", "onDataChange:found "+state);
//                                                                             }else {
                                                        Log.d("found1", "onDataChange:not found "+state);
                                                        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                                                        HashMap<String,Object> hashMap =new HashMap<>();
                                                        hashMap.put("name",Name);
                                                        hashMap.put("email",Email);
                                                        hashMap.put("username",user_name);
                                                        hashMap.put("phone",phone);
                                                        hashMap.put("uId",user.getUid());
                                                        databaseReference.child(user.getUid()).setValue(hashMap);

                                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                        finish();
                                                    }

//                                                                     }
                                                }

//                                                                 @Override
//                                                                 public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                 }
//                                                             });


//                                                         }
//                                                     }
                                            });


                                        }else{
                                            String exception=""+task.getException();
                                            String modifiedExe=exception.replace("com.google.firebase.auth.FirebaseAuthUserCollisionException:","");
                                            Toast.makeText(RegisterActivity.this, modifiedExe, Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


//

                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    public boolean checkUsername(String username, DataSnapshot dataSnapshot){
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            String user=""+ds.child("username").getValue();
            Log.d("name", "checkUsername: "+user);
            if(user.equals(username)){
                return true;
            }
        }
        return  false;
    }

}
