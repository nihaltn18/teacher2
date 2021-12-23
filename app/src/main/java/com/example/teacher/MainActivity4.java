package com.example.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity {

    RecyclerView recyclerView;
    SAdapter adapter;
    ClassObj obj;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity4.this));

        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(code).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                obj = task.getResult().getValue(ClassObj.class);
                if(obj.getStudents().get(0).getName().equals("blahblahblah"))
                {
                    obj.students.remove(0);
                }
                adapter = new SAdapter(MainActivity4.this,obj);
                recyclerView.setAdapter(adapter);
                getSupportActionBar().setTitle(obj.getClass_name());
                adapter.notifyDataSetChanged();
            }
        });

    }
}