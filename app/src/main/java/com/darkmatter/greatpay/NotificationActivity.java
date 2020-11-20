package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationModel notificationModel;
    private List<NotificationModel> notificationModelList;
    private NotificationAdapter notificationAdapter;
    private FirebaseUser user;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_null);

        getSupportActionBar().setTitle("Notifications");

        user= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView= findViewById(R.id.notifi_recycler);
        layout=findViewById(R.id.no_notification_layout);

        notificationModelList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        loadNotification(layout,recyclerView);


    }

    private void loadNotification(final LinearLayout layout, final RecyclerView recyclerView) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationModelList.clear();
                    Log.d("notifi", "onDataChange: Yes");

                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String type=""+ds.child("type").getValue();
                        String name=""+ds.child("name").getValue();
                        String amount=""+ds.child("amount").getValue();
                        String sUid=""+ds.child("sUid").getValue();
                        String key=""+ds.child("commonKey").getValue();
                        String status=""+ds.child("status").getValue();
                        String myname=""+ds.child("myname").getValue();
                        String time=""+ds.child("time").getValue();
                        String purpose=""+ds.child("purpose").getValue();
                        String image=""+ds.child("image").getValue();
                        String uniqueKey=""+ds.child("uniqueKey").getValue();




                        if(!status.equals("responded")){
                            notificationModel=new NotificationModel(type,name,amount,sUid,key,myname,time,purpose,image,uniqueKey,status);
                            notificationModelList.add(notificationModel);
                        }

                    }

                    if(notificationModelList.size()==0){
                        recyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    }else{
                        layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        notificationAdapter=new NotificationAdapter(NotificationActivity.this,notificationModelList);
                        recyclerView.setAdapter(notificationAdapter);
                    }


                }

//            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
