package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medapp.adapters.AppointmentAdapter;
import com.example.medapp.models.Appointment;
import com.example.medapp.models.Doctor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorHome extends AppCompatActivity {

    TextView docHomeName,docHomeDes;
    CircleImageView docHomeImg;
    CardView docHomeProCard;
    RecyclerView recyclerView;
    AppointmentAdapter appointmentAdapter;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dRef;
    StorageReference  sRef;
    static Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        setUpDocHomeView();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        doctor = new Doctor();

        dRef=database.getReference("Doctors").child(mAuth.getUid());

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor= snapshot.getValue(Doctor.class);

                docHomeName.setText("Dr "+doctor.getName());
                docHomeDes.setText(doctor.getHospital());
                Glide.with(docHomeImg.getContext()).load(doctor.getImgUrl()).into(docHomeImg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorHome.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });





        recyclerView=findViewById(R.id.doc_home_list_of_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Appointment> options=
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("DocApp").child(mAuth.getUid()), Appointment.class)
                        .build();
        appointmentAdapter=new AppointmentAdapter(options,this);
        recyclerView.setAdapter(appointmentAdapter);

        docHomeProCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorHome.this,DoctorProfile.class);
                intent.putExtra("authId",mAuth.getUid());
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        appointmentAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        appointmentAdapter.stopListening();
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
                Toast.makeText(DoctorHome.this,"Logged out successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DoctorHome.this,LogInActivity.class));
                finish();
                break;
            }
            case R.id.pat_help:{
                Intent intent= new Intent(DoctorHome.this,Help.class);
                intent.putExtra("curUser","Doctors");
                startActivity(intent);
                //Toast.makeText(DoctorHome.this,"Help!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.pat_contact_us:{
                startActivity(new Intent(DoctorHome.this,ContactUs.class));
                //Toast.makeText(DoctorHome.this,"Contact us",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.pat_edit_profile:{
                startActivity(new Intent(DoctorHome.this,EditDoctorProfile.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpDocHomeView(){
        docHomeName=findViewById(R.id.doc_home_name);
        docHomeDes=findViewById(R.id.doc_home_description);
        docHomeImg=findViewById(R.id.doc_home_img);
        docHomeProCard=findViewById(R.id.doc_home_pro_card);
    }
}