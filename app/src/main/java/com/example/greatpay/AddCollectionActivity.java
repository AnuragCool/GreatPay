package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddCollectionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String url,url1,namee,name2;
    String state ="not found";
    String pUid;
    private EditText C_username,C_amount,C_purpose;
    private Button add,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        C_username=findViewById(R.id.ruser_c);
        C_amount=findViewById(R.id.coll_amount);
        add=findViewById(R.id.c_save);
        cancel=findViewById(R.id.c_cancel);
        C_purpose=findViewById(R.id.coll_pur);


        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("Users");
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
                String pur=C_purpose.getText().toString().trim();
                final String ruser=C_username.getText().toString().trim();
                final String amounts=C_amount.getText().toString().trim();
                if(ruser.isEmpty()){
                    C_username.setError("Enter Recipient username");
                    C_username.setFocusable(true);

                }else if(amounts.isEmpty()){
                    C_amount.setError("Enter the amount");
                    C_amount.setFocusable(true);

                }
                else{
                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String username = "" + ds.child("email").getValue();
                                Log.d("name", "checkRuserName: " + username);
                                if (ruser.equals(username)) {
                                    url=""+ds.child("image").getValue();
                                    name2=""+ds.child("name").getValue();
                                    pUid=""+ds.child("uId").getValue();
                                    state="found";
                                    break;
                                }
                            }
                            if(!state.equals("found")){
                                C_username.setError("User don't Exists!");
                                C_username.setFocusable(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    if(state.equals("found")){
                        DatabaseReference reference1 =FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);
                        String key=reference1.child("collection").push().getKey();
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put("amount",amounts);
                        hashMap1.put("name",namee);
                        hashMap1.put("rUser",user.getEmail());
                        hashMap1.put("image",url1);
                        hashMap1.put("pUid",user.getUid());
                        hashMap1.put("key",databaseReference.child("loan").push().getKey());
                        hashMap1.put("commonkey",key);




                        databaseReference.child("loan").child(key).setValue(hashMap1);

                        HashMap<String,Object> hashMap2=new HashMap<>();
                        hashMap2.put("key",key);
                        hashMap2.put("name",namee);
                        hashMap2.put("nSenderUid",user.getUid());
                        hashMap2.put("amount",amounts);
                        hashMap2.put("type","loan");


                        databaseReference.child("notification").child(key).setValue(hashMap2);


                        Log.d("got", "onDataChange: executed");


                        HashMap<String,Object> hashMap =new HashMap<>();
                        hashMap.put("remail",ruser);
                        hashMap.put("rname",name2);
                        hashMap.put("amount",amounts);
                        hashMap.put("image",url);
                        hashMap.put("purpose",pur);
                        hashMap.put("key",key);
                        hashMap.put("bUid",pUid);
                        hashMap.put("commonkey",key);
                        reference1.child("collection").child(key).setValue(hashMap);





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
