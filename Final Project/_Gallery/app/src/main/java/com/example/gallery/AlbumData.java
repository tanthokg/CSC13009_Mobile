package com.example.gallery;

public class AlbumData {
    private String albumName;
    private String picturePath;

    public AlbumData(String albumName, String picturePath) {
        this.albumName = albumName;
        this.picturePath = picturePath;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
