package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medapp.models.Doctor;
import com.example.medapp.models.Prescription;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrescriptionActivity extends AppCompatActivity {
    CardView callTime,callLink;
    TextView presMedicines,presTests,presPrecautions,presDescription,presCallTime,presVideoCallLink,prescriptionText;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    DatabaseReference reference,mRef;
    Prescription prescription;
    Doctor doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        setUpPresView();
        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        prescription = new Prescription();
        doctor = new Doctor();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading... Please wait");
        progressDialog.show();

        String appId = getIntent().getExtras().getString("appId");

            reference=database.getReference("Prescriptions").child(appId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    prescription = snapshot.getValue(Prescription.class);

                    if(prescription.getVideoCallLink().isEmpty())
                        callLink.setVisibility(View.GONE);
                    else
                        presVideoCallLink.setText(prescription.getVideoCallLink());
                    if(prescription.getCallTime().isEmpty())
                        callTime.setVisibility(View.GONE);
                    else
                        presCallTime.setText(prescription.getCallTime());
                    if(prescription.getMedicines().isEmpty()==false)
                        presMedicines.setText(prescription.getMedicines());
                    if(prescription.getTests().isEmpty()==false)
                        presTests.setText(prescription.getTests());
                    if(prescription.getPrecautions().isEmpty()==false)
                        presPrecautions.setText(prescription.getPrecautions());
                    if(prescription.getOtherDescription().isEmpty()==false)
                        presDescription.setText(prescription.getOtherDescription());
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(PrescriptionActivity.this,"Error in loading data!",Toast.LENGTH_SHORT).show();
                }
            });

    }
    private void setUpPresView(){
        presMedicines=findViewById(R.id.pres_medicines);
        presTests=findViewById(R.id.pres_tests);
        presPrecautions=findViewById(R.id.pres_precautions);
        presDescription=findViewById(R.id.pres_description);
        presCallTime=findViewById(R.id.pres_call_time);
        presVideoCallLink=findViewById(R.id.pres_video_call_link);
        callTime=findViewById(R.id.pres_call_time_card);
        callLink=findViewById(R.id.pres_video_call_link_card);
        prescriptionText=findViewById(R.id.pres_text);
    }
}