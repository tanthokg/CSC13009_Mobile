package com.example.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.effect.EffectFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

public class EditImageActivity extends AppCompatActivity implements EditCallbacks{

    ImageView imgEdit;
    FilterFragment filterFragment;
    Bitmap currentBitmap;
    String pathPictureFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        pathPictureFile = intent.getStringExtra("pathToPictureFolder");
        Glide.with(this).asBitmap().load(pathPictureFile).into(imgEdit);
        filterFragment = new FilterFragment(this, BitmapFactory.decodeFile(pathPictureFile));

        getSupportFragmentManager().beginTransaction().replace(R.id.editFragment, filterFragment).commit();
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

    @Override
    public void onMsgFromFragToEdit(String sender, Bitmap request) {
        switch (sender) {
            case "FILTER-FLAG":
                Glide.with(this).asBitmap().load(request).into(imgEdit);
                currentBitmap = request;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (R.id.menuReset == id) {
            Glide.with(this).asBitmap().load(pathPictureFile).into(imgEdit);
            return true;
        }
        if (R.id.menuSave == id) {
            saveImage(currentBitmap);
        }
        if (android.R.id.home == id) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void saveImage(Bitmap bitmap) {
        String pathFile = pathPictureFile.substring(0, pathPictureFile.lastIndexOf("/"));
        File pictureFile = new File(pathFile, bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error to save image in edit ", e.getMessage());
        }
    }
}
