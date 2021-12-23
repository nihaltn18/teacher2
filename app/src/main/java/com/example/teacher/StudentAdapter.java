package com.example.teacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    ArrayList<String> students;
    ArrayList<Boolean> attendance;
    Context context;

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public ArrayList<Boolean> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<Boolean> attendance) {
        this.attendance = attendance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public StudentAdapter(ArrayList<String> students, ArrayList<Boolean> attendance, Context context) {
        this.students = students;
        this.attendance = attendance;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlestudent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.rollnumber.setText(Integer.toString(position));
            holder.name.setText(students.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkBox.isChecked())
                    {
                        //(TODO) when clicked on unchecked checkbox
                        attendance.set(position,true);
                    }
                    else
                    {
                        //(TODO) when clicked on checked checkbox
                        attendance.set(position,false);
                    }
                }
            });
        if(position==0)
        {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rollnumber,name;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            rollnumber = itemView.findViewById(R.id.rollnumber);
            checkBox = itemView.findViewById(R.id.attended);
            name = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            checkBox.callOnClick();
        }
    }
}
