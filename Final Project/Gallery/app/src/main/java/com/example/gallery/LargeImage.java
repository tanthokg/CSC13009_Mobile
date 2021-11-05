package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class LargeImage extends AppCompatActivity {
    ImageView largeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item);
        Intent intent = getIntent();
        largeImage = findViewById(R.id.largeGalleryItem);
        largeImage.setImageResource(intent.getIntExtra("imageResId", 0));
    }
}