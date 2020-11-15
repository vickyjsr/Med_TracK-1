package com.example.medapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medapp.AppointmentActivity;
import com.example.medapp.DoctorProfile;
import com.example.medapp.QRCodeScanner;
import com.example.medapp.R;
import com.example.medapp.models.Doctor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends FirebaseRecyclerAdapter <Doctor,DoctorAdapter.doctorViewHolder>  {
    private Context context;
    public DoctorAdapter(@NonNull FirebaseRecyclerOptions<Doctor> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull doctorViewHolder holder, int position, @NonNull final Doctor model) {
        holder.listDocName.setText("Dr "+model.getName());
        holder.listDocHospital.setText(model.getHospital());
        Glide.with(holder.docRowImg.getContext()).load(model.getImgUrl()).into(holder.docRowImg);
        holder.startAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, QRCodeScanner.class);
                intent.putExtra("To",model.getName());
                intent.putExtra("ToAuthId",model.getAuthId());
                intent.putExtra("entMan","YES");
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.viewDoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DoctorProfile.class);
                intent.putExtra("authId",model.getAuthId());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public doctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor_folder,parent,false);
        return new doctorViewHolder(view);
    }

    class doctorViewHolder extends RecyclerView.ViewHolder{
        TextView listDocName,listDocHospital;
        Button startAppointment,viewDoProfile;
        CircleImageView docRowImg;

        public doctorViewHolder(@NonNull View itemView) {
            super(itemView);
            listDocName=itemView.findViewById(R.id.list_doc_name);
            listDocHospital=itemView.findViewById(R.id.list_doc_hospital);
            startAppointment=itemView.findViewById(R.id.start_doc_appointment);
            viewDoProfile=itemView.findViewById(R.id.view_doc_profile);
            docRowImg=itemView.findViewById(R.id.doc_list_img);
        }
    }
}
