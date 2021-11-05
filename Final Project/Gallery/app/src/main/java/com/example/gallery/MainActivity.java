package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private final int[] images = {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView galleryRecView = findViewById(R.id.galleryRecView);
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, images);
        galleryRecView.setAdapter(galleryAdapter);
        galleryRecView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}