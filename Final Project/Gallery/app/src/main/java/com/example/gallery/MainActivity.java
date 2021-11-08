package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    /*private final int[] images = {
        R.drawable.avatar01,
                R.drawable.avatar02,
                R.drawable.avatar03,
                R.drawable.avatar04,
                R.drawable.avatar05,
                R.drawable.avatar06,
                R.drawable.avatar07,
                R.drawable.avatar08,
                R.drawable.avatar09,
                R.drawable.avatar10,
                R.drawable.avatar11,
                R.drawable.avatar12,
                R.drawable.avatar13,
                R.drawable.avatar14,
                R.drawable.avatar15,
                R.drawable.avatar16,
                R.drawable.avatar17,
                R.drawable.avatar18,
                R.drawable.avatar19,
                R.drawable.avatar20
    };*/
    RecyclerView galleryRecView;
    TextView txtMsg;
    File[] pictureFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        txtMsg = findViewById(R.id.txtMsg);

    }

    void readPicturesFolder() {
        try {
            // Get path to external storage: /storage/emulated/0
            String absolutePathToSDCard = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Path to Pictures folder: /storage/emulated/0/Pictures/
            String pathToPicturesFolder = absolutePathToSDCard + "/Pictures/";
            txtMsg.append("Path: " + pathToPicturesFolder + "\n");
            File pictureFile = new File(pathToPicturesFolder);
            pictureFiles = pictureFile.listFiles();
            txtMsg.append( "Exist: " + pictureFile.exists() + ". Is Directory: " + pictureFile.isDirectory()
                    + ". Can Read: " + pictureFile.canRead() + "\n");
            if (pictureFiles == null)
                txtMsg.append("NULL");
            else {
                txtMsg.append("File Count: " + pictureFiles.length + "\n");
                // Load gallery with current path
                loadGallery(pathToPicturesFolder);
            }

        }
        catch (Exception e) {
            txtMsg.append(e.getMessage());
        }
    }

    void loadGallery(String pathToPicturesFolder) {
        // The idea was to send a string path to the adapter, not a File object
        // The adapter will then create everything we need from the provided path
        // This implementation is my personal idea only, it is not permanent
        galleryRecView = findViewById(R.id.galleryRecView);
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, pathToPicturesFolder);
        galleryRecView.setAdapter(galleryAdapter);
        galleryRecView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readPicturesFolder();
            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}