package com.example.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditImageActivity extends AppCompatActivity implements EditCallbacks {

    EditImageView editImageView;
    RecyclerView toolsRecView;
    Fragment currentFragment;
    List<Tool> toolItemList;
    ToolAdapter adapter;
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

        toolsRecView = (RecyclerView) findViewById(R.id.toolsRecView);
        initTool();
    }

    private void initTool() {
        toolItemList = new ArrayList<Tool>();
        toolItemList.add(new Tool(R.drawable.ic_outline_rotate_left_24, "Rotate"));
        toolItemList.add(new Tool(R.drawable.ic_outline_filter_hdr_24, "Filter"));
        toolItemList.add(new Tool(R.drawable.ic_outline_brush_24, "Brush"));

        adapter = new ToolAdapter(toolItemList, EditImageActivity.this, editImageView);
        toolsRecView.setAdapter(adapter);
        toolsRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                    editImageView.reset();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            return true;
        }
        if (R.id.menuSave == id) {
            saveImage(editImageView.getEditBitmap());
        }
        if (android.R.id.home == id) {
            onBackPressed();
            finish();
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

    public void inflateFragment(Fragment fragment) {
        toolsRecView.setVisibility(View.GONE);
        getSupportActionBar().hide();
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.editFragment, currentFragment).commit();
    }

    @Override
    public void onMsgFromFragToEdit(String sender, String request, Bitmap bitmap) {
        if (request.equals("CLEAR")) {
            if (sender.equals("ROTATE"))
                editImageView.clearRotate();
            if (sender.equals("FILTER")) {
                editImageView.clearFilter();
            }
        }
        if (request.equals("CHECK")) {
            editImageView.saveImage();
        }
        getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        getSupportActionBar().show();
        toolsRecView.setVisibility(View.VISIBLE);
    }
}
