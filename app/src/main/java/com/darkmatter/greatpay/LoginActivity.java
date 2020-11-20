package com.darkmatter.greatpay;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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
            public void onClick(final View v) {
                hideKeyboard(v);
                progressBar.setVisibility(View.VISIBLE);
                String Email= email.getText().toString();
                String Password= password.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(v,"Please Enter Email",Snackbar.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(v,"Please Enter the password",Snackbar.LENGTH_LONG).show();
                    return;
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
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        Snackbar.make(v,"Weak Password",Snackbar.LENGTH_LONG).show();
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        Snackbar.make(v,"Wrong Password. Try Again!!",Snackbar.LENGTH_LONG).show();
                                    }catch (FirebaseAuthInvalidUserException e){
                                        Snackbar.make(v,"This account has not been registered",Snackbar.LENGTH_LONG).show();
                                    } catch(Exception e) {
                                    }

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
