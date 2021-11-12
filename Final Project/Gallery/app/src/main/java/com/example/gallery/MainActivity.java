package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
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
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        picturesFragment = PicturesFragment.getInstance(MainActivity.this);
        albumsFragment = AlbumsFragment.getInstance();
        settingsFragment = SettingsFragment.getInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolder, picturesFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_pictures:
                        //selectedFragment = new PicturesFragment(MainActivity.this);
                        selectedFragment = picturesFragment;
                        break;
                    case R.id.nav_album:
                        //selectedFragment = new AlbumsFragment();
                        selectedFragment = albumsFragment;
                        break;
                    case R.id.nav_setting:
                        //selectedFragment = new SettingsFragment();
                        selectedFragment = settingsFragment;
                        break;
                }
                // Use addToBackStack to return the previous fragment when the Back button is pressed
                if (selectedFragment != null)
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentHolder, selectedFragment)
                            .commit();
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: load images, maybe
            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}