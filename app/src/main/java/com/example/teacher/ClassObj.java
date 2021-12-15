package com.example.teacher;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class ClassObj {
    String Class_name;
    String Class_code;
    ArrayList<Student> students;

    public ClassObj(String class_name, String class_code){
        Class_name = class_name;
        Class_code = class_code;
        students = new ArrayList<>();
        students.add(new Student(0,"nihal", FirebaseAuth.getInstance().getCurrentUser().getUid()));
    }
    @Override
    public String toString() {
        return "ClassObj{" +
                ", Class_name='" + Class_name + '\'' +
                ", Class_code='" + Class_code + '\'' +
                ", list=" + students +
                '}';
    }
    public String getClass_code() {
        return Class_code;
    }
    public void setClass_code(String class_code) {
        Class_code = class_code;
    }
    public String getClass_name() {
        return Class_name;
    }
    public void setClass_name(String class_name) {
        Class_name = class_name;
    }
    public ArrayList<Student> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    public ClassObj() {
    }
}
