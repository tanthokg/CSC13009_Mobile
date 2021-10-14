package com.example.homework05;

public class User {
    private String name;
    private String studentID;
    private String studentClass;
    private int thumbnailID;
    private float avg;

    public User() {
        // Do nothing here
    }

    public User(String name, String studentClass, String studentID, int thumbnailID, float avg) {
        this.name = name;
        this.studentID = studentID;
        this.thumbnailID = thumbnailID;
        this.studentClass = studentClass;
        this.avg = avg;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public int getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(int thumbnailID) {
        this.thumbnailID = thumbnailID;
    }


}
