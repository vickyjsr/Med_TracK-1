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

import com.example.medapp.models.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDoctorProfile extends AppCompatActivity {

    private EditText editDocName,editDocMobNo,editDocDob,editDocMbbsId,editDocDegrees,editDocHospital,editDocLocality,editDocCity,editDocPinCode;
    private Button editDocProSaveBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    static DatabaseReference mRef;
    static Doctor doctor,newDoctor;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);
        setUpEditDocView();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        doctor = new Doctor();
        newDoctor = new Doctor();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating details...");
        mRef=database.getReference("Doctors").child(mAuth.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor=snapshot.getValue(Doctor.class);
                editDocName.setText(doctor.getName());
                editDocMobNo.setText(doctor.getMobNo());
                editDocDob.setText(doctor.getDob());
                editDocMbbsId.setText(doctor.getRegNo());
                editDocHospital.setText(doctor.getHospital());
                editDocDegrees.setText(doctor.getDegree());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditDoctorProfile.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        editDocProSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String docName= editDocName.getText().toString().trim();
                String docMobNo= editDocMobNo.getText().toString().trim();
                String docDob = editDocDob.getText().toString().trim();
                String docMbbsId = editDocMbbsId.getText().toString().trim();
                String docHospital= editDocHospital.getText().toString().trim();
                String docDegrees = editDocDegrees.getText().toString().trim();
                String docLocality = editDocLocality.getText().toString().trim();
                String docCity = editDocCity.getText().toString().trim();
                String docPinCode = editDocPinCode.getText().toString().trim();

                if(docName.isEmpty()||docMobNo.isEmpty()||docDob.isEmpty()||docMbbsId.isEmpty()||docHospital.isEmpty()||docLocality.isEmpty()||docCity.isEmpty()||docPinCode.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Fill all the details!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    newDoctor.setName(docName);
                    newDoctor.setEmailId(doctor.getEmailId());
                    newDoctor.setMobNo(docMobNo);
                    newDoctor.setDob(docDob);
                    newDoctor.setSex(doctor.getSex());
                    newDoctor.setRegNo(docMbbsId);
                    newDoctor.setDegree(docDegrees);
                    newDoctor.setExperience(doctor.getExperience());
                    newDoctor.setHospital(docHospital);
                    newDoctor.setAddress(docLocality+" "+docCity+" "+docPinCode);
                    newDoctor.setPassword(doctor.getPassword());
                    newDoctor.setAuthId(doctor.getAuthId());
                    newDoctor.setImgUrl(doctor.getImgUrl());

                    mRef.setValue(newDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(EditDoctorProfile.this,"Details updated",Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(EditDoctorProfile.this,DoctorHome.class));
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(EditDoctorProfile.this,"Error in updating details!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void setUpEditDocView(){
        editDocName=findViewById(R.id.edit_doc_name);
        editDocMobNo=findViewById(R.id.edit_doc_mob_no);
        editDocDob=findViewById(R.id.edit_doc_dob);
        editDocMbbsId=findViewById(R.id.edit_doc_mbbs_id);
        editDocDegrees=findViewById(R.id.edit_doc_degrees);
        editDocHospital=findViewById(R.id.edit_doc_hospital);
        editDocLocality=findViewById(R.id.edit_doc_locality);
        editDocCity=findViewById(R.id.edit_doc_city);
        editDocPinCode=findViewById(R.id.edit_doc_pin_code);
        editDocProSaveBtn=findViewById(R.id.edit_doc_pro_save_btn);
    }
}