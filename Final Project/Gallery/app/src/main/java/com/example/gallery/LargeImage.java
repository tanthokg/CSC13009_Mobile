package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import LargeImagePackage.ViewPagerAdapter;
import LargeImagePackage.ZoomableViewPager;

public class LargeImage extends AppCompatActivity {
    File pictureFile;
    File[] pictureFiles;
    ZoomableViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    int currentPosition;
    String type;
    private WallpaperManager wallpaperManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_picture_container);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        // Get current position from intent
        currentPosition = intent.getIntExtra("itemPosition", -1);

        // Create a File object from the received path
        type = intent.getStringExtra("itemType");
        if (type.equals("FOLDER")) {
            pictureFile = new File(intent.getStringExtra("pathToPicturesFolder"));
            // Create an array contains all files in the folder above
            pictureFiles = pictureFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.toLowerCase(Locale.ROOT).endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
                }
            });
        }
        if (type.equals("ALBUM")) {
            String albumName = intent.getStringExtra("pathToPicturesFolder");
            AlbumData albumData = AlbumUtility.getInstance(this).findDataByAlbumName(albumName);
            ArrayList<String> picturePaths = albumData.getPicturePaths();
            int i = 0;
            pictureFiles = new File [picturePaths.size()];
            for (String path : picturePaths) {
                pictureFiles[i] = new File(path);
                i++;
            }
        }

        mViewPager = (ZoomableViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(this, pictureFiles);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(currentPosition);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.deletePicture) {
                    String path = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    if (type.equals("FOLDER"))
                        deleteOnDeviceByPath(path, bottomNavigationView);
                    if (type.equals("ALBUM"))
                        deleteOnAlbumByPath(intent.getStringExtra("pathToPicturesFolder"), path);
                }
                if (item.getItemId() == R.id.sharePicture) {
                    String path = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    LargeImage.this.shareOnPath(path);
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

    private void deleteOnDeviceByPath(String path, View bottomNav)
    {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(bottomNav.getContext(), R.style.AlertDialog);
        confirmDialog.setMessage("Are you sure to remove this picture from device?");
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

    private void deleteOnAlbumByPath(String albumName, String picturePath) {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this, R.style.AlertDialog);

        // confirmDialog.setMessage("Are you sure to remove this picture from album " + albumName + "?");
        confirmDialog.setMessage("Are you sure to remove this picture from album?");
        confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AlbumUtility.getInstance(LargeImage.this).deletePictureInAlbum(albumName, picturePath)) {
                    Toast.makeText(LargeImage.this, "Picture removed from album", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(LargeImage.this, "Cannot remove this from album", Toast.LENGTH_SHORT).show();
            }
        });
        confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        confirmDialog.create().show();
    }

    private void shareOnPath(String path) {
        Drawable drawable = mViewPagerAdapter.getImageView().getDrawable();
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

        try {
            File file = new File(path);
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
        menuInflater.inflate(R.menu.large_picture_top_menu, menu);
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
            try {
                // set the wallpaper by calling the setResource function and passing the drawable file
                // Glide.with(this).asBitmap().load(pictureFiles[currentPosition[0]].getAbsolutePath())
                wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(),largeImage.getHeight()));
            } catch (IOException e) {
                // e.printStackTrace();
                Log.e("Error set as wallpaper: ", e.getMessage());
            }
            Toast.makeText(this, "Set as Wallpaper", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_SetLockscreen) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(),largeImage.getHeight()), null, true, WallpaperManager.FLAG_LOCK); //For Lock screen
                    Toast.makeText(this, "Set as Lockscreen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lock screen wallpaper not supported", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                // e.printStackTrace();
                Log.e("Error set as lockscreen: ", e.getMessage());
            }

        }
        if (item.getItemId() == R.id.menu_ViewInfo) {
            File currentFile = new File(pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ROOT);

            View pictureInfoView = LayoutInflater.from(this).inflate(R.layout.picture_info, null);
            TextView filename = pictureInfoView.findViewById(R.id.info_filename);
            TextView filepath = pictureInfoView.findViewById(R.id.info_filepath);
            TextView lastModified = pictureInfoView.findViewById(R.id.info_lastModified);
            TextView filesize = pictureInfoView.findViewById(R.id.info_filesize);
            filename.setText(currentFile.getName());
            filepath.setText(currentFile.getAbsolutePath());
            lastModified.setText(sdf.format(currentFile.lastModified()));
            filesize.setText(Math.round(currentFile.length() * 1.0 / 1000) + " KB");

            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
            dialog.setView(pictureInfoView);
            dialog.create().show();
        }
        if (item.getItemId() == R.id.menu_AddToAlbum) {
            addPictureToAlbum();
            String result = AlbumUtility.getInstance(this).getAllAlbums().size() + " album(s)";
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPictureToAlbum() {
        View addToAlbumView = LayoutInflater.from(this).inflate(R.layout.choose_album_form, null);
        ListView chooseAlbumListView = addToAlbumView.findViewById(R.id.chooseAlbumListView);

        ArrayList<String> albums = AlbumUtility.getInstance(this).getAllAlbums();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, albums);
        chooseAlbumListView.setAdapter(adapter);
        /*chooseAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String albumName = albums.get(i);
                String picturePath = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                if (AlbumUtility.getInstance(LargeImage.this).addPictureToAlbum(albumName, picturePath)) {
                    Toast.makeText(LargeImage.this, albums.get(i) + " chosen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LargeImage.this, "Error: Cannot Add To Selected Album", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        AlertDialog.Builder addToAlbumDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        addToAlbumDialog.setView(addToAlbumView);
        ArrayList<String> chosen = new ArrayList<String>();

        addToAlbumDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String picturePath = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                for (int index = 0; index < chooseAlbumListView.getCount(); ++index) {
                    if (chooseAlbumListView.isItemChecked(index))
                        chosen.add(chooseAlbumListView.getItemAtPosition(index).toString());
                }
                for (String s: chosen) {
                    AlbumUtility.getInstance(LargeImage.this).addPictureToAlbum(s, picturePath);
                }
                Toast.makeText(LargeImage.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });
        addToAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(LargeImage.this, "CANCELED", Toast.LENGTH_SHORT).show();
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