package com.example.medapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medapp.CauseActivity;
import com.example.medapp.DoctorProfile;
import com.example.medapp.PatientProfile;
import com.example.medapp.PrescriptionActivity;
import com.example.medapp.R;
import com.example.medapp.models.Appointment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AppointmentAdapter extends FirebaseRecyclerAdapter<Appointment,AppointmentAdapter.appointmentViewHolder> {

    private Context context;
    public AppointmentAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull appointmentViewHolder holder, int position, @NonNull final Appointment model) {
        holder.sinAppDoc.setText(model.getTo());
        holder.sinAppPat.setText(model.getFrom());
        holder.sinAppDate.setText(model.getAppointmentDate());
        holder.sinAppTime.setText(model.getAppointmentTime());
        holder.sinAppStatus.setText(model.getAppointmentStatus());
        holder.sinAppCause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CauseActivity.class);
                intent.putExtra("appId",model.getAppointmentId());
                intent.putExtra("pat",model.getFrom());
                intent.putExtra("patId",model.getFromAuthId());
                intent.putExtra("doc",model.getTo());
                intent.putExtra("docId",model.getToAuthId());
                intent.putExtra("appStatus",model.getAppointmentStatus());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.sinAppPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getAppointmentStatus().equals("Prescribed")){
                Intent intent = new Intent(context, PrescriptionActivity.class);
                intent.putExtra("appId",model.getAppointmentId());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
                }
                else
                {
                    Toast.makeText(context,"Not prescribed yet!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.sinAppPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PatientProfile.class);
                intent.putExtra("authId",model.getFromAuthId());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.sinAppDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorProfile.class);
                intent.putExtra("authId",model.getToAuthId());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public appointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_appointment_folder,parent,false);
        return new AppointmentAdapter.appointmentViewHolder(view);
    }

    class appointmentViewHolder extends RecyclerView.ViewHolder{
        TextView sinAppDoc,sinAppPat,sinAppTime,sinAppDate,sinAppStatus;
        Button sinAppCause,sinAppPres;
        public appointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            sinAppDoc=itemView.findViewById(R.id.sin_row_app_doc_name);
            sinAppPat=itemView.findViewById(R.id.sin_row_app_pat_name);
            sinAppTime=itemView.findViewById(R.id.sin_row_app_time);
            sinAppDate=itemView.findViewById(R.id.sin_row_app_date);
            sinAppStatus=itemView.findViewById(R.id.sin_row_app_status);
            sinAppCause=itemView.findViewById(R.id.sin_row_app_cause);
            sinAppPres=itemView.findViewById(R.id.sin_row_app_pres);
        }
    }
}
