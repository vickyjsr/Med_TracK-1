package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText resetPasswordEmail;
    Button resetPasswordBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setUpForgetPasswordView();
        mAuth=FirebaseAuth.getInstance();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.3));

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetPasswordEmail.getText().toString().trim();
                if(email.isEmpty())
                    Toast.makeText(ResetPassword.this,"Enter e-mail id!",Toast.LENGTH_SHORT).show();
                else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this,"Password reset e-mail sent!",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(ResetPassword.this,"Error in resetting password!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setUpForgetPasswordView(){
        resetPasswordEmail=findViewById(R.id.reset_password_email);
        resetPasswordBtn=findViewById(R.id.reset_password_btn);
    }
}