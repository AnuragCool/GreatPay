package com.example.greatpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                    Intent intent= new Intent(getApplicationContext(),HomePage.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception ex){}
                super.run();
            }
        };
        thread.start();
    }
}
