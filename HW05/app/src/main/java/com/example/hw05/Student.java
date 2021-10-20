package com.example.hw05;

public class Student {
    private String studentID;
    private String name;
    private String classID;
    private float avgScore;
    private int thumbnailID;

    public Student() {}

    public Student(String studentID, String name, String classID, float avgScore, int thumbnailID) {
        this.studentID = studentID;
        this.name = name;
        this.classID = classID;
        this.avgScore = avgScore;
        this.thumbnailID = thumbnailID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public float getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(float avgScore) {
        this.avgScore = avgScore;
    }

    public int getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(int thumbnailID) {
        this.thumbnailID = thumbnailID;
    }
}

