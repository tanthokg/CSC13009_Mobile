package com.example.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;

public class EditImageActivity extends AppCompatActivity {

    EditImageView editImageView;
    String pathPictureFile;
    int widthScreen, heightScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme(AppConfig.getInstance(this).getDarkMode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        pathPictureFile = intent.getStringExtra("pathToPictureFolder");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heightScreen = displayMetrics.heightPixels;

        editImageView = (EditImageView) findViewById(R.id.imgEdit);
        Glide.with(this).asBitmap().load(pathPictureFile).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                editImageView.setBitmapResource(resource, widthScreen, heightScreen*6/10);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
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
            Glide.with(this).asBitmap().load(pathPictureFile).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    editImageView.setBitmapResource(resource, widthScreen, heightScreen*6/10);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            return true;
        }
        if (R.id.menuSave == id) {
            //saveImage(currentBitmap);
        }
        if (android.R.id.home == id) {
            onBackPressed();
            return true;
        }
        return false;
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

    private void saveImage(Bitmap bitmap) {
        String pathFile = pathPictureFile.substring(0, pathPictureFile.lastIndexOf("/"));
        File pictureFile = new File(pathFile, bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Saved to " + pathFile, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error to save image in edit ", e.getMessage());
        }
    }
}
