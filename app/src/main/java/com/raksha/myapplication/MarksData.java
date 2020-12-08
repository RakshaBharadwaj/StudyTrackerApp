package com.raksha.myapplication;

public class MarksData {

    String date;
    int marks;

    public MarksData(String date, int marks) {
        this.date = date;
        this.marks = marks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
