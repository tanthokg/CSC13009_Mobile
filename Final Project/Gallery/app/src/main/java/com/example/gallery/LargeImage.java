package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gallery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import LargeImagePackage.ViewPagerAdapter;
import LargeImagePackage.ZoomableViewPager;

public class LargeImage extends AppCompatActivity {

    File pictureFile;
    File[] pictureFiles;
    ZoomableViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    int[] currentPosition;

    private WallpaperManager wallpaperManager;
    private AlbumsFragment albumsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_large_item_container);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        albumsFragment = new AlbumsFragment(this);

        // Get current position from intent
        // It has to be final int[] when used in anonymous functions, otherwise it will cause errors
        currentPosition = new int[]{intent.getIntExtra("itemPosition", -1)};

        // Create a File object from the received path
        pictureFile = new File(intent.getStringExtra("pathToPicturesFolder"));

        // Create an array contains all files in the folder above
        pictureFiles = pictureFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase(Locale.ROOT).endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
            }
        });

        mViewPager = (ZoomableViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(this, pictureFiles);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(currentPosition[0]);

        /*
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.deleteAlbum) {
                    String path = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
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
        });*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.deleteAlbum) {
                    String path = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    LargeImage.this.deleteOnPath(path, bottomNavigationView);
                }
                if (item.getItemId() == R.id.shareAlbum) {
                    String path = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    LargeImage.this.shareOnPath(path)    ;
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

    /*
    private void deleteOn(String path) {
        File a = new File(path);
        a.delete();
        callScanIntent(getApplicationContext(),path);
        Toast.makeText(this,"Image Deleted",Toast.LENGTH_SHORT).show();
        finish();
    }*/

    private void deleteOnPath(String path, View bottomNav)
    {
        androidx.appcompat.app.AlertDialog.Builder confirmDialog =
                new androidx.appcompat.app.AlertDialog.Builder(bottomNav.getContext(), R.style.AlertDialog);
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

    private void shareOnPath(String path) {
        Drawable drawable = mViewPagerAdapter.getImageView().getDrawable();
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

        try {
            File file=new File(path);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  callScanIntent(Context context, String path) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,null);
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

        ImageView largeImage = mViewPagerAdapter.getImageView();
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
                    Toast.makeText(this, "Lock screen wallpaper not supported", Toast.LENGTH_SHORT).show();
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

    public static Bitmap viewToBitmap(View view,int width,int height){
        Bitmap bm=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bm);
        view.draw(canvas);
        return bm;
    }


}