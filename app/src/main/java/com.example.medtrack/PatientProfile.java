package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medapp.models.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfile extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView patProName,patProDob,patProSex,patProBloodGroup,patProHeight,patProWeight,patProAllergy,patProChronicDisease,patProEmail,patProMobileNo,patProAddress;
    CircleImageView patProImg;
    Patient patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setUpPatientProView();
        patient=new Patient();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Patients").child(getIntent().getExtras().getString("authId"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient=snapshot.getValue(Patient.class);
                patProName.setText(patient.getName());
                patProDob.setText("Date of Birth : "+patient.getDOB());
                patProSex.setText("Gender : "+patient.getGender());
                patProBloodGroup.setText("Blood Group : "+patient.getBloodGroup());
                patProHeight.setText("Height : "+patient.getHeight()+" cm");
                patProWeight.setText("Weight : "+patient.getWeight()+" kg");
                    patProAllergy.setText("Allergies : "+patient.getAllergy());
                    patProChronicDisease.setText("Chronic Disease : "+patient.getChronicDisease());
                patProAddress.setText("Lives at "+patient.getAddress());
                patProEmail.setText(patient.getEmailId());
                patProMobileNo.setText(patient.getMobNo());

                Glide.with(patProImg.getContext()).load(patient.getImgUrl()).into(patProImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientProfile.this,"Error occurred",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUpPatientProView(){
        patProName=findViewById(R.id.pat_pro_name);
        patProDob=findViewById(R.id.pat_pro_dob);
        patProSex=findViewById(R.id.pat_pro_sex);
        patProBloodGroup=findViewById(R.id.pat_pro_blood_grp);
        patProHeight=findViewById(R.id.pat_pro_height);
        patProWeight=findViewById(R.id.pat_pro_weight);
        patProAllergy=findViewById(R.id.pat_pro_allergy);
        patProChronicDisease=findViewById(R.id.pat_pro_chronic_disease);
        patProEmail=findViewById(R.id.pat_pro_email);
        patProMobileNo=findViewById(R.id.pat_pro_mob_no);
        patProImg=findViewById(R.id.pat_pro_img);
        patProAddress=findViewById(R.id.pat_pro_address);
    }
}