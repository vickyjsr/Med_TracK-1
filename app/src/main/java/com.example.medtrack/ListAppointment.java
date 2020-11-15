package com.example.medapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medapp.adapters.AppointmentAdapter;
import com.example.medapp.adapters.DoctorAdapter;
import com.example.medapp.models.Appointment;
import com.example.medapp.models.Doctor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ListAppointment extends AppCompatActivity {

    RecyclerView recyclerView;
    AppointmentAdapter appointmentAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointment);
        mAuth = FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.list_of_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Appointment> options=
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("PatApp").child(mAuth.getUid()), Appointment.class)
                        .build();
        appointmentAdapter=new AppointmentAdapter(options,this);
        recyclerView.setAdapter(appointmentAdapter);
    }
    @Override
    protected void onStart(){
        super.onStart();
        appointmentAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        appointmentAdapter.stopListening();
    }
}