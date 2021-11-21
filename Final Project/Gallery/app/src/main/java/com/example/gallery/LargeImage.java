package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

public class LargeImage extends AppCompatActivity {
    ImageView largeImage;
    Button btnPrev, btnNext;
    File pictureFile;
    File[] pictureFiles;
    int[] currentPosition ;

    private ScaleGestureDetector scaleGestureDetector;
    //we are defining our scale factor.
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        largeImage = findViewById(R.id.largeGalleryItem);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // Create a File object from the received path
        pictureFile = new File(intent.getStringExtra("pathToPicturesFolder"));
        // Create an array contains all files in the folder above
        pictureFiles = pictureFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase(Locale.ROOT).endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
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
            if (item.getItemId() == R.id.deleteAlbum) {
                String path = pictureFiles[currentPosition[0]].getAbsolutePath();
                deleteOn(path);
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

    private void deleteOn(String path)
    {
        File a = new File(path);
        a.delete();
        callScanIntent(getApplicationContext(),path);
        Toast.makeText(this,"Image Deleted",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void  callScanIntent(Context context,String path) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // inside on touch event method we are calling on
        // touch event method and passing our motion event to it.
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            // inside on scale method we are setting scale
            // for our image in our image view.
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            // on below line we are setting scale x and scale y to our image view.
            largeImage.setScaleX(mScaleFactor);
            largeImage.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}
