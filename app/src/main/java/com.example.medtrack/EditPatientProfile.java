package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medapp.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPatientProfile extends AppCompatActivity {

    private EditText editPatName,editPatMobNo,editPatDob,editPatBloodGrp,editPatWeight,editPatHeight,editPatAllergies,editPatChronicDisease,editPatLocality,editPatCity,editPatPinCode;
    private Button editPatProSaveBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    static DatabaseReference mRef;
    static Patient patient, newPatient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);
        setUpEditPatView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        patient = new Patient();
        newPatient = new Patient();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating details...");
        mRef = database.getReference("Patients").child(mAuth.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient=snapshot.getValue(Patient.class);
                editPatName.setText(patient.getName());
                editPatMobNo.setText(patient.getMobNo());
                editPatDob.setText(patient.getDOB());
                editPatBloodGrp.setText(patient.getBloodGroup());
                editPatWeight.setText(patient.getWeight());
                editPatHeight.setText(patient.getHeight());
                editPatAllergies.setText(patient.getAllergy());
                editPatChronicDisease.setText(patient.getChronicDisease());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditPatientProfile.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        editPatProSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name= editPatName.getText().toString().trim();
                String mobNo= editPatMobNo.getText().toString().trim();
                String dob= editPatDob.getText().toString().trim();
                String bloodGrp= editPatBloodGrp.getText().toString().trim();
                String weight= editPatWeight.getText().toString().trim();
                String height= editPatHeight.getText().toString().trim();
                String allergies= editPatAllergies.getText().toString().trim();
                String chronicDisease= editPatChronicDisease.getText().toString().trim();
                String locality= editPatLocality.getText().toString().trim();
                String city= editPatCity.getText().toString().trim();
                String pinCode= editPatPinCode.getText().toString().trim();

                if(name.isEmpty()||mobNo.isEmpty()||dob.isEmpty()||bloodGrp.isEmpty()||weight.isEmpty()||height.isEmpty()||locality.isEmpty()||city.isEmpty()||pinCode.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Fill all the details!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    newPatient.setName(name);
                    newPatient.setEmailId(patient.getEmailId());
                    newPatient.setMobNo(mobNo);
                    newPatient.setDOB(dob);
                    newPatient.setBloodGroup(bloodGrp);
                    newPatient.setGender(patient.getGender());
                    newPatient.setWeight(weight);
                    newPatient.setHeight(height);
                    newPatient.setAllergy(allergies);
                    newPatient.setChronicDisease(chronicDisease);
                    newPatient.setAddress(locality+" "+city+" "+pinCode);
                    newPatient.setPassword(patient.getPassword());
                    newPatient.setAuthId(patient.getAuthId());
                    newPatient.setImgUrl(patient.getImgUrl());

                    mRef.setValue(newPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(EditPatientProfile.this,"Details updated",Toast.LENGTH_SHORT).show();
                                // startActivity(new Intent(EditPatientProfile.this,PatientHome.class));
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(EditPatientProfile.this,"Error in updating details!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void setUpEditPatView(){
        editPatName=findViewById(R.id.edit_pat_name);
        editPatMobNo=findViewById(R.id.edit_pat_mob_no);
        editPatDob=findViewById(R.id.edit_pat_dob);
        editPatBloodGrp=findViewById(R.id.edit_pat_blood_group);
        editPatWeight=findViewById(R.id.edit_pat_weight);
        editPatHeight=findViewById(R.id.edit_pat_height);
        editPatAllergies=findViewById(R.id.edit_pat_allergy);
        editPatChronicDisease=findViewById(R.id.edit_pat_chronic_disease);
        editPatLocality=findViewById(R.id.edit_pat_locality);
        editPatCity=findViewById(R.id.edit_pat_city);
        editPatPinCode=findViewById(R.id.edit_pat_pin_code);
        editPatProSaveBtn=findViewById(R.id.edit_pat_pro_save_btn);
    }
}