package com.example.greatpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SummaryActivity extends AppCompatActivity  {

    private TextView dateText;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

       // dateText=findViewById(R.id.date_text);

        toolbar=findViewById(R.id.draw_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Summary");

//        //findViewById(R.id.show_Dialog).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//            }
//        });
//
    }
//    private void showDatePickerDialog(){
//
//        DatePickerDialog datePickerDialog= new DatePickerDialog(
//                this,
//                this,
//                Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//
//
//    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        String date= "month/day/year: " + month + "/" + dayOfMonth + "/" + year;
//        dateText.setText(date);
//    }
}
