package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements MainCallbacks {
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
    public PicturesFragment picturesFragment;
    public AlbumsFragment albumsFragment;
    public SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);

        picturesFragment = PicturesFragment.getInstance(MainActivity.this);
        albumsFragment = AlbumsFragment.getInstance(MainActivity.this);
        settingsFragment = SettingsFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolder, picturesFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_pictures:
                    selectedFragment = picturesFragment;
                    break;
                case R.id.nav_album:
                    selectedFragment = albumsFragment;
                    break;
                case R.id.nav_setting:
                    selectedFragment = settingsFragment;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(MainActivity.this, "No network is currently active!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Network is not connected!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(MainActivity.this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(MainActivity.this, "Network is OK!", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onMsgFromFragtoMain(String sender, String request) {
        switch (sender) {
            case "PICTURES-FLAG":
                new UrlDialogFragment().show(getSupportFragmentManager(), UrlDialogFragment.Tag);
                break;
            case "URL-FLAG":
                boolean network = checkInternetConnection();
                if (!network)
                    return;
                DownloadImageFromURL task = new DownloadImageFromURL();
                task.execute(request);
                try {
                    Bitmap bitmap = task.get();
                    picturesFragment.onMsgFromMainToFrag(bitmap);
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No result", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}