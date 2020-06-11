package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.webkit.WebView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setTitle("Notifications");

        user= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView= findViewById(R.id.notifi_recycler);

        notificationModelList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        loadNotification();


    }

    private void loadNotification() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationModelList.clear();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String type=""+ds.child("type").getValue();
                    String name=""+ds.child("name").getValue();
                    String amount=""+ds.child("amount").getValue();
                    String sUid=""+ds.child("sUid").getValue();
                    String key=""+ds.child("key").getValue();
                    String status=""+ds.child("status").getValue();
                    String myname=""+ds.child("myname").getValue();
                    String time=""+ds.child("time").getValue();



                    if(!status.equals("responded")){
                        notificationModel=new NotificationModel(type,name,amount,sUid,key,myname,time);
                        notificationModelList.add(notificationModel);
                    }

                }
                notificationAdapter=new NotificationAdapter(NotificationActivity.this,notificationModelList);
                recyclerView.setAdapter(notificationAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
