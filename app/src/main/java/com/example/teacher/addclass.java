package com.example.teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<String> codelist, teachercodelist,classnames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);
        codelist = new ArrayList<String>();
        teachercodelist = new ArrayList<>();
        classnames = new ArrayList<>();
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
        FirebaseDatabase.getInstance().getReference().child("classname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    classnames.add(snapshot1.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("msg", "onCreate() returned: " + codelist.toString());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new class
                if(!(classcode.getText().toString().equals(""))&&!(classname.getText().toString().equals("")))
                {
                    if (codelist.contains(classcode.getText().toString())) {
                        //class already exist
                        Log.d(TAG, "onClick() returned: " + "cant add");
                        Toast.makeText(addclass.this, "Class already exist", Toast.LENGTH_LONG).show();
                    } else {
                        //class can be created
                        Log.d(TAG, "onClick() returned: " + "added");
                        ClassObj cls = new ClassObj(classname.getText().toString(), classcode.getText().toString());
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                        reference1 = reference1.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference1 = reference1.child(classcode.getText().toString());

                        //add some info such as ClassObj Object or Other details into node reference1
                        reference1.setValue(cls);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("classcode");
                        codelist.add(classcode.getText().toString());
                        teachercodelist.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        classnames.add(classname.getText().toString());
                        reference2.setValue(codelist);
                        FirebaseDatabase.getInstance().getReference().child("teachercode").setValue(teachercodelist);
                        FirebaseDatabase.getInstance().getReference().child("classname").setValue(classnames);
                        View inflater = getLayoutInflater().inflate(R.layout.success_toast, (ViewGroup) findViewById(R.id.layout2));
                        Toast toast = new Toast(addclass.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(inflater);
                        toast.show();

                    }
                    startActivity(new Intent(addclass.this, MainActivity2.class));
                }
                else
                {
                    Toast.makeText(addclass.this,"None Of The Fields Can Be Empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}