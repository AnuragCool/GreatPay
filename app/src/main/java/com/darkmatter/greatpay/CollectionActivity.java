package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CollectionAdapter collectionAdapter;
    private CollectionModel collectionModel;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FloatingActionButton floatingActionButton;
    String hisUid,amount;
    String mail,ruser,url;
    private List<CollectionModel> collectionModelList;
    private AdView mAdView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_null);
        floatingActionButton=findViewById(R.id.c_add);





        getSupportActionBar().setTitle("My Collections");


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();


        recyclerView=findViewById(R.id.recycler_collection);
        linearLayout=findViewById(R.id.no_collection_layout);
        collectionModelList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CollectionActivity.this,AddCollectionActivity.class));
            }
        });


        load();
    }

    private void load() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());

        ref.child("collection").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectionModelList.clear();

                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String status = "" + ds.child("status").getValue();
                        if (!status.equals("collected") && !status.equals("notVerified")) {
                            String amount = "" + ds.child("amount").getValue();
                            String bUid = "" + ds.child("bUid").getValue();
                            String image = "" + ds.child("image").getValue();
                            String myname = "" + ds.child("myname").getValue();
                            String purpose = "" + ds.child("purpose").getValue();
                            String remail = "" + ds.child("remail").getValue();
                            String rname = "" + ds.child("rname").getValue();
                            String time = "" + ds.child("time").getValue();
                            String key = "" + ds.child("key").getValue();
                            String remind = "" + ds.child("remind").getValue();
                            Log.d("testingg", "onDataChange: " + remail);
                            collectionModel = new CollectionModel(rname, remail, amount, image, key, bUid, status, myname, purpose, remind);


                            Log.d("working", "onDataChange: " + collectionModel.getAmount());
                            collectionModelList.add(collectionModel);

                        }


                    }

                }
                if(collectionModelList.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    collectionAdapter=new CollectionAdapter(CollectionActivity.this,collectionModelList);
                    recyclerView.setAdapter(collectionAdapter);
                    collectionAdapter.notifyDataSetChanged();
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
