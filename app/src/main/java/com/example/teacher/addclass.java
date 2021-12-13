package com.example.teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addclass extends AppCompatActivity{

    Button add;
    EditText classcode,classname;
    ArrayList<String> codelist, teachercodelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);
        codelist = new ArrayList<String>();
        teachercodelist = new ArrayList<>();
        add = findViewById(R.id.add);
        classcode = findViewById(R.id.classcode);
        classname = findViewById(R.id.classname);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference = reference.child("classcode");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    codelist.add(snapshot1.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("msg", "onCancelled() returned: " + error.toString());
            }
        });
        FirebaseDatabase.getInstance().getReference().child("teachercode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    teachercodelist.add(snapshot1.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("msg", "onCreate() returned: " + codelist.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new class

                if(codelist.contains(classcode.getText().toString()))
                {
                    //class already exist
                    Log.d(TAG, "onClick() returned: " + "cant add");
                    Toast.makeText(addclass.this, "ClassObj already exist", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //class can be created
                    Log.d(TAG, "onClick() returned: " + "added");
                    ClassObj cls = new ClassObj(classname.getText().toString(),classcode.getText().toString());
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    reference1 = reference1.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference1 = reference1.child(classcode.getText().toString());

                    //add some info such as ClassObj Object or Other details into node reference1
                    reference1.setValue(cls);

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("classcode");
                    codelist.add(classcode.getText().toString());
                    teachercodelist.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference2.setValue(codelist);

                    FirebaseDatabase.getInstance().getReference().child("teachercode").setValue(teachercodelist);
                }
                startActivity(new Intent(addclass.this,MainActivity2.class));
            }
        });

    }
}