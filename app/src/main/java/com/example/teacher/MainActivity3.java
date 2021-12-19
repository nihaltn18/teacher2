package com.example.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
    String date,classcode;
    ArrayList<Student> students;
    ClassObj obj;
    RecyclerView recyclerView;
    StudentAdapter adapter;
    ArrayList<String> studentsnamelist;
    ArrayList<Boolean> attendancelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        classcode = intent.getStringExtra("classcode");
        studentsnamelist = new ArrayList<>();
        attendancelist = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity3.this));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(classcode);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                obj = snapshot.getValue(ClassObj.class);
                students = obj.getStudents();
                for(Student student:students)
                {
                    studentsnamelist.add(student.getName());
                    attendancelist.add(false);
                }
                adapter = new StudentAdapter(studentsnamelist,attendancelist,MainActivity3.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //(TODO) what to do when clicked on submit attendance
        switch (item.getItemId())
        {
            case R.id.done:
                SubmitAttendance();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void SubmitAttendance()
    {
        attendancelist = adapter.getAttendance();
        studentsnamelist = adapter.getStudents();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(classcode);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(classcode).child("students");

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                ClassObj obj = snapshot.getValue(ClassObj.class);
                ArrayList<Student> students = obj.getStudents();
                Student student;
                for(int i =0;i<studentsnamelist.size();i++)
                {
                    student = students.get(i);
                    student.date.add(date);
                    student.attendance.add(attendancelist.get(i));
                    if(student.getName()==studentsnamelist.get(i))
                    {
                        reference2.child(Integer.toString(i)).setValue(student);
                    }
                }
            }
        });
        Toast.makeText(MainActivity3.this,"Attendance Done for the day "+date,Toast.LENGTH_LONG).show();
        startActivity(new Intent(MainActivity3.this,MainActivity2.class));
    }
}