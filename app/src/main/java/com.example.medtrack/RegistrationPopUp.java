package com.example.medapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class RegistrationPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pop_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.3));
    }
    public void startDoctorRegistration(View v){
       // Toast.makeText(RegistrationPopUp.this,"Not Allowed!",Toast.LENGTH_SHORT).show();
       startActivity(new Intent(RegistrationPopUp.this,DoctorsRegistrationActivity.class));
    }
    public void startPatientRegistration(View v){
        startActivity(new Intent(RegistrationPopUp.this,PatientRegistration.class));
    }
}