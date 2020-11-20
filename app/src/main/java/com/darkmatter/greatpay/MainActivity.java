package com.darkmatter.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    double VERSION;
    double _version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Thread thread=new Thread(){
//            @Override
//            public void run() {
//                try{
                    try {
                        PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0);
                        final String version =packageInfo.versionName;
                        _version=Double.parseDouble(version);
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Version");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        VERSION = Double.parseDouble((""+dataSnapshot.child("version").getValue()));
                                        Log.d("values", "onDataChange: "+VERSION+" "+_version);
                                        if (VERSION >_version) {
                                            Toast.makeText(MainActivity.this, "Update to newest version to Continue", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }catch (PackageManager.NameNotFoundException e){
                        e.printStackTrace();

                    }


//                }
//                catch (Exception ex){}
//                super.run();
//            }
//        };
//        thread.start();
    }
}
