package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddLoanActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String url,url1,namee,name2;
    String state ="not found";
    String pUid;
    private EditText Lusername,amount,purpose;
    private Button add,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        Lusername=findViewById(R.id.luser);
        amount=findViewById(R.id.loanam);
        add=findViewById(R.id.save);
        cancel=findViewById(R.id.cancel);
        purpose=findViewById(R.id.loan_pur);




        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Users");
        ref1.orderByChild("uId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for(DataSnapshot ds2: dataSnapshot1.getChildren()){
                    namee=""+ds2.child("name").getValue();
                    url1=""+ds2.child("image").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pur=purpose.getText().toString().trim();
                final String luser=Lusername.getText().toString().trim();
                final String amounts=amount.getText().toString().trim();
                if(luser.isEmpty()){
                    Lusername.setError("Enter Recipient username");
                    Lusername.setFocusable(true);

                }else if(amounts.isEmpty()){
                    amount.setError("Enter the amount");
                    amount.setFocusable(true);

                }
                else{
                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String username = "" + ds.child("email").getValue();
                                Log.d("name", "checkRuserName: " + username);
                                if (luser.equals(username)) {
                                    url=""+ds.child("image").getValue();
                                    name2=""+ds.child("name").getValue();
                                    pUid=""+ds.child("uId").getValue();
                                    state="found";
                                    break;
                                }
                            }
                            if(!state.equals("found")){
                                Lusername.setError("User don't Exists!");
                                Lusername.setFocusable(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    if(state.equals("found")){
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put("amount",amounts);
                        hashMap1.put("rname",namee);
                        hashMap1.put("remail",user.getEmail());
                        hashMap1.put("image",url1);
                        hashMap1.put("pUid",pUid);
                        databaseReference.child("collection").push().setValue(hashMap1);


                        Log.d("got", "onDataChange: executed");

                        DatabaseReference reference1 =FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                        HashMap<String,Object> hashMap =new HashMap<>();
                        hashMap.put("rUser",luser);
                        hashMap.put("name",name2);
                        hashMap.put("amount",amounts);
                        hashMap.put("image",url);
                        hashMap.put("purpose",pur);
                        reference1.child("loan").push().setValue(hashMap);
                        finish();
                    }
                }
            }
        });
    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

}
