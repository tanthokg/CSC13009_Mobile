package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FilenameFilter;

public class LargeImage extends AppCompatActivity {
    ImageView largeImage;
    Button btnPrev, btnNext;
    File pictureFile;
    File[] pictureFiles;
    int[] currentPosition ;


    private void deleteOn(String path)
    {
        File a = new File(path);
        a.delete();
       // largeImage.setImageResource(-1);
        //largeImage.setImageResource(android.R.color.transparent);
        //largeImag.setImageDrawable(null);

        callScanItent(getApplicationContext(),path);
        Toast.makeText(this,"image deleted",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void  callScanItent(Context context,String path) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item);

        Intent intent = getIntent();
        largeImage = findViewById(R.id.largeGalleryItem);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        // Create a File object from the received path
        pictureFile = new File(intent.getStringExtra("pathToPicturesFolder"));
        // Create an array contains all files in the folder above
        pictureFiles = pictureFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith("png") || s.endsWith("jpg");
            }
        });
        // Get current position from intent
        // It has to be final int[] when used in anonymous functions, otherwise it will cause errors
        currentPosition = new int[]{intent.getIntExtra("itemPosition", -1)};
        if (pictureFiles != null) {
            // Set image for widget
            largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[currentPosition[0]].getAbsolutePath()));
            updateButton(currentPosition[0]);
        }

        // TODO: make sure the range does not exceed the item count in folder
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureFiles != null) {
                    int position = --currentPosition[0];
                    largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[position].getAbsolutePath()));
                    updateButton(position);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (pictureFiles != null) {
                    int position = ++currentPosition[0];
                    largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[position].getAbsolutePath()));
                    updateButton(position);
                }
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                case R.id.deleteAlbum:
                        String path=pictureFiles[currentPosition[0]].getAbsolutePath();



              deleteOn(path);
                    break;

            }
            // Use addToBackStack to return the previous fragment when the Back button is pressed
            // Checking null was just a precaution
            if (selectedFragment != null)
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHolder, selectedFragment)
                        .commit();
            return true;
        });
    }

    void updateButton(int currentPosition)
    {
        btnPrev.setEnabled(0 != currentPosition);
        btnNext.setEnabled((pictureFiles.length - 1) != currentPosition);
    }
}
