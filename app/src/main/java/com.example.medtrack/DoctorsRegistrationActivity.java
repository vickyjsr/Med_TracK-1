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

import com.example.medapp.models.Doctor;
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

import java.nio.file.FileStore;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorsRegistrationActivity extends AppCompatActivity {

    CircleImageView regDocImg;
    EditText docRegName,docRegEmail,docRegMobNo,docRegMbbsId,docRegHospital,docRegLocality,docRegCity,docRegPinCode,docRegPassword,docRegDegree,docRegStartingYear,docRegDob;
    Button docRegBtn;
    RadioGroup docRegSexRadGrp;
    RadioButton docRegMaleRadBtn,docRegFemaleRadBtn,docRegOthersRadBtn,docRegSexRadBtn;
    FirebaseAuth dAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference sRef;
    Doctor doctor;
    ProgressDialog progressDialog;
    DatabaseReference dRef;
    Uri regDocImgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            regDocImgPath=data.getData();
            regDocImg.setImageURI(regDocImgPath);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_registration);
        setUpDocRegViews();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering... Please wait");

        dAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        doctor=new Doctor();

        docRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String doctorRegName = docRegName.getText().toString().trim();
                String doctorRegEmail = docRegEmail.getText().toString().trim();
                String doctorRegMobNo = docRegMobNo.getText().toString().trim();
                String doctorRegMbbsId = docRegMbbsId.getText().toString().trim();
                String doctorRegHospital = docRegHospital.getText().toString().trim();
                String doctorRegPassword = docRegPassword.getText().toString().trim();
                String doctorRegDegree = docRegDegree.getText().toString().trim();
                String doctorRegLocality = docRegLocality.getText().toString().trim();
                String doctorRegCity = docRegCity.getText().toString().trim();
                String doctorRegPinCode = docRegPinCode.getText().toString().trim();
                String doctorRegDob = docRegDob.getText().toString().trim();
                String doctorStartingYear= docRegStartingYear.getText().toString().trim();
                String doctorRegAddress= doctorRegLocality+" "+doctorRegCity+" "+doctorRegPinCode;

                if(doctorRegName.isEmpty()||doctorRegEmail.isEmpty()||doctorRegMobNo.isEmpty()||doctorRegMbbsId.isEmpty()||doctorRegHospital.isEmpty()||doctorRegPassword.isEmpty()||doctorRegCity.isEmpty()||doctorRegLocality.isEmpty()||doctorRegPinCode.isEmpty()||doctorRegDob.isEmpty()||doctorStartingYear.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Fill all the details!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else  if(doctorRegPassword.length()<6){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Password too short!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(docRegMaleRadBtn.isChecked()||docRegFemaleRadBtn.isChecked()||docRegOthersRadBtn.isChecked()){

                    if(regDocImgPath==null){
                        progressDialog.dismiss();
                        Snackbar.make(view, "Select image!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else {
                        final int selectedRadId= docRegSexRadGrp.getCheckedRadioButtonId();
                        docRegSexRadBtn=findViewById(selectedRadId);
                        String doctorRegSex=docRegSexRadBtn.getText().toString().trim();

                        doctor.setName(doctorRegName);
                        doctor.setDegree(doctorRegDegree);
                        doctor.setSex(doctorRegSex);
                        doctor.setDob(doctorRegDob);
                        doctor.setExperience(("Professional doctor since "+doctorStartingYear));
                        doctor.setEmailId(doctorRegEmail);
                        doctor.setRegNo(doctorRegMbbsId);
                        doctor.setMobNo(doctorRegMobNo);
                        doctor.setHospital(doctorRegHospital);
                        doctor.setAddress(doctorRegAddress);
                        doctor.setPassword(doctorRegPassword);

                        dAuth.createUserWithEmailAndPassword(doctorRegEmail,doctorRegPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    doctor.setAuthId(dAuth.getUid());
                                    sRef=storage.getReference().child(dAuth.getUid()).child("ProfilePicture");
                                    UploadTask uploadTask=sRef.putFile(regDocImgPath);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(DoctorsRegistrationActivity.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                            dAuth.signOut();
                                            startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                            finish();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    doctor.setImgUrl(uri.toString());
                                                    dRef=database.getReference("Doctors").child(dAuth.getUid());
                                                    dRef.setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(DoctorsRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                                                dAuth.signOut();
                                                                startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                                finish();
                                                            }
                                                            else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(DoctorsRegistrationActivity.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                                                dAuth.signOut();
                                                                startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(DoctorsRegistrationActivity.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                                    dAuth.signOut();
                                                    startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    });

                                    /*dRef=database.getReference("Doctors").child(dAuth.getUid());
                                    dRef.setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sRef=storage.getReference().child(dAuth.getUid()).child("ProfilePicture");
                                                UploadTask uploadTask=sRef.putFile(regDocImgPath);
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(DoctorsRegistrationActivity.this,"Image Upload failed!",Toast.LENGTH_SHORT).show();
                                                        dAuth.signOut();
                                                        startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                        finish();
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(DoctorsRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                                        dAuth.signOut();
                                                        startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                            else {
                                                dAuth.signOut();
                                                progressDialog.dismiss();
                                                Toast.makeText(DoctorsRegistrationActivity.this,"Error in uploading details!",Toast.LENGTH_SHORT).show();
                                                dAuth.signOut();
                                                startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                                finish();
                                            }
                                        }
                                    });*/
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(DoctorsRegistrationActivity.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                                    dAuth.signOut();
                                    startActivity(new Intent(DoctorsRegistrationActivity.this,LogInActivity.class));
                                    finish();
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

        regDocImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");//application/* audio/* video/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
    }


    private void setUpDocRegViews(){
        docRegName=findViewById(R.id.doc_reg_name);
        docRegEmail=findViewById(R.id.doc_reg_email_id);
        docRegMobNo=findViewById(R.id.doc_reg_mob_no);
        docRegMbbsId=findViewById(R.id.doc_reg_mbbs_id);
        docRegHospital=findViewById(R.id.doc_reg_hospital);
        docRegPassword=findViewById(R.id.doc_reg_password);
        docRegBtn=findViewById(R.id.doc_reg_button);
        docRegLocality=findViewById(R.id.doc_reg_locality);
        docRegCity=findViewById(R.id.doc_reg_city);
        docRegPinCode=findViewById(R.id.doc_reg_pin_code);
        docRegDegree=findViewById(R.id.doc_reg_degree);
        docRegStartingYear=findViewById(R.id.doc_reg_starting_year);
        docRegSexRadGrp=findViewById(R.id.doc_reg_sex_rad_grp);
        docRegMaleRadBtn=findViewById(R.id.doc_reg_male);
        docRegFemaleRadBtn=findViewById(R.id.doc_reg_female);
        docRegOthersRadBtn=findViewById(R.id.doc_reg_others);
        docRegDob=findViewById(R.id.doc_reg_dob);
        regDocImg=findViewById(R.id.doc_reg_img);
    }
}