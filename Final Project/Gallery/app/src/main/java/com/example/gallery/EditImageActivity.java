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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class EditImageActivity extends AppCompatActivity implements EditCallbacks{

    ImageView imgEdit;
    FilterFragment filterFragment;

    String pathPictureFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme(checkTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_activity);

        Intent intent = getIntent();
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        pathPictureFile = intent.getStringExtra("pathToPictureFolder");
        Glide.with(this).asBitmap().load(pathPictureFile).into(imgEdit);
        filterFragment = new FilterFragment(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.editFragment, filterFragment).commit();

        setFilter(0.22, 0.5, 1);
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
    public void onMsgFromFragToEdit(String sender, String request) {

    }

    public void setFilter(double red, double green, double blue) {
        try {
            Bitmap bmp = BitmapFactory.decodeFile(pathPictureFile);
            Bitmap newBmp = bmp.copy(bmp.getConfig(), true);
            /*for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int p = bmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);

                    int intensity = (r + g + b) / 3;
                    *//*r = (int) red * r;
                    g = (int) green * g;
                    b = (int) blue * b;*//*
                    r = intensity;
                    g = intensity;
                    b = intensity;
                    bmp.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                }
            }*/
            ColorMatrix matrix = new ColorMatrix();
            matrix.set(new float[]{
                    0.33f, 0.33f, 0.33f, 0, 0,
                    0.33f, 0.33f, 0.33f, 0, 0,
                    0.33f, 0.33f, 0.33f, 0, 0,
                    0, 0, 0, 1, 0
            });
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
            Canvas canvas = new Canvas(newBmp);
            Glide.with(this).load(newBmp).into(imgEdit);
            canvas.drawBitmap(newBmp,0, 0, paint);
            /*Glide.with(this).load(newBmp).into(imgEdit);*/
        }
        catch (Exception e) {
            Log.e("Error in filter image", e.getMessage());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
