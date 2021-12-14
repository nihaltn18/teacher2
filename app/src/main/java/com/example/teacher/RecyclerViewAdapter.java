package com.example.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<String> name;
    ArrayList<String> code;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView,codeView;
        int pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.classname2);
            codeView = itemView.findViewById(R.id.classcode2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //what to do when clicked on particular item
            Intent intent = new Intent(context,MainActivity3.class);
            intent.putExtra("classcode",code.get(this.getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
