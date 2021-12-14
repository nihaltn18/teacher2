package com.example.teacher;
import java.util.ArrayList;
public class Student{
    String name;
    ArrayList<String> date;
    ArrayList<Boolean> attendance;
    public Student(int i,String name)
    {
        date = new ArrayList<>();
        attendance = new ArrayList<>();
        date.add("23/06/2001");
        attendance.add(true);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<Boolean> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<Boolean> attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", attendance=" + attendance +
                '}';
    }

    public Student() {
    }
}
