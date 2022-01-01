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
import java.util.Collections;

public class SAdapter extends RecyclerView.Adapter<SAdapter.ViewHolder> {
    Context context;
    ClassObj obj;
    ArrayList<Student> students;
    String classCode;
    int pos;

    public SAdapter(Context context, ClassObj obj,String className) {
        this.context = context;
        this.classCode = className;
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
        if(position==0)
        {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Remove Student?!!");
                    builder.setMessage("Do You Really Want To Remove "+students.get(getAdapterPosition()).getName()+" From This Class?!!");
                    builder.setNegativeButton("NO!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<Student> arrayList = obj.getStudents();
                            Student student = arrayList.remove(getAdapterPosition());
                            ArrayList<String> arrayList1 = new ArrayList<>();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(student.getStudentCode()+"x");
                            reference.child("classCodes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    arrayList1.clear();
                                    DataSnapshot snapshot = task.getResult();
                                    for (DataSnapshot snapshot1:snapshot.getChildren())
                                    {
                                        arrayList1.add(snapshot1.getValue(String.class));
                                    }
                                    pos = arrayList1.indexOf(classCode);
                                    arrayList1.remove(pos);
                                    reference.child("classCodes").setValue(arrayList1);
                                }
                            });
                            reference.child("classNames").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    arrayList1.clear();
                                    DataSnapshot snapshot = task.getResult();
                                    for (DataSnapshot snapshot1:snapshot.getChildren())
                                    {
                                        arrayList1.add(snapshot1.getValue(String.class));
                                    }
                                    arrayList1.remove(pos);
                                    reference.child("classNames").setValue(arrayList1);
                                }
                            });
                            reference.child("teacherCodes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    arrayList1.clear();
                                    DataSnapshot snapshot = task.getResult();
                                    for (DataSnapshot snapshot1:snapshot.getChildren())
                                    {
                                        arrayList1.add(snapshot1.getValue(String.class));
                                    }
                                    arrayList1.remove(pos);
                                    reference.child("teacherCodes").setValue(arrayList1);
                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(classCode).child("students").setValue(arrayList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    context.startActivity(new Intent(context,MainActivity2
                                            .class));
                                }
                            });
                        }
                    });
                    builder.show();
                    return true;
                }
            });
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
