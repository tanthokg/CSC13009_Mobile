package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class LargeImage extends AppCompatActivity {
    ImageView largeImage;
    Button btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item);

        Intent intent = getIntent();
        largeImage = findViewById(R.id.largeGalleryItem);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        // Create a File object from the received path
        File pictureFile = new File(intent.getStringExtra("pathToPicturesFolder"));
        // Create an array contains all files in the folder above
        File[] pictureFiles = pictureFile.listFiles();
        // Get current position from intent
        // It has to be final int[] when used in anonymous functions, otherwise it will cause errors
        final int[] currentPosition = {intent.getIntExtra("itemPosition", -1)};
        if (pictureFiles != null) {
            // Set image for widget
            largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[currentPosition[0]].getAbsolutePath()));
        }

        // TODO: make sure the range does not exceed the item count in folder
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureFiles != null)
                    largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[--currentPosition[0]].getAbsolutePath()));
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureFiles != null)
                    largeImage.setImageDrawable(Drawable.createFromPath(pictureFiles[++currentPosition[0]].getAbsolutePath()));
            }
        });
    }
}