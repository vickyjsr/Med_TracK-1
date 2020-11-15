package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medapp.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrescriptionSent extends AppCompatActivity {

    FirebaseDatabase database;
    static DatabaseReference pRef,dRef;
    FirebaseAuth mAuth;
    private static Appointment appointment,newApp;
    private Button sendPresFinal;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_sent);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        appointment=new Appointment();
        newApp= new Appointment();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        sendPresFinal=findViewById(R.id.send_pres_finally);

        String appId = getIntent().getExtras().getString("appId");

        pRef=database.getReference("PatApp").child(getIntent().getExtras().getString("patId")).child(appId);
        dRef=database.getReference("DocApp").child(mAuth.getUid()).child(appId);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointment = snapshot.getValue(Appointment.class);

                newApp.setAppointmentStatus("Prescribed");
                newApp.setTo(appointment.getTo());
                newApp.setFrom(appointment.getFrom());
                newApp.setToAuthId(appointment.getToAuthId());
                newApp.setFromAuthId(appointment.getFromAuthId());
                newApp.setAppointmentId(appointment.getAppointmentId());
                newApp.setAppointmentTime(appointment.getAppointmentTime());
                newApp.setAppointmentDate(appointment.getAppointmentDate());

                pRef.setValue(newApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dRef.setValue(newApp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(PrescriptionSent.this,"Prescription sent",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(PrescriptionSent.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(PrescriptionSent.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(PrescriptionSent.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void goToDocHome(View v){
        startActivity(new Intent(PrescriptionSent.this,DoctorHome.class));
        finish();
    }
}