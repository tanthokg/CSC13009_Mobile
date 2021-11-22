package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

public class LargeImage extends AppCompatActivity {
    private ImageView largeImage;
    private Button btnPrev, btnNext;
    private File pictureFile;
    private File[] pictureFiles;
    private int[] currentPosition ;

    private ScaleGestureDetector scaleGestureDetector;
    //we are defining our scale factor.
    private float mScaleFactor = 1.0f;

    private AlbumsFragment albumsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        largeImage = findViewById(R.id.largeGalleryItem);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        albumsFragment = new AlbumsFragment(this);
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
        // It must be final int[] when used in anonymous functions, otherwise it will cause errors
        currentPosition = new int[]{intent.getIntExtra("itemPosition", -1)};
        if (pictureFiles != null) {
            // Set image for widget
            Glide.with(this).asBitmap().load(pictureFiles[currentPosition[0]].getAbsolutePath()).into(largeImage);
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
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.deleteAlbum) {
                    String path = pictureFiles[currentPosition[0]].getAbsolutePath();
                    LargeImage.this.deleteOnPath(path);
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

    private void deleteOnPath(String path)
    {
        File a = new File(path);
        a.delete();
        callScanIntent(getApplicationContext(),path);
        Toast.makeText(this,"Image Deleted",Toast.LENGTH_SHORT).show();
        finish();
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
            Toast.makeText(this, "Set as Wallpaper", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_SetLockscreen) {
            // TODO: set image as lockscreen here
            Toast.makeText(this, "Set as Lockscreen", Toast.LENGTH_SHORT).show();
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

    private void addPictureToAlbum() {
        View addToAlbumView = LayoutInflater.from(this).inflate(R.layout.choose_album_form, null);
        ListView chooseAlbumListView = addToAlbumView.findViewById(R.id.chooseAlbumListView);
        ArrayList<String> albums = albumsFragment.getAlbums();
        ArrayAdapter<String> albumDefaultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albums);
        chooseAlbumListView.setAdapter(albumDefaultAdapter);
        chooseAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LargeImage.this, albums.get(i) + " chosen", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder addToAlbumDialog = new AlertDialog.Builder(this);
        addToAlbumDialog.setView(addToAlbumView);

        addToAlbumDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        addToAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        addToAlbumDialog.create();
        addToAlbumDialog.show();
    }
    private void changeTheme(boolean isChecked) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_Gallery_);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Gallery);
        }
    }

    private boolean checkTheme() {
        SharedPreferences preferencesContainer = getSharedPreferences("app theme", Activity.MODE_PRIVATE);
        boolean theme = false;
        if (preferencesContainer != null && preferencesContainer.contains("dark mode"))
            theme = preferencesContainer.getBoolean("dark mode", false);
        return theme;
    }
}
