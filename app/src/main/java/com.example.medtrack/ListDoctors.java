package com.example.medapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medapp.adapters.DoctorAdapter;
import com.example.medapp.models.Doctor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ListDoctors extends AppCompatActivity {

    RecyclerView recyclerView;
    DoctorAdapter doctorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_doctors);

        recyclerView=findViewById(R.id.list_of_doctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Doctor> options=
                new FirebaseRecyclerOptions.Builder<Doctor>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Doctors"), Doctor.class)
                        .build();
        doctorAdapter=new DoctorAdapter(options,this);
        recyclerView.setAdapter(doctorAdapter);
    }
    @Override
    protected void onStart(){
        super.onStart();
        doctorAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        doctorAdapter.stopListening();
    }
}