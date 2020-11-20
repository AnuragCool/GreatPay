package com.darkmatter.greatpay;

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
import com.android.volley.Request;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddLoanActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String url,url1,namee,name2,email;
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

        String timestamp = String.valueOf(System.currentTimeMillis());
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        final String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();




        final DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Users");
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
                final String pur=purpose.getText().toString().trim();
                final String luser=Lusername.getText().toString().trim().toLowerCase();
                final String amounts=amount.getText().toString().trim();
                if(luser.isEmpty()){
                    Lusername.setError("Enter Recipient username");
                    Lusername.setFocusable(true);

                }else if(amounts.isEmpty()){
                    amount.setError("Enter the amount");
                    amount.setFocusable(true);

                }
                else{
                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

                    Query query=reference.child("Users").orderByChild("username").equalTo(luser);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String username = "" + ds.child("username").getValue();
                                Log.d("name", "checkRuserName: " + username);
                                if (luser.equals(username)) {
                                    url=""+ds.child("image").getValue();
                                    name2=""+ds.child("name").getValue();
                                    pUid=""+ds.child("uId").getValue();
                                    email=""+ds.child("email").getValue();
                                    state="found";
                                    if(state.equals("found")){

                                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);

                                        String key=databaseReference.child("loan").push().getKey();
                                        HashMap<String,Object> hashMap1=new HashMap<>();
                                        hashMap1.put("amount",amounts);
                                        hashMap1.put("rname",namee);
                                        hashMap1.put("remail",user.getEmail());
                                        hashMap1.put("image",url1);
                                        hashMap1.put("commonkey",key);
                                        hashMap1.put("bUid",user.getUid());
                                        hashMap1.put("key",key);
                                        hashMap1.put("purpose",pur);
                                        hashMap1.put("status","");
                                        hashMap1.put("myname",name2);
                                        databaseReference.child("collection").child(key).setValue(hashMap1);


                                        Log.d("got", "onDataChange: executed");

                                        DatabaseReference reference1 =FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                                        HashMap<String,Object> hashMap =new HashMap<>();
                                        hashMap.put("rUser",email);
                                        hashMap.put("name",name2);
                                        hashMap.put("amount",amounts);
                                        hashMap.put("image",url);
                                        hashMap.put("purpose",pur);
                                        hashMap.put("pUid",pUid);
                                        hashMap.put("myname",namee);
                                        hashMap.put("commonkey",key);
                                        hashMap.put("status","verified");
                                        reference1.child("loan").child(key).setValue(hashMap);



                                        HashMap<String,Object> hashMap2=new HashMap<>();
                                        String uniqueKey=databaseReference.child("notification").push().getKey();
                                        hashMap2.put("commonKey",key);
                                        hashMap2.put("name",namee);
                                        hashMap2.put("sUid",user.getUid());
                                        hashMap2.put("amount",amounts);
                                        hashMap2.put("status","");
                                        hashMap2.put("myname",name2);
                                        hashMap2.put("time",pTime);
                                        hashMap2.put("image",url);
                                        hashMap2.put("purpose",pur);
                                        hashMap2.put("type","addedCollection");
                                        hashMap2.put("uniqueKey",uniqueKey);


                                        databaseReference.child(uniqueKey).setValue(hashMap2);

                                        sendNotification(pUid);
                                        finish();
                                    }
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

                    Log.d("sec", "onClick: after st=found");

                }
            }
        });
    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void sendNotification(final String hisUid) {

        final RequestQueue requestQueue;

        requestQueue= Volley.newRequestQueue(AddLoanActivity.this);

        DatabaseReference allToken=FirebaseDatabase.getInstance().getReference("Tokens");

        allToken.orderByKey().equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Token token=ds.getValue(Token.class);
                    Data data;
                        data =new Data(user.getUid(),"New Collection amount added from "+namee,"New Collection Amount",hisUid);



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

}
