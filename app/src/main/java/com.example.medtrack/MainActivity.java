package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.medapp.models.Doctor;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ImageView ivTop,ivHeart,ivBeat,ivBottom;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay=200;
    Handler handler = new Handler();
    FirebaseAuth mAuth;
    FirebaseUser user;
    Doctor doctor;
    FirebaseDatabase database;
    DatabaseReference mRef;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        doctor=new Doctor();


        // assign
        ivTop=findViewById(R.id.iv_top);
        ivBottom=findViewById(R.id.iv_bottom);
        ivHeart=findViewById(R.id.iv_heart);
        ivBeat=findViewById(R.id.iv_beat);
        textView=findViewById(R.id.text_view);

        //set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize top animation
        Animation animation1= AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTop.setAnimation(animation1);

        //Initialize object animator
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivHeart, PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleX",1.2f)
        );
        //set duration
        objectAnimator.setDuration(500);
        //set repeat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //Set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animator
        objectAnimator.start();

        //set animate text
        animatText("Med_TracK");

        //load GIF
        Glide.with(this).load("https://www.youtube.com/redirect?event=video_description&v=OARIq17u12g&redir_token=QUFFLUhqblpibnNqRUljRDJ1QjhodEJtSzB6QVpHZjhhd3xBQ3Jtc0ttMGFIRl9pUnlZV01UdExUNmw3N2hUb2Q0Qm5oQnpTbGtGLXgyMEk1dGpiNlh2cmlhMEVjcG82NF9ncHpFNnRtT1hFNjJDNEVXSGd6WS01TFRYRW1iWVZQbXExUi1pQnBqM2RlMlZ1QWVvMzZ6b25GTQ%3D%3D&q=https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fdemoapp-ae96a.appspot.com%2Fo%2Fheart_beat.gif%3Falt%3Dmedia%26token%3Db21dddd8-782c-457c-babd-f2e922ba172b").asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivBeat);

        // initialize bottom animation
        Animation animation2 = AnimationUtils.loadAnimation(this
                ,R.anim.bottom_wave);

        // Start bottom animation
        ivBottom.setAnimation(animation2);

        if(user!=null){
            mRef=database.getReference("Doctors").child(mAuth.getUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    doctor = snapshot.getValue(Doctor.class);
                    if(doctor==null) {
                        Intent intent=new Intent(MainActivity.this, PatientHome.class);
                        intent.putExtra("curUser","Patients");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent=new Intent(MainActivity.this, DoctorHome.class);
                        intent.putExtra("curUser","Doctors");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this,LogInActivity.class));
                            finish();
                        }
                    },4000);
                }
            });
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,LogInActivity.class));
                    finish();
                }
            },4000);
        }

    }

    Runnable runnable =new Runnable() {
        @Override
        public void run() {
            //when runnable is run
            //set text
            textView.setText(charSequence.subSequence(0,index++));
            //check condition
            if(index<= charSequence.length())
            {
                //when length is equal to text length
                //run handler
                handler.postDelayed(runnable,delay);
            }
        }
    };

    //Create animated text method
    public void animatText(CharSequence cs)
    {
        //set text
        charSequence = cs;
        //clear index
        index=0;
        //clear text
        textView.setText("");
        //remove call back
        handler.removeCallbacks(runnable);
        //run handler
        handler.postDelayed(runnable,delay);

    }
}
