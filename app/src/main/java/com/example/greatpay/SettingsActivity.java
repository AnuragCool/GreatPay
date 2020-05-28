package com.example.greatpay;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar=findViewById(R.id.draw_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Settings");

        if(findViewById(R.id.fragment_containers)!=null){

            if(savedInstanceState!=null)
                return;

            getFragmentManager().beginTransaction().add(R.id.fragment_containers, new SettingsFragment()).commit();
        }
    }
}
