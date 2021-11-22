package com.example.homework08;

public class Student {
    private String studentID;
    private String name;
    private int classID;

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    private float avgScore;
    private int thumbnailID;

    public Student() {}

    public Student(String studentID, String name, int classID, float avgScore) {
        this.studentID = studentID;
        this.name = name;
        this.classID = classID;
        this.avgScore = avgScore;
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

