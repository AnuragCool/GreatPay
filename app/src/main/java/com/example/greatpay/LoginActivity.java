package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView register , forgot_pass;
    private EditText email,password;
    private Button login_btn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register=findViewById(R.id.register);
        forgot_pass=findViewById(R.id.forgot_password);
        progressBar=findViewById(R.id.login_progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);

        firebaseAuth=FirebaseAuth.getInstance();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                progressBar.setVisibility(View.VISIBLE);
                String Email= email.getText().toString();
                String Password= password.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Password.length()<8){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                }



                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                                    finish();
                                }
                                else {

                                    Toast.makeText(getApplicationContext(),"Login failed or User not available",Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
            }
        });
    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

}
