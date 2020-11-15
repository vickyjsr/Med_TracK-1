package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medapp.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientRegistration extends AppCompatActivity {

    private EditText regPatName,regPatEmail,regPatMobNo,regPatWeight,regPatHeight,regPatDob,regPatPwd;
    private EditText regPatBloodGrp,regPatAllergy,regPatChronicDisease,regPatPinCode,regPatLocality,regPatCity;
    private Button patRegBtn;
    private CircleImageView regPatImg;
    private RadioGroup regPatSexRadGrp;
    private RadioButton regPatMaleRadBtn,regPatFemaleRadBtn,regPatOthersRadBtn,regPatSex;
    private ProgressDialog progressDialog;
    FirebaseAuth pAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference sRef;
    Patient patient;
    DatabaseReference pRef;
    Uri regPatImgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            regPatImgPath=data.getData();
            regPatImg.setImageURI(regPatImgPath);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        setUpPatRegView();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering... Please wait");

        pAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        patient=new Patient();

        patRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String regPatientName=regPatName.getText().toString().trim();
                String regPatientEmail=regPatEmail.getText().toString().trim();
                String regPatientMobNo=regPatMobNo.getText().toString().trim();
                String regPatientWeight=regPatWeight.getText().toString().trim();
                String regPatientHeight=regPatHeight.getText().toString().trim();
                String regPatientDob=regPatDob.getText().toString().trim();
                String regPatientBloodGrp=regPatBloodGrp.getText().toString().trim();
                String regPatientAllergy=regPatAllergy.getText().toString().trim();
                String regPatientChronicDisease=regPatChronicDisease.getText().toString().trim();
                String regPatientPinCode=regPatPinCode.getText().toString().trim();
                String regPatientLocality=regPatLocality.getText().toString().trim();
                String regPatientCity=regPatCity.getText().toString().trim();
                String regPatientPwd=regPatPwd.getText().toString().trim();
                String regPatientAddress= regPatientLocality +" "+ regPatientCity +" "+ regPatientPinCode;

                if(regPatientAllergy.isEmpty()){
                    regPatientAllergy="None";
                }
                if(regPatientChronicDisease.isEmpty()){
                    regPatientChronicDisease="None";
                }

                if(regPatientName.isEmpty()||regPatientEmail.isEmpty()||regPatientMobNo.isEmpty()||regPatientWeight.isEmpty()||regPatientHeight.isEmpty()||regPatientDob.isEmpty()||regPatientPwd.isEmpty()||regPatientBloodGrp.isEmpty()||regPatientPinCode.isEmpty()||regPatientCity.isEmpty()||regPatientLocality.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Fill all the details!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(regPatientPwd.length()<6){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Password too short!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(regPatMaleRadBtn.isChecked()||regPatFemaleRadBtn.isChecked()||regPatOthersRadBtn.isChecked()){
                    if(regPatImgPath==null){
                        progressDialog.dismiss();
                        Snackbar.make(view, "Select image!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else {
                        int selectedRadId= regPatSexRadGrp.getCheckedRadioButtonId();
                        regPatSex=findViewById(selectedRadId);
                        String regPatientSex=regPatSex.getText().toString().trim();

                        patient.setBloodGroup(regPatientBloodGrp);
                        patient.setGender(regPatientSex);
                        patient.setAllergy(regPatientAllergy);
                        patient.setChronicDisease(regPatientChronicDisease);
                        patient.setName(regPatientName);
                        patient.setEmailId(regPatientEmail);
                        patient.setMobNo(regPatientMobNo);
                        patient.setWeight(regPatientWeight);
                        patient.setHeight(regPatientHeight);
                        patient.setDOB(regPatientDob);
                        patient.setAddress(regPatientAddress);
                        patient.setPassword(regPatientPwd);

                        pAuth.createUserWithEmailAndPassword(regPatientEmail,regPatientPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    patient.setAuthId(pAuth.getUid());

                                    sRef=storage.getReference().child(pAuth.getUid()).child("ProfilePicture");
                                    UploadTask uploadTask=sRef.putFile(regPatImgPath);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            sRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(PatientRegistration.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                                    pAuth.signOut();
                                                    startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                                    finish();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    patient.setImgUrl(uri.toString());
                                                    pRef=database.getReference("Patients").child(pAuth.getUid());

                                                    pRef.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(PatientRegistration.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                                                pAuth.signOut();
                                                                startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                                                finish();
                                                            }
                                                            else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(PatientRegistration.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                                                pAuth.signOut();
                                                                startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PatientRegistration.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                            pAuth.signOut();
                                            startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                            finish();
                                        }
                                    });
                                   /* patient.setAuthId(pAuth.getUid());
                                    pRef=database.getReference("Patients").child(pAuth.getUid());
                                    pRef.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sRef=storage.getReference().child(pAuth.getUid()).child("ProfilePicture");
                                                UploadTask uploadTask=sRef.putFile(regPatImgPath);
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PatientRegistration.this,"Image Upload failed!",Toast.LENGTH_SHORT).show();
                                                        pAuth.signOut();
                                                        startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                                        finish();
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PatientRegistration.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                                        pAuth.signOut();
                                                        startActivity(new Intent(PatientRegistration.this,LogInActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                            else {
                                                pAuth.signOut();
                                                progressDialog.dismiss();
                                                Toast.makeText(PatientRegistration.this,"Error in uploading details!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });*/
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(PatientRegistration.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    progressDialog.dismiss();
                    Snackbar.make(view, "Select your Gender!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        regPatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");//application/* audio/* video/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
    }


    private void setUpPatRegView(){
        regPatName=findViewById(R.id.reg_pat_name);
        regPatEmail=findViewById(R.id.reg_pat_email_id);
        regPatMobNo=findViewById(R.id.reg_pat_mob_no);
        regPatWeight=findViewById(R.id.reg_pat_weight);
        regPatHeight=findViewById(R.id.reg_pat_height);
        regPatDob=findViewById(R.id.reg_pat_dob);
        regPatPinCode=findViewById(R.id.reg_pat_pin_code);
        regPatSexRadGrp=findViewById(R.id.reg_pat_sex_rad_grp);
        regPatBloodGrp=findViewById(R.id.reg_pat_blood_group);
        regPatAllergy=findViewById(R.id.reg_pat_allergy);
        regPatChronicDisease=findViewById(R.id.reg_pat_chronic_disease);
        regPatCity=findViewById(R.id.reg_pat_city);
        regPatLocality=findViewById(R.id.reg_pat_locality);
        regPatPwd=findViewById(R.id.reg_pat_password);
        patRegBtn=findViewById(R.id.pat_reg_btn);
        regPatMaleRadBtn=findViewById(R.id.reg_pat_male);
        regPatFemaleRadBtn=findViewById(R.id.reg_pat_female);
        regPatOthersRadBtn=findViewById(R.id.reg_pat_others);
        regPatImg = findViewById(R.id.pat_reg_img);
    }
}