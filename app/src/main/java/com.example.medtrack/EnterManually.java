package com.example.medapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterManually extends AppCompatActivity {

    EditText entManTemp,entManPulse,entManOxySat,entManResRate,entManBloodPressure;
    Button entManNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_manually);
        setUpEnterView();
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        entManNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = entManTemp.getText().toString().trim();
                if(temp.isEmpty())
                    temp="not provided";
                else
                    temp=temp+" F";
                String pulse = entManPulse.getText().toString().trim();
                if(pulse.isEmpty())
                    pulse="not provided";
                else
                    pulse = pulse+" per minute";
                String oxy = entManOxySat.getText().toString().trim();
                if(oxy.isEmpty())
                    oxy= "not provided";
                else
                    oxy = oxy+" %";
                String resp = entManResRate.getText().toString().trim();
                if(resp.isEmpty())
                    resp="not provided";
                else
                    resp= resp+" per minute";
                String pres = entManBloodPressure.getText().toString().trim();
                if(pres.isEmpty())
                    pres="not provided";
                else
                    pres = pres+" mmHg";
                Intent intent=new Intent(EnterManually.this, SymptomsActivity.class);
                intent.putExtra("To",getIntent().getExtras().getString("To"));
                intent.putExtra("ToAuthId",getIntent().getExtras().getString("ToAuthId"));
                intent.putExtra("Temp",temp);
                intent.putExtra("Pulse",pulse);
                intent.putExtra("OxySat",oxy);
                intent.putExtra("ResRate",resp);
                intent.putExtra("BloodPressure",pres);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpEnterView(){
        entManTemp=findViewById(R.id.ent_man_temp);
        entManPulse=findViewById(R.id.ent_man_pulse);
        entManOxySat=findViewById(R.id.ent_man_oxy_sat);
        entManResRate=findViewById(R.id.ent_man_res_rate);
        entManBloodPressure=findViewById(R.id.ent_man_blood_pressure);
        entManNextBtn=findViewById(R.id.ent_man_next_btn);
    }
}