package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medapp.models.Appointment;
import com.example.medapp.models.Cause;
import com.example.medapp.models.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CauseActivity extends AppCompatActivity {

    private TextView causeBodyTemp,causeOxySatLevel,causePulseRate,causeRespRate,causeBloodPressure,causeSymptoms,causeDescription,causeRequestedCall;
    FirebaseDatabase database;
    private Button causeSendPrescription;
    FirebaseAuth mAuth;
    DatabaseReference reference,mRef,dRef,pRef;
    Cause cause;
    Doctor doctor;
    private ProgressDialog progressDialog;
    private Appointment appointment,newApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cause);
        setUpCauseView();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        causeSendPrescription.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        cause = new Cause();
        doctor = new Doctor();
        appointment = new Appointment();
        newApp = new Appointment();

        reference=database.getReference("Causes").child(getIntent().getExtras().getString("appId"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cause = snapshot.getValue(Cause.class);
                causeBodyTemp.setText(cause.getBodyTemp());
                causeOxySatLevel.setText(cause.getOxygenSaturation());
                causePulseRate.setText(cause.getPulse());
                causeRespRate.setText(cause.getRespiratoryRate());
                causeBloodPressure.setText(cause.getBloodPressure());
                causeSymptoms.setText(cause.getSymptoms());
                causeDescription.setText(cause.getDescription());
                causeRequestedCall.setText(cause.getCallRequest());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(CauseActivity.this,"Error in loading data! Please refresh",Toast.LENGTH_SHORT).show();
            }
        });
        mRef=database.getReference("Doctors").child(mAuth.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor=snapshot.getValue(Doctor.class);
                if(doctor!=null){
                    String  appStatus = getIntent().getExtras().getString("appStatus");
                    if(appStatus.equals("Requested")){
                        causeSendPrescription.setVisibility(View.VISIBLE);
                        //following program was for viewed feature,
                        //but it is commented as there occurs a bug by using it.
                        /*String appId = getIntent().getExtras().getString("appId");
                        pRef=database.getReference("PatApp").child(getIntent().getExtras().getString("patId")).child(appId);
                        dRef=database.getReference("DocApp").child(getIntent().getExtras().getString("docId")).child(appId);

                        dRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                appointment = snapshot.getValue(Appointment.class);

                                newApp.setAppointmentStatus("Viewed");
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
                                                        Intent intent = new Intent(CauseActivity.this,CauseActivity.class);
                                                        intent.putExtra("appId",getIntent().getExtras().getString("appId"));
                                                        intent.putExtra("pat",getIntent().getExtras().getString("pat"));
                                                        intent.putExtra("patId",getIntent().getExtras().getString("patId"));
                                                        intent.putExtra("doc",getIntent().getExtras().getString("doc"));
                                                        intent.putExtra("docId",getIntent().getExtras().getString("docId"));
                                                        intent.putExtra("appStatus","Viewed");
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                    else {

                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(CauseActivity.this,"Error occurred!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(CauseActivity.this,"error",Toast.LENGTH_SHORT);
                            }
                        });*/
                    }
                    else if(appStatus.equals("Viewed")){
                        causeSendPrescription.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    Toast.makeText(CauseActivity.this,"no error",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CauseActivity.this,"no error",Toast.LENGTH_SHORT);
            }
        });

        causeSendPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CauseActivity.this,SendPrescription.class);
                intent.putExtra("appId",getIntent().getExtras().getString("appId"));
                intent.putExtra("patId",getIntent().getExtras().getString("patId"));
                startActivity(intent);
                finish();
            }
        });
    }
    private void setUpCauseView(){
        causeBodyTemp=findViewById(R.id.cause_body_temp);
        causeOxySatLevel=findViewById(R.id.cause_oxy_sat_level);
        causePulseRate=findViewById(R.id.cause_pulse_rate);
        causeRespRate=findViewById(R.id.cause_respiratory_rate);
        causeBloodPressure=findViewById(R.id.cause_blood_pressure);
        causeSymptoms=findViewById(R.id.cause_symptoms);
        causeDescription=findViewById(R.id.cause_description);
        causeRequestedCall=findViewById(R.id.cause_requested_call);
        causeSendPrescription=findViewById(R.id.cause_send_prescription);
    }
}