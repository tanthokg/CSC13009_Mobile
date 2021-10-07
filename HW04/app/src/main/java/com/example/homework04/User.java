package com.example.homework04;

public class User {
    private String name;
    private String studentID;
    private int thumbnailID;

    public User() {
        // Do nothing here
    }

    public User(String name, String studentID, int thumbnailID) {
        this.name = name;
        this.studentID = studentID;
        this.thumbnailID = thumbnailID;
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
