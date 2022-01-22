package com.example.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    RecyclerView recyclerView;
    Attendance adapter;
    ArrayList<String> date,attendance;
    String name,classCode;
    int spos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Toast toast = new Toast(MainActivity5.this);
        View view = getLayoutInflater().inflate(R.layout.attendance_toast,findViewById(R.id.attend));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity5.this));
        Intent intent = getIntent();
        date = intent.getStringArrayListExtra("date");
        name = intent.getStringExtra("name");
        classCode = intent.getStringExtra("code");
        spos = intent.getIntExtra("pos",0);
        getSupportActionBar().setTitle(name);
        if(date!=null)
        {
            attendance = intent.getStringArrayListExtra("attendance");
            adapter = new Attendance(date,attendance,MainActivity5.this,classCode,spos);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}