package com.example.teacher;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Attendance extends RecyclerView.Adapter<Attendance.ViewHolder> {
    ArrayList<String> date,attended;
    Context context;

    public Attendance(ArrayList<String> date, ArrayList<String> attended,Context context) {
        this.date = date;
        this.attended = attended;
        this.context = context;
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
        }
    }
}

