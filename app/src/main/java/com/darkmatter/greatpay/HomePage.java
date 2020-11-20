package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daasuu.cat.CountAnimationTextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity  {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    CountAnimationTextView given,collected;
    DatabaseReference ref;
    CircleImageView imageView;
    FloatingActionButton floatingActionButton;
    String my_Uid;
    NotificationBadge notificationBadge;
    private Button invite;
    static int count;
    int given_amount,collec_amount;
    private AdView mAdView;

    private LinearLayout my_loan, profile, settings, summary,collection,notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);




        notificationBadge=findViewById(R.id.badge);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        given=findViewById(R.id.total_given);
        collected=findViewById(R.id.total_collected);
        invite=findViewById(R.id.invite);

        imageView=findViewById(R.id.profile1);
        notification=findViewById(R.id.card3);
        summary=findViewById(R.id.card1);
        floatingActionButton=findViewById(R.id.floataction);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        checkUser();

        if(user!=null){
            loadMoneyCounter();
        }



        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check this Money Tracker at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });











        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
            }
        });
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SummaryActivity.class));
            }
        });

        settings=findViewById(R.id.card6);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        collection=findViewById(R.id.card4);
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CollectionActivity.class));
            }
        });

      floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              user = mAuth.getCurrentUser();

              MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(HomePage.this);
              builder.setMessage("Are you sure want to Logout?");
              builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      user = mAuth.getCurrentUser();
                      mAuth.signOut();
                      checkUser();





                  }
              });
              builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomePage.this, new OnSuccessListener<InstanceIdResult>() {
                          @Override
                          public void onSuccess(InstanceIdResult instanceIdResult) {
                              String token = instanceIdResult.getToken();
                              Log.i("FCM Token", token);
                              updateToken(token);
                          }
                      });
                  }
              });
              builder.show();
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
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomePage.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    Log.d("token", token+" working");
                    updateToken(token);
                }
            });

            loadProfile();




////            my_Uid=user.getUid();
            ref= FirebaseDatabase.getInstance().getReference("Users");
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

        profile = findViewById(R.id.card2);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this, MyProfile.class);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    Bundle bundle=ActivityOptions.makeSceneTransitionAnimation(HomePage.this,imageView,"profileImage").toBundle();
                    startActivity(intent,bundle);
                }else {
                    startActivity(intent);
                }

            }
        });

        settings = findViewById(R.id.card6);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, SettingsActivity.class));
            }
        });


    }

    private void loadMoneyCounter() {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
        databaseReference.child("loan").orderByChild("status").equalTo("paid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                given_amount = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("openth", "onDataChange: " +ds.child("amount").getValue());
                        if(!(""+ds.child("amount").getValue()).equals("null")) {
                            given_amount += Integer.parseInt(("" + ds.child("amount").getValue()));
                        }

                    }

                    given.setAnimationDuration(2000).countAnimation(0, given_amount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
        databaseReference1.child("collection").orderByChild("status").equalTo("collected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collec_amount = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if(!(""+ds.child("amount").getValue()).equals("null")) {
                            collec_amount += Integer.valueOf("" + ds.child("amount").getValue());
                        }

                    }

                    collected.setAnimationDuration(2000).countAnimation(0, collec_amount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkNotification() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification");
        reference.orderByChild("status").equalTo("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count=0;


                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Log.d("ds", "onDataChange: "+ds);
                String status=""+ds.child("status").getValue();
                   if(status.equals("")){
                       count++;
                   }

                }

                Log.d("noti", "checkNotification: "+count);
                notificationBadge.setNumber(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",user.getUid());
            editor.apply();

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
    private void loadProfile() {
        ref= FirebaseDatabase.getInstance().getReference("Users");

        ref.orderByChild("uId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for(DataSnapshot ds2: dataSnapshot1.getChildren()){

                    String url=""+ds2.child("image").getValue();


                    Log.d("url", "onDataChange: "+url);

                        Glide.with(getApplicationContext()).load(url).placeholder(R.drawable.profile1).into(imageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateToken(String s) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("token",s);
        reference.child(user.getUid()).setValue(hashMap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkNotification();
    }

}


