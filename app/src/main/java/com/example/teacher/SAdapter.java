package com.example.teacher;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class SAdapter extends RecyclerView.Adapter<SAdapter.ViewHolder> {
    Context context;
    ClassObj obj;
    ArrayList<Student> students;

    public SAdapter(Context context, ClassObj obj) {
        this.context = context;
        this.obj = obj;
        students = obj.getStudents();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ClassObj getObj() {
        return obj;
    }

    public void setObj(ClassObj obj) {
        this.obj = obj;
    }

    @NonNull
    @Override
    public SAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studadapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SAdapter.ViewHolder holder, int position) {
        holder.name.setText(students.get(position).getName());
        ArrayList<Boolean> attendance = students.get(position).getAttendance();
        int tot = attendance.size()-1;
        int present = Collections.frequency(attendance,true)-1;
        double per=((double)present/tot);
        if(present==0)
        {
            holder.percentage.setText("0.0");
        }
        else
        {
            holder.percentage.setText(String.format("%.2f", per * 100));
        }
    }

    @Override
    public int getItemCount() {
        return obj.getStudents().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,percentage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            percentage = itemView.findViewById(R.id.percentage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,MainActivity5.class);
            ArrayList<String> attendance = new ArrayList<>();
            ArrayList<Boolean> boolattendance= obj.getStudents().get(getAdapterPosition()).getAttendance();
            for(Boolean b:boolattendance)
            {
                if(b==true)
                {
                    attendance.add("Present");
                }
                else
                {
                    attendance.add("Absent");
                }
            }
            ArrayList<String> date = obj.getStudents().get(getAdapterPosition()).getDate();
            intent.putStringArrayListExtra("date",date);
            intent.putStringArrayListExtra("attendance",attendance);
            intent.putExtra("name",obj.getStudents().get(getAdapterPosition()).getName());
            context.startActivity(intent);

        }
    }
}
