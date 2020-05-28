package com.example.greatpay;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username,name,email,mobile,pass,cnf_pass,gender;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2;
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

        username=findViewById(R.id.reg_username);
        name=findViewById(R.id.reg_name);
        email=findViewById(R.id.reg_email);
        mobile=findViewById(R.id.reg_phone);
        pass=findViewById(R.id.reg_password);
        cnf_pass=findViewById(R.id.reg_cnf_password);
        register=findViewById(R.id.register_button);
        radioGroup=findViewById(R.id.radio_grp1);

        radioButton1=findViewById(R.id.reg_male);
        radioButton2=findViewById(R.id.reg_female);
        progressBar=findViewById(R.id.register_progressBar);

        mAuth=FirebaseAuth.getInstance();

        privacy=findViewById(R.id.checkBox);

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        //Add Validation for Name
        awesomeValidation.addValidation(this,R.id.reg_name,
                RegexTemplate.NOT_EMPTY,R.string.invalid_name);

        //For Mobile Number
        awesomeValidation.addValidation(this,R.id.reg_phone,
                "[5-9]{1}[0-9]{9}$",R.string.invalid_mobile);


        //for Email
        awesomeValidation.addValidation(this,R.id.reg_email,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);

        //for username
        awesomeValidation.addValidation(this,R.id.reg_username,
        RegexTemplate.NOT_EMPTY,R.string.invalid_username);

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

                    String gender = "";

                    if (radioButton1.isChecked()) {
                        gender = "Male";
                    } else {
                        gender = "Female";
                    }

                    String checkbox="";
                    if(privacy.isChecked()){
                        checkbox="true";
                    }
                    else {
                        checkbox = "false";
                    }
                    final String Name = name.getText().toString();
                    final String Email = email.getText().toString();
                    final String UserName = username.getText().toString();
                    final String phone = mobile.getText().toString();
                    final String password = pass.getText().toString();
                    final String cnf_password = cnf_pass.getText().toString();



                //check Validation
                if (awesomeValidation.validate()) {
                    if(!radioButton1.isChecked() && !radioButton2.isChecked()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Select gender",Toast.LENGTH_SHORT).show();
                    }
                    else if(!privacy.isChecked())
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"not checked privacy",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final String finalGender = gender;
                        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 if(checkRuserName(UserName,dataSnapshot)){
                                     username.setError("Username Exists! Try Another");
                                     username.setFocusable(true);
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

                                                             databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                                                             HashMap<String,Object> hashMap =new HashMap<>();
                                                             hashMap.put("name",Name);
                                                             hashMap.put("email",Email);
                                                             hashMap.put("phone",phone);
                                                             hashMap.put("username",UserName);
                                                             hashMap.put("gender", finalGender);
                                                             hashMap.put("uId",user.getUid());
                                                             databaseReference.child(user.getUid()).setValue(hashMap);

                                                             startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                             finish();
                                                         }
                                                     }
                                                 });


                                             }else{
                                                 Toast.makeText(RegisterActivity.this, "Not registered Successfully", Toast.LENGTH_SHORT).show();

                                             }

                                         }
                                     });
                                 }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //on Success

                    }
//
//                startActivity(new Intent(getApplicationContext(),HomePage.class));
//                finish();
                }
            }
        });

    }
    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
    private boolean checkRuserName(final String ruser, DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String username = "" + ds.child("username").getValue();
            Log.d("name", "checkRuserName: "+username);
            if (ruser.equals(username)) {
                return true;

            }


        }
        return  false;
    }

}
