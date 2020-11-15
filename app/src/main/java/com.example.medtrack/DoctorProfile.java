package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medapp.models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfile extends AppCompatActivity {

    TextView docProName,docProDegrees,docProMbbsId,docProHospital,docProExperience,docProAddress,docProDob,docProSex,docProEmail,docProMobNo;
    FirebaseDatabase database;
    CircleImageView docProImg;
    DatabaseReference reference;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        setUpDocProView();
        database = FirebaseDatabase.getInstance();
        reference=database.getReference("Doctors").child(getIntent().getExtras().getString("authId"));
        doctor = new Doctor();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor=snapshot.getValue(Doctor.class);

                docProName.setText("Dr "+doctor.getName());
                if(doctor.getDegree().isEmpty())
                    docProDegrees.setText("MBBS");
                else
                    docProDegrees.setText("MBBS, "+doctor.getDegree());
                docProMbbsId.setText("MBBS Registration Id : "+doctor.getRegNo());
                docProHospital.setText("Works at "+doctor.getHospital());
                docProExperience.setText(doctor.getExperience());
                docProAddress.setText("Lives at "+doctor.getAddress());
                docProDob.setText("Date of birth : "+doctor.getDob());
                docProSex.setText("Gender : "+doctor.getSex());
                docProEmail.setText("E-mail id : "+doctor.getEmailId());
                docProMobNo.setText("Mobile number : "+doctor.getMobNo());
                Glide.with(docProImg.getContext()).load(doctor.getImgUrl()).into(docProImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorProfile.this,"Error in loading details!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpDocProView(){
        docProName = findViewById(R.id.doc_pro_name);
        docProDegrees = findViewById(R.id.doc_pro_degrees);
        docProMbbsId = findViewById(R.id.doc_pro_mbbs_id);
        docProHospital = findViewById(R.id.doc_pro_hospital);
        docProExperience = findViewById(R.id.doc_pro_exp);
        docProAddress = findViewById(R.id.doc_pro_address);
        docProDob = findViewById(R.id.doc_pro_dob);
        docProSex = findViewById(R.id.doc_pro_sex);
        docProEmail = findViewById(R.id.doc_pro_email);
        docProMobNo= findViewById(R.id.doc_pro_mob_no);
        docProImg=findViewById(R.id.doc_pro_img);
    }
}