package com.example.teacher;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.SnapshotHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<String> name;
    ArrayList<String> code;
    ArrayList<String> tcode2,code2,name2;
    ClassObj obj = new ClassObj();
    int pos;

    public RecyclerViewAdapter(Context context, ArrayList<String> name, ArrayList<String> code) {
        this.context = context;
        this.name = name;
        this.code = code;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String name3 = name.get(position),code3 = code.get(position);
        holder.nameView.setText(name3);
        holder.codeView.setText(code3);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public void filterList(ArrayList<String> fcode, ArrayList<String> fname) {
        name = fname;
        code = fcode;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView,codeView;
        int pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.classname2);
            codeView = itemView.findViewById(R.id.classcode2);
            pos = this.getAdapterPosition();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do You Really Want To Remove "+code.get(getAdapterPosition())+" From Your Class List?");
                    builder.setTitle("Remove Class?!!");
                    builder.setNegativeButton("NO!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("YES!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<ClassObj> arrayList = new ArrayList<>();
                            tcode2 = new ArrayList<>();
                            code2 = new ArrayList<>();
                            name2 = new ArrayList<>();
                            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(code.get(getAdapterPosition())).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("classcode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    DataSnapshot snapshot1 = task.getResult();
                                    for (DataSnapshot snapshot2:snapshot1.getChildren())
                                    {
                                        code2.add(snapshot2.getValue(String.class));
                                    }
                                    pos = code2.indexOf(codeView.getText().toString());
                                    code2.remove(pos);
                                    FirebaseDatabase.getInstance().getReference().child("classcode").setValue(code2);
                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("classname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    DataSnapshot snapshot1 = task.getResult();
                                    for (DataSnapshot snapshot2:snapshot1.getChildren())
                                    {
                                        name2.add(snapshot2.getValue(String.class));
                                    }
                                    name2.remove(pos);
                                    FirebaseDatabase.getInstance().getReference().child("classname").setValue(name2);
                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("teachercode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    DataSnapshot snapshot1 = task.getResult();
                                    for (DataSnapshot snapshot2:snapshot1.getChildren())
                                    {
                                        tcode2.add(snapshot2.getValue(String.class));
                                    }
                                    tcode2.remove(pos);
                                    FirebaseDatabase.getInstance().getReference().child("teachercode").setValue(tcode2).onSuccessTask(new SuccessContinuation<Void, Object>() {
                                        @NonNull
                                        @Override
                                        public Task<Object> then(Void unused) throws Exception {
                                            context.startActivity(new Intent(context,MainActivity2.class));
                                            return null;
                                        }
                                    });
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
            //what to do when clicked on particular item
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Intent intent = new Intent(context,MainActivity3.class);
                    intent.putExtra("date",Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year));
                    intent.putExtra("classcode",code.get(getAdapterPosition()));
                    intent.putExtra("name",name.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }
}
