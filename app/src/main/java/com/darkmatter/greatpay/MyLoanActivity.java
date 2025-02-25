package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

public class MyLoanActivity extends AppCompatActivity {

    private FloatingActionMenu menu;
    private FloatingActionButton f_btn;
    private LoanModel loanModel;
    private List<LoanModel> loanModels;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    String mail,ruser,namee,url1;
    String url;
    private FirebaseUser user;
    private LoanAdapter loanAdapter;
    private Button edit_btn;
    private AdView mAdView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_loan);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_null);


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        linearLayout=findViewById(R.id.no_loan_activity);
        recyclerView=findViewById(R.id.recycler_loan);



        getSupportActionBar().setTitle("My Loans");
        loanModels=new ArrayList<>();



        f_btn=findViewById(R.id.f_add);


        f_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddLoanActivity.class));
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        load();


    }

    private void load() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());

        ref.child("loan").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loanModels.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String status = "" + ds.child("status").getValue();
                        if (!status.equals("paid") && !status.equals("")) {
                            loanModel = ds.getValue(LoanModel.class);
                            loanModels.add(loanModel);
                        }


                    }


                }
                if(loanModels.size()==0){
                    linearLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    linearLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loanAdapter = new LoanAdapter(MyLoanActivity.this, loanModels);
                    recyclerView.setAdapter(loanAdapter);
                    loanAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("on", "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("on", "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("on", "onPause: ");
    }

}
