package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class AlbumUtility {
    private SharedPreferences sharedPreferences;
    private static AlbumUtility instance;
    private static final String ALL_ALBUM_KEY = "album_list";
    private static final String ALL_ALBUM_DATA_KEY = "album_data";

    private AlbumUtility(Context context) {
        sharedPreferences = context.getSharedPreferences("albums_database", Context.MODE_PRIVATE);
        if (getAllAlbums() == null) {
            initAlbums();
        }
        if (getAllAlbumData() == null) {
            initAlbumData();
        }
    }

    public static AlbumUtility getInstance(Context context) {
        if (null == instance)
            instance = new AlbumUtility(context);
        return instance;
    }

    public ArrayList<String> getAllAlbums() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_KEY, null), type);
    }

    public ArrayList<AlbumData> getAllAlbumData() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AlbumData>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_DATA_KEY, null), type);
    }

    public void setAllAlbums(ArrayList<String> albums) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ALL_ALBUM_KEY);
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.apply();
    }

    public void setAllAlbumData(ArrayList<AlbumData> data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ALL_ALBUM_DATA_KEY);
        editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(data));
        editor.apply();
    }

    private void initAlbums() {
        ArrayList<String> albums = new ArrayList<String>();
        albums.add("Favorite");
        albums.add("Cats");
        albums.add("Dogs");
        albums.add("Food");
        albums.add("Holiday");
        albums.add("Parties");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.apply();
    }

    private void initAlbumData() {
        ArrayList<AlbumData> albumData = new ArrayList<AlbumData>();
        albumData.add(new AlbumData("Favorite", new ArrayList<String>()));
        albumData.add(new AlbumData("Cats", new ArrayList<String>()));
        albumData.add(new AlbumData("Dogs", new ArrayList<String>()));
        albumData.add(new AlbumData("Food", new ArrayList<String>()));
        albumData.add(new AlbumData("Holiday", new ArrayList<String>()));
        albumData.add(new AlbumData("Parties", new ArrayList<String>()));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(albumData));
        editor.apply();
    }

    public boolean addNewAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        ArrayList<AlbumData> data = getAllAlbumData();
        if (albums != null && data != null)
            if (albums.add(albumName) && data.add(new AlbumData(albumName, new ArrayList<String>()))) {
                setAllAlbums(albums);
                setAllAlbumData(data);
                return true;
            }
        return false;
    }

    public boolean addPictureToAlbum(String albumName, String picturePath) {
        ArrayList<AlbumData> data = getAllAlbumData();
        if (null != data) {
            AlbumData selectedAlbum = findDataByAlbumName(albumName);
            if (selectedAlbum != null) {
                if (selectedAlbum.addNewPath(picturePath)){
                    data.removeIf(d -> d.getAlbumName().equals(selectedAlbum.getAlbumName()));
                    data.add(selectedAlbum);
                }
            }
            setAllAlbumData(data);
            return true;
        }
        return false;
    }

    public boolean deleteAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        ArrayList<AlbumData> data = getAllAlbumData();
        if (albums != null && data != null)
            for (String album: albums)
                if (album.equals(albumName))
                    if (albums.remove(album)) {
                        data.removeIf(d->d.getAlbumName().equals(albumName));
                        setAllAlbums(albums);
                        setAllAlbumData(data);
                        return true;
                    }
        return false;
    }

    public boolean deletePictureInAlbum(String albumName, String picturePath) {
        // Get all album data
        ArrayList<AlbumData> data = getAllAlbumData();
        // Get AlbumData object matching the name
        AlbumData albumData = findDataByAlbumName(albumName);
        if (albumData != null) {
            // Remove required path in AlbumData object
            ArrayList<String> paths = albumData.getPicturePaths();
            paths.removeIf(s -> s.equals(picturePath));
            // Set new paths for AlbumData object
            albumData.setPicturePaths(paths);
            // Remove that AlbumData in total album data
            data.removeIf(d -> d.getAlbumName().equals(albumName));
            // Add modified AlbumData object to data
            data.add(albumData);

            // Apply changes to shared preferences
            setAllAlbumData(data);
            return true;
        }
        return false;
    }

    public boolean deletePictureInAllAlbums(String picturePath) {
        ArrayList<AlbumData> data = getAllAlbumData();
        if (data != null) {
            for(AlbumData d : data) {
                ArrayList<String> paths = d.getPicturePaths();
                paths.removeIf(path -> path.equals(picturePath));
                d.setPicturePaths(paths);
            }
            setAllAlbumData(data);
            return true;
        }
        return false;
    }

    public AlbumData findDataByAlbumName(String albumName) {
        ArrayList<AlbumData> data = getAllAlbumData();
        if (null != data)
            for (AlbumData d : data)
                if (d.getAlbumName().equals(albumName))
                    return d;
        return null;
    }
}
