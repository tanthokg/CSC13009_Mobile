package com.example.gallery;

import java.util.ArrayList;

public class AlbumData {
    private String albumName;
    private ArrayList<String> picturePaths;

    public AlbumData(String albumName, ArrayList<String> picturePath) {
        this.albumName = albumName;
        this.picturePaths = picturePath;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<String> getPicturePaths() {
        return picturePaths;
    }

    public void setPicturePaths(ArrayList<String> picturePaths) {
        this.picturePaths = picturePaths;
    }
}
