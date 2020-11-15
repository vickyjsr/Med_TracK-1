package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medapp.models.Appointment;
import com.example.medapp.models.Doctor;
import com.example.medapp.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {
    private TextView appTo,appFrom,appTime,appDate,appDes;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRef,dRef,pRef;
    Patient patient;
    static String toAuthId,fromAuthId;
    static String to,from;
    Appointment appointment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        setUpAppView();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        patient=new Patient();
        appointment=new Appointment();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mRef=database.getReference("Patients").child(mAuth.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient=snapshot.getValue(Patient.class);
                from=patient.getName();
                fromAuthId=patient.getAuthId();
                //app starts
                to = getIntent().getExtras().getString("To");
                toAuthId=getIntent().getExtras().getString("ToAuthId");
                appTo.setText("Requested to: Dr "+to);
                appFrom.setText("Requested from: "+from);
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm:ss a");
                String curDate=simpleDateFormat.format(calendar.getTime());
                appDate.setText("Appointment date: "+curDate);
                String curTime=simpleTimeFormat.format(calendar.getTime());
                appTime.setText("Appointment time: "+curTime);

                String appId=getIntent().getExtras().getString("appId");

                appDes.setText("CAUSES : "+getIntent().getExtras().getString("Temp")+" | "+getIntent().getExtras().getString("Pulse")
                +" | "+getIntent().getExtras().getString("OxySat")+" | "+getIntent().getExtras().getString("ResRate")+" | "+
                        getIntent().getExtras().getString("BloodPressure")+" | "+getIntent().getExtras().getString("Symptoms")+" | "
                +getIntent().getExtras().getString("symptomDes")+" | "+getIntent().getExtras().getString("Call"));


                pRef=database.getReference("PatApp").child(fromAuthId).child(appId);
                dRef=database.getReference("DocApp").child(toAuthId).child(appId);

                appointment.setAppointmentDate(curDate);
                appointment.setAppointmentTime(curTime);
                appointment.setAppointmentStatus("Requested");
                appointment.setAppointmentId(appId);
                appointment.setFrom(from);
                appointment.setTo(to);
                appointment.setFromAuthId(fromAuthId);
                appointment.setToAuthId(toAuthId);

                dRef.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pRef.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(AppointmentActivity.this,"Prescription requested",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AppointmentActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AppointmentActivity.this,ListDoctors.class));
                                        finish();
                                    }
                                }
                            });

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(AppointmentActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AppointmentActivity.this,ListDoctors.class));
                            finish();
                        }
                    }
                });

                //app ends
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AppointmentActivity.this,ListDoctors.class));
                finish();
            }
        });


    }
    public void startAppointmentList(View view){
        Intent intent = new Intent(AppointmentActivity.this,ListAppointment.class);
        intent.putExtra("ToAuthId",toAuthId);
        startActivity(intent);
    }
    private void setUpAppView(){
        appTo=findViewById(R.id.app_to);
        appFrom=findViewById(R.id.app_from);
        appTime=findViewById(R.id.cur_time);
        appDate=findViewById(R.id.cur_date);
        appDes=findViewById(R.id.des);
    }
}