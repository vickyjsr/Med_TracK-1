package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medapp.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientHome extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    CircleImageView patHomeImg;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        patHomeImg=findViewById(R.id.pat_home_img);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        patient = new Patient();
        reference = database.getReference("Patients").child(mAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient=snapshot.getValue(Patient.class);
                Glide.with(patHomeImg.getContext()).load(patient.getImgUrl()).into(patHomeImg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientHome.this,"Can't load image!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pat_log_out:{
                mAuth.signOut();
                Toast.makeText(PatientHome.this,"Logged out successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PatientHome.this,LogInActivity.class));
                finish();
                break;
            }
            case R.id.pat_help:{
                Intent intent= new Intent(PatientHome.this,Help.class);
                intent.putExtra("curUser","Patients");
                startActivity(intent);
                //Toast.makeText(PatientHome.this,"Help!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.pat_contact_us:{
                startActivity(new Intent(PatientHome.this,ContactUs.class));
                //Toast.makeText(PatientHome.this,"Contact us",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.pat_edit_profile:{
                startActivity(new Intent(PatientHome.this,EditPatientProfile.class));
                //Toast.makeText(PatientHome.this,"Edit profile",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public  void startListDoctors(View view){
        Intent intent = new Intent(PatientHome.this,ListDoctors.class);
        startActivity(intent);
    }
    public void startScanQRcode(View view){
        Intent intent = new Intent(PatientHome.this,QRCodeScanner.class);
        intent.putExtra("entMan","NO");
        intent.putExtra("To","null");
        intent.putExtra("ToAuthId","null");
        startActivity(intent);
    }
    public void startListAppointments(View view){
        Intent intent = new Intent(PatientHome.this,ListAppointment.class);
        startActivity(intent);
        //Toast.makeText(PatientHome.this,"Appointments",Toast.LENGTH_SHORT).show();
    }
    public void viewMyProfile(View view){
        Intent intent = new Intent(PatientHome.this,PatientProfile.class);
        intent.putExtra("authId",mAuth.getUid());
        startActivity(intent);
        //Toast.makeText(PatientHome.this,"My Profile",Toast.LENGTH_SHORT).show();
    }
}