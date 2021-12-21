package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements MainCallbacks {

    public FoldersFragment foldersFragment;
    public PicturesFragment picturesFragment;
    public AlbumsFragment albumsFragment;
    public SettingsFragment settingsFragment;
    public TrashedFragment trashedFragment;
    Fragment selectedFragment;
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    static boolean darkButtonIsPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeTheme(AppConfig.getInstance(this).getDarkMode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);
    }

    private void initiateApp() {
        actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.picture);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        bottomNavigationView = findViewById(R.id.bottomNavBar);

        foldersFragment = FoldersFragment.getInstance(MainActivity.this);
        picturesFragment = null;
        trashedFragment = null;
        albumsFragment = AlbumsFragment.getInstance(MainActivity.this);
        settingsFragment = SettingsFragment.getInstance();
        if (!darkButtonIsPressed) {
            selectedFragment = foldersFragment;
        }
        else {
            selectedFragment = settingsFragment;
            darkButtonIsPressed = false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, selectedFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (R.id.nav_pictures == itemId) {
                selectedFragment = foldersFragment;
            } else if (R.id.nav_album == itemId) {
                selectedFragment = albumsFragment;
            } else if (R.id.nav_setting == itemId) {
                selectedFragment = settingsFragment;
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
                initiateApp();
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
    public void onMsgFromFragToMain(String sender, String request) {
        switch (sender) {
            case "PICTURES-FLAG":
                if (request.contains("Open Url Dialog"))
                    new UrlDialogFragment().show(getSupportFragmentManager(), UrlDialogFragment.Tag);
                else if (request.contains("Turn back folder")) {
                    selectedFragment = foldersFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, selectedFragment).commit();
                } else if (request.contains("Turn back album")) {
                    selectedFragment = albumsFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, selectedFragment).commit();
                }
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
            case "SETTING-FLAG":
                try {
                    boolean isDarkMode = Boolean.parseBoolean(request);
                    darkButtonIsPressed = true;
                    changeTheme(isDarkMode);
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Can't set dark mode!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "FOLDER-FLAG":
                try {
                    picturesFragment = PicturesFragment.getInstance(foldersFragment.getContext(), request, "FOLDER");
                    selectedFragment = picturesFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, selectedFragment).commit();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Can't call picture fragment!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "ALBUM-FLAG":
                try {
                    if (request.equals("Trashed")) {
                        trashedFragment = TrashedFragment.getInstance(albumsFragment.getContext());
                        selectedFragment = trashedFragment;
                    } else {
                        picturesFragment = PicturesFragment.getInstance(albumsFragment.getContext(), request, "ALBUM");
                        selectedFragment = picturesFragment;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, selectedFragment).commit();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Can't call picture fragment!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void changeTheme(boolean isChecked) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_Gallery_);
            AppConfig.getInstance(this).setDarkMode(true);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Gallery);
            AppConfig.getInstance(this).setDarkMode(false);
        }
    }

}