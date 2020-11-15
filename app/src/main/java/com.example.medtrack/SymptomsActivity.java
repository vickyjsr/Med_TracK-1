package com.example.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medapp.models.Appointment;
import com.example.medapp.models.Cause;
import com.example.medapp.models.Doctor;
import com.example.medapp.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SymptomsActivity extends AppCompatActivity {

    private CheckBox feverCheckbox,coldCheckbox,coughCheckbox,runnyNoseCheckbox,headAcheCheckbox,stomachacheCheckbox,
            earacheCheckbox,looseMotionCheckbox,vomitingCheckbox,hardStoolCheckbox,looseStoolCheckbox,dizzinessCheckbox,
            frequentUrinationCheckbox,constipationCheckbox,paleEyesCheckbox,paleUrineCheckbox,unconsciousnessCheckbox,
            soreThroatCheckbox,acidityCheckbox;
    private EditText symptomDescription;
    static String symptoms="",reqCall,symptomDes;
    private RadioGroup reqCallRadGrp;
    private RadioButton reqAudioCall,reqVideoCall,reqNoCall,callRadBtn;
    private Button reqForPrescription;
    FirebaseDatabase database;
    DatabaseReference cRef;
    Cause cause;
    FirebaseAuth mAuth;
    static DatabaseReference mRef,dRef,pRef;
    Appointment appointment;
    Patient patient;
    private ProgressDialog progressDialog;
    static String curDate,curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        setUpSymptomView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        cause = new Cause();
        patient=new Patient();
        appointment=new Appointment();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending appointment request...");

        reqForPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                int callRadId = reqCallRadGrp.getCheckedRadioButtonId();
                callRadBtn=findViewById(callRadId);
                reqCall=callRadBtn.getText().toString().trim();
                symptomDes=symptomDescription.getText().toString().trim();
                if(symptomDes.isEmpty())
                    symptomDes="null";

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm:ss a");
                curDate=simpleDateFormat.format(calendar.getTime());
                curTime=simpleTimeFormat.format(calendar.getTime());
                final String appId = curDate+curTime;

                if(feverCheckbox.isChecked())
                    symptoms=symptoms+feverCheckbox.getText().toString().trim();
                if(coldCheckbox.isChecked())
                    symptoms=symptoms+" "+coldCheckbox.getText().toString().trim();
                if(coughCheckbox.isChecked())
                    symptoms=symptoms+" "+coughCheckbox.getText().toString().trim();
                if(runnyNoseCheckbox.isChecked())
                symptoms=symptoms+" "+runnyNoseCheckbox.getText().toString().trim();
                if(headAcheCheckbox.isChecked())
                symptoms=symptoms+" "+headAcheCheckbox.getText().toString().trim();
                if(stomachacheCheckbox.isChecked())
                symptoms=symptoms+" "+stomachacheCheckbox.getText().toString().trim();
                if(earacheCheckbox.isChecked())
                symptoms=symptoms+" "+earacheCheckbox.getText().toString().trim();
                if(looseMotionCheckbox.isChecked())
                symptoms=symptoms+" "+looseMotionCheckbox.getText().toString().trim();
                if(looseStoolCheckbox.isChecked())
                symptoms=symptoms+" "+looseStoolCheckbox.getText().toString().trim();
                if(hardStoolCheckbox.isChecked())
                symptoms=symptoms+" "+hardStoolCheckbox.getText().toString().trim();
                if(vomitingCheckbox.isChecked())
                symptoms=symptoms+" "+vomitingCheckbox.getText().toString().trim();
                if(dizzinessCheckbox.isChecked())
                symptoms=symptoms+" "+dizzinessCheckbox.getText().toString().trim();
                if(frequentUrinationCheckbox.isChecked())
                symptoms=symptoms+" "+frequentUrinationCheckbox.getText().toString().trim();
                if(constipationCheckbox.isChecked())
                symptoms=symptoms+" "+constipationCheckbox.getText().toString().trim();
                if(paleEyesCheckbox.isChecked())
                symptoms=symptoms+" "+paleEyesCheckbox.getText().toString().trim();
                if(paleUrineCheckbox.isChecked())
                symptoms=symptoms+" "+paleUrineCheckbox.getText().toString().trim();
                if(unconsciousnessCheckbox.isChecked())
                symptoms=symptoms+" "+unconsciousnessCheckbox.getText().toString().trim();
                if(soreThroatCheckbox.isChecked())
                    symptoms=symptoms+" "+soreThroatCheckbox.getText().toString().trim();
                if(acidityCheckbox.isChecked())
                    symptoms = symptoms+" "+acidityCheckbox.getText().toString().trim();
                if(symptoms.isEmpty())
                    symptoms="null";

                cause.setBodyTemp(getIntent().getExtras().getString("Temp"));
                cause.setPulse(getIntent().getExtras().getString("Pulse"));
                cause.setOxygenSaturation(getIntent().getExtras().getString("OxySat"));
                cause.setRespiratoryRate(getIntent().getExtras().getString("ResRate"));
                cause.setBloodPressure(getIntent().getExtras().getString("BloodPressure"));
                cause.setCallRequest(reqCall);
                cause.setDescription(symptomDes);
                cause.setSymptoms(symptoms);
                cause.setSuggestions("null");

                cRef=database.getReference("Causes").child(appId);
                cRef.setValue(cause).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            mRef=database.getReference("Patients").child(mAuth.getUid());
                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    patient=snapshot.getValue(Patient.class);

                                    pRef=database.getReference("PatApp").child(patient.getAuthId()).child(appId);
                                    dRef=database.getReference("DocApp").child(getIntent().getExtras().getString("ToAuthId")).child(appId);

                                    appointment.setAppointmentDate(curDate);
                                    appointment.setAppointmentTime(curTime);
                                    appointment.setAppointmentStatus("Requested");
                                    appointment.setAppointmentId(appId);
                                    appointment.setFrom(patient.getName());
                                    appointment.setTo(getIntent().getExtras().getString("To"));
                                    appointment.setFromAuthId(patient.getAuthId());
                                    appointment.setToAuthId(getIntent().getExtras().getString("ToAuthId"));

                                    dRef.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                pRef.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            progressDialog.dismiss();
                                                            Toast.makeText(SymptomsActivity.this,"Prescription requested",Toast.LENGTH_SHORT).show();
                                                            //startActivity(new Intent(SymptomsActivity.this,PatientHome.class));
                                                            finish();
                                                        }
                                                        else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(SymptomsActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                            else {
                                                progressDialog.dismiss();
                                                Toast.makeText(SymptomsActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SymptomsActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            /*Intent intent=new Intent(SymptomsActivity.this, AppointmentActivity.class);
                            intent.putExtra("To",getIntent().getExtras().getString("To"));
                            intent.putExtra("ToAuthId",getIntent().getExtras().getString("ToAuthId"));
                            intent.putExtra("Temp",getIntent().getExtras().getString("Temp"));
                            intent.putExtra("Pulse",getIntent().getExtras().getString("Pulse"));
                            intent.putExtra("OxySat",getIntent().getExtras().getString("OxySat"));
                            intent.putExtra("ResRate",getIntent().getExtras().getString("ResRate"));
                            intent.putExtra("BloodPressure",getIntent().getExtras().getString("BloodPressure"));
                            intent.putExtra("Symptoms",symptoms);
                            intent.putExtra("symptomDes",symptomDes);
                            intent.putExtra("Call",reqCall);
                            intent.putExtra("appId",appId);
                            startActivity(intent);*/
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SymptomsActivity.this,"Appointment failed!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void setUpSymptomView(){
        feverCheckbox=findViewById(R.id.fever_checkbox);
        coldCheckbox=findViewById(R.id.cold_checkbox);
        coughCheckbox=findViewById(R.id.cough_checkbox);
        runnyNoseCheckbox=findViewById(R.id.runny_nose_checkbox);
        headAcheCheckbox=findViewById(R.id.headache_checkbox);
        stomachacheCheckbox=findViewById(R.id.stomachache_checkbox);
        earacheCheckbox=findViewById(R.id.earache_checkbox);
        looseMotionCheckbox=findViewById(R.id.loose_motion_checkbox);
        vomitingCheckbox=findViewById(R.id.vomiting_checkbox);
        looseStoolCheckbox=findViewById(R.id.loose_stool_checkbox);
        hardStoolCheckbox=findViewById(R.id.hard_stool_checkbox);
        dizzinessCheckbox=findViewById(R.id.dizziness_checkbox);
        frequentUrinationCheckbox=findViewById(R.id.frequent_urination_checkbox);
        constipationCheckbox=findViewById(R.id.constipation_checkbox);
        paleEyesCheckbox=findViewById(R.id.pale_eyes_checkbox);
        paleUrineCheckbox=findViewById(R.id.pale_urine_checkbox);
        unconsciousnessCheckbox=findViewById(R.id.unconsciousness_checkbox);
        symptomDescription=findViewById(R.id.symptoms_description);
        reqCallRadGrp=findViewById(R.id.req_for_call_rad_grp);
        reqAudioCall=findViewById(R.id.audio_call_rad_btn);
        reqVideoCall=findViewById(R.id.video_call_rad_btn);
        reqNoCall=findViewById(R.id.no_call_rad_btn);
        reqForPrescription=findViewById(R.id.req_for_prescription);
        soreThroatCheckbox=findViewById(R.id.sore_throat_checkbox);
        acidityCheckbox=findViewById(R.id.acidity_checkbox);

    }
}