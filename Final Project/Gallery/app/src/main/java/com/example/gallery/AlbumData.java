package com.example.gallery;

import java.util.ArrayList;

public class AlbumData {
    private String albumName;
    private ArrayList<String> picturePaths;

    public AlbumData(String albumName, ArrayList<String> picturePaths) {
        this.albumName = albumName;
        this.picturePaths = picturePaths;
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

    public boolean addNewPath(String newPath) {
        // To avoid duplications in album data
        for (String p: picturePaths) {
            if (p.equals(newPath))
                return false;
        }
        picturePaths.add(newPath);
        return true;
    }
}
