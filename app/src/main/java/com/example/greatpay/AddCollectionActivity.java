package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCollectionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String url,url1,namee,name2;
    String state ="not found";
    String pUid;
    String amounts, pur;
    String TAG="race";
    private Sender sender;
    private RequestQueue requestQueue;
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


        String timestamp = String.valueOf(System.currentTimeMillis());
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        final String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();


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
                pur=C_purpose.getText().toString().trim();
                final String ruser=C_username.getText().toString().trim();
                amounts=C_amount.getText().toString().trim();
                if(ruser.isEmpty()){
                    C_username.setError("Enter Recipient username");
                    C_username.setFocusable(true);

                }else if(amounts.isEmpty()){
                    C_amount.setError("Enter the amount");
                    C_amount.setFocusable(true);

                }
                else{
                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                    reference.orderByChild("email").equalTo(ruser).addValueEventListener(new ValueEventListener() {
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
                                Log.d(TAG, "onDataChange: 2");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    if(state.equals("found") ){
                        Log.d(TAG, "onDataChange: 3");
                        DatabaseReference reference1 =FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);
                        String key=reference1.child("collection").push().getKey();
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put("amount",amounts);
                        hashMap1.put("name",namee);
                        hashMap1.put("rUser",user.getEmail());
                        hashMap1.put("image",url1);
                        hashMap1.put("pUid",user.getUid());
                        hashMap1.put("myname",name2);
                        hashMap1.put("key",databaseReference.child("loan").push().getKey());
                        hashMap1.put("commonkey",key);
                        hashMap1.put("status","");
                        hashMap1.put("purpose",pur);




                        databaseReference.child("loan").child(key).setValue(hashMap1);

                        HashMap<String,Object> hashMap2=new HashMap<>();
                        hashMap2.put("key",key);
                        hashMap2.put("name",namee);
                        hashMap2.put("sUid",user.getUid());
                        hashMap2.put("amount",amounts);
                        hashMap2.put("status","");
                        hashMap2.put("myname",name2);
                        hashMap2.put("time",pTime);
                        hashMap2.put("purpose",pur);
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
                        hashMap.put("status","");
                        hashMap.put("myname",namee);
                        hashMap.put("commonkey",key);
                        reference1.child("collection").child(key).setValue(hashMap);


                        sendNotification(pUid);





                        finish();
                    }
                }
            }
        });
    }

    private void sendNotification(final String hisUid) {

        requestQueue= Volley.newRequestQueue(AddCollectionActivity.this);

        DatabaseReference allToken=FirebaseDatabase.getInstance().getReference("Tokens");
        allToken.orderByKey().equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Token token=ds.getValue(Token.class);
                    Data data;
                    if(pur==""){
                         data =new Data(user.getUid(),"Have you taken ₹ "+amounts+" from "+namee+" ?","Loan Verification",hisUid);
                    }
                    else{
                        data =new Data(user.getUid(),"Have you taken ₹ "+amounts+" from "+namee+" for "+pur+" ?","Loan Verification",hisUid);
                    }


                    Sender sender=new Sender(data,token.getToken());


                    try {
                        JSONObject SenderjsonObject=new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", SenderjsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("responce", "onResponse: "+response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> headers=new HashMap<>();
                                headers.put("Content-Type","application/json");
                                headers.put("Authorization","key=AAAAjL1k34w:APA91bEt-WyQg0VgQEXAeTjFe1OCJaQGZRIB_kXMu92eAaWwDage6jiHjRn29GzUWl6CZd04c1oo8c-XaMx4fPTnFZ479V6YojxXRxBriGakY4MBbLAgaf9mnI8OtxDCT_QszCvqCR1S");
                                return headers;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

}
