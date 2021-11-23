package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

<<<<<<< Updated upstream
=======
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
>>>>>>> Stashed changes
import android.content.Context;
import android.content.Intent;
<<<<<<< Updated upstream
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
=======
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Build;
>>>>>>> Stashed changes
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FilenameFilter;
<<<<<<< Updated upstream
=======
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
>>>>>>> Stashed changes

public class LargeImage extends AppCompatActivity {
    ImageView largeImage;
    Button btnPrev, btnNext;
    File pictureFile;
    File[] pictureFiles;
    int[] currentPosition ;

<<<<<<< Updated upstream
=======
    WallpaperManager wallpaperManager;
    private ScaleGestureDetector scaleGestureDetector;
    //we are defining our scale factor.
    private float mScaleFactor = 1.0f;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
=======
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
=======
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.deleteAlbum) {
                    String path = pictureFiles[currentPosition[0]].getAbsolutePath();
                    LargeImage.this.deleteOnPath(path, bottomNavigationView);
                }

                // Use addToBackStack to return the previous fragment when the Back button is pressed
                // Checking null was just a precaution
                if (selectedFragment != null)
                    LargeImage.this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentHolder, selectedFragment)
                            .commit();
                return true;
            }
        });
    }

    void updateButton(int currentPosition)
    {
        btnPrev.setEnabled(0 != currentPosition);
        btnNext.setEnabled((pictureFiles.length - 1) != currentPosition);
    }

    private void deleteOnPath(String path, View bottomNav)
    {
        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(bottomNav.getContext(), R.style.AlertDialog);
        confirmDialog.setMessage("Are you sure to delete this image?");
        confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File a = new File(path);
                a.delete();
                callScanIntent(getApplicationContext(),path);
                Toast.makeText(getApplicationContext(),"Image Deleted",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        confirmDialog.create();
        confirmDialog.show();
    }

    public void  callScanIntent(Context context, String path) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.large_image_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menu_SetWallpaper) {
            // TODO: set image as wallpaper here
            try {
                // set the wallpaper by calling the setResource function and
                // passing the drawable file
                //Glide.with(this).asBitmap().load(pictureFiles[currentPosition[0]].getAbsolutePath())
                wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(),largeImage.getHeight()));
            } catch (IOException e) {
                // here the errors can be logged instead of printStackTrace
                e.printStackTrace();
            }
            Toast.makeText(this, "Set as Wallpaper", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_SetLockscreen) {
            // TODO: set image as lockscreen here
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(),largeImage.getHeight()), null, true, WallpaperManager.FLAG_LOCK); //For Lock screen
                    Toast.makeText(this, "Set as Lockscreen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lock screen walpaper not supported", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (item.getItemId() == R.id.menu_ViewInfo) {
            // TODO: show image info here
            Toast.makeText(this, "View Info", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_AddToAlbum) {
            addPictureToAlbum();
            Toast.makeText(this, albumsFragment.getAlbums().size() + " item(s).", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
>>>>>>> Stashed changes

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

    public static Bitmap viewToBitmap(View view,int width,int height){
        Bitmap bm=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bm);
        view.draw(canvas);
        return bm;
    }
}
