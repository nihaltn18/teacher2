package com.example.teacher;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Attendance extends RecyclerView.Adapter<Attendance.ViewHolder> {
    ArrayList<String> date,attended;
    String classCode;
    Context context;
    int spos;

    public Attendance(ArrayList<String> date, ArrayList<String> attended,Context context,String code,int spos) {
        this.date = date;
        this.attended = attended;
        this.context = context;
        classCode = code;
        this.spos = spos;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<String> getAttended() {
        return attended;
    }

    public void setAttended(ArrayList<String> attended) {
        this.attended = attended;
    }

    @NonNull
    @Override
    public Attendance.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Attendance.ViewHolder holder, int position) {
        holder.date.setText(date.get(position));
        holder.attended.setText(attended.get(position));
        if(position==0)
        {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return attended.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,attended;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            attended = itemView.findViewById(R.id.attended);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("change the attendance status ?");
                    builder.setMessage("Do want to change attendance status of the student for the date "+date.getText().toString());
                    builder.setNegativeButton("No!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Yes!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(classCode).child("students").child(Integer.toString(spos)).child("attendance").child(Integer.toString(getAdapterPosition()));
                            reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    DataSnapshot snapshot = task.getResult();
                                    boolean status = snapshot.getValue(Boolean.class);
                                    if(status)
                                    {
                                        reference.setValue(false);
                                    }
                                    else
                                    {
                                        reference.setValue(true);
                                    }
                                    context.startActivity(new Intent(context,MainActivity2.class));
                                }
                            });
                        }
                    });

                    builder.show();
                    return true;
                }
            });
        }
    }
}

