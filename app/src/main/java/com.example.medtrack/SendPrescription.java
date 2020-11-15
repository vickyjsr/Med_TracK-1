package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medapp.models.Appointment;
import com.example.medapp.models.Prescription;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendPrescription extends AppCompatActivity {

    private EditText sendPresMedicines,sendPresTests,sendPresVideoCallLink,sendPresCallTime,sendPresPrecautions,sendPresDescription;
    private Button sendPresBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    static DatabaseReference reference;
    Prescription prescription;
    ProgressDialog progressDialog;
    static DatabaseReference pRef,dRef;
    private static Appointment appointment,newApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_prescription);
        setUpSendPresView();
        mAuth =FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        prescription = new Prescription();
        appointment = new Appointment();
        newApp = new Appointment();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Sending...");

        sendPresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String medicines= sendPresMedicines.getText().toString().trim();
                String tests  = sendPresTests.getText().toString().trim();
                String videoCallLink = sendPresVideoCallLink.getText().toString().trim();
                String callTime = sendPresCallTime.getText().toString().trim();
                String precautions= sendPresPrecautions.getText().toString().trim();
                String descriptions = sendPresDescription.getText().toString().trim();

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm:ss a");
                String curDate=simpleDateFormat.format(calendar.getTime());
                String curTime=simpleTimeFormat.format(calendar.getTime());

                if(medicines.isEmpty()&&tests.isEmpty()&&videoCallLink.isEmpty()&&callTime.isEmpty()&&precautions.isEmpty()&&descriptions.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Can't send empty prescription!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    final String appId = getIntent().getExtras().getString("appId");
                    prescription.setPrescriptionTimeAndDate(curTime+" "+curDate);
                    prescription.setMedicines(medicines);
                    prescription.setTests(tests);
                    prescription.setVideoCallLink(videoCallLink);
                    prescription.setCallTime(callTime);
                    prescription.setPrecautions(precautions);
                    prescription.setOtherDescription(descriptions);

                    reference=database.getReference("Prescriptions").child(appId);
                    reference.setValue(prescription).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
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
                                                                Toast.makeText(SendPrescription.this,"Prescription sent",Toast.LENGTH_SHORT).show();
                                                                // startActivity(new Intent(SendPrescription.this,DoctorHome.class));
                                                                finish();
                                                            }
                                                            else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(SendPrescription.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SendPrescription.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SendPrescription.this,"Error in sending Prescription!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                /*progressDialog.dismiss();
                                Intent intent = new Intent(SendPrescription.this,PrescriptionSent.class);
                                intent.putExtra("appId",getIntent().getExtras().getString("appId"));
                                intent.putExtra("patId",getIntent().getExtras().getString("patId"));
                                startActivity(intent);
                                finish();*/
                               // Toast.makeText(SendPrescription.this,"Prescription sent",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SendPrescription.this,"Error in sending prescription!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SendPrescription.this,DoctorHome.class));
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }
    private void setUpSendPresView(){
        sendPresBtn=findViewById(R.id.send_pres_btn);
        sendPresMedicines=findViewById(R.id.send_pres_medicines);
        sendPresTests=findViewById(R.id.send_pres_tests);
        sendPresVideoCallLink=findViewById(R.id.send_pres_call_link);
        sendPresCallTime=findViewById(R.id.send_pres_call_time);
        sendPresPrecautions=findViewById(R.id.send_pres_precautions);
        sendPresDescription=findViewById(R.id.send_pres_description);
    }
}