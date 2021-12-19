package com.example.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    RecyclerView recyclerView;
    Attendance adapter;
    ArrayList<String> date,attendance;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity5.this));
        Intent intent = getIntent();
        date = intent.getStringArrayListExtra("date");
        name = intent.getStringExtra("name");
        if(date!=null)
        {
            attendance = intent.getStringArrayListExtra("attendance");
            adapter = new Attendance(date,attendance,MainActivity5.this);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}