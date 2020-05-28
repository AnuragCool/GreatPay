package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity  {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView em, na;
    DatabaseReference ref;
    FloatingActionButton floatingActionButton;
    String my_Uid;

    private CardView my_loan, profile, settings, summary;
    View hView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton=findViewById(R.id.signout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        checkUser();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = mAuth.getCurrentUser();
                mAuth.signOut();
                checkUser();

            }
        });


        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            collapsingToolbar.setCollapsedTitleTypeface(getResources().getFont(R.font.mont));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            collapsingToolbar.setExpandedTitleTypeface(getResources().getFont(R.font.paci));
        }
        //navigationView.setCheckedItem(R.id.reg_phone);


        if (user != null) {
////            my_Uid=user.getUid();
////            ref= FirebaseDatabase.getInstance().getReference("Users");
////            ref.orderByChild("uId").equalTo(my_Uid).addValueEventListener(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    for(DataSnapshot ds: dataSnapshot.getChildren()){
////                        String email=""+ds.child("email").getValue();
////                        String username=""+ds.child("username").getValue();
////                        Log.d("test", "onDataChange: "+email+username);
////                        em.setText(email);
////                        na.setText(username);
////                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }
        my_loan = findViewById(R.id.card5);

        my_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, MyLoanActivity.class));
            }
        });

        settings = findViewById(R.id.card3);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, SettingsActivity.class));
            }
        });

        profile = findViewById(R.id.card4);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, MyProfile.class));
            }
        });

        settings = findViewById(R.id.card6);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, SummaryActivity.class));
            }
        });


    }

    private void checkUser() {
        user = mAuth.getCurrentUser();
        if (user != null) {
            if (!user.isEmailVerified()) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                startActivity(new Intent(HomePage.this,AboutUsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}


