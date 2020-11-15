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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medapp.models.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    private EditText loginEmail,loginPassword;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Doctor doctor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setUpLoginView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        doctor = new Doctor();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String logInEmail = loginEmail.getText().toString().trim();
                String logInPassword = loginPassword.getText().toString().trim();
                if(logInEmail.isEmpty()||logInPassword.isEmpty()){
                    progressDialog.dismiss();
                    Snackbar.make(view, "Enter e-mail id and password!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(logInEmail,logInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                myRef=database.getReference("Doctors").child(mAuth.getUid());
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        doctor = snapshot.getValue(Doctor.class);
                                        if(doctor==null) {
                                            Toast.makeText(LogInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(LogInActivity.this, PatientHome.class);
                                            intent.putExtra("curUser","Patients");
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(LogInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(LogInActivity.this, DoctorHome.class);
                                            intent.putExtra("curUser","Doctors");
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LogInActivity.this,"Login failed!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LogInActivity.this,"Login failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public void startRegistrationActivity(View view){
        startActivity(new Intent(LogInActivity.this,RegistrationPopUp.class));
    }
    public void startResetPasswordActivity(View view){
        startActivity(new Intent(LogInActivity.this,ResetPassword.class));
    }
    private void setUpLoginView(){
        loginEmail=findViewById(R.id.pat_login_email_id);
        loginPassword=findViewById(R.id.pat_login_password);
        loginBtn=findViewById(R.id.pat_login_button);
    }
}