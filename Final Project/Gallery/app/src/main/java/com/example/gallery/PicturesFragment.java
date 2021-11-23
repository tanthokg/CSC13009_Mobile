package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Locale;

public class PicturesFragment extends Fragment implements FragmentCallbacks{
    private RecyclerView picturesRecView;
    private TextView txtMsg;
    private File[] allFiles;
    private File[] pictureFiles;
    private final int spanCount = 4;

    Context context;
    String pathFolder;
    private FloatingActionButton btnAdd, btnUpload, btnCamera, btnUrl;
    private boolean addIsPressed;
    private Animation menuFABShow, menuFABHide;
    private final int CAMERA_CAPTURED = 100;
    MainActivity main;

    public static PicturesFragment getInstance(Context context, String pathFolder)
    {
        return new PicturesFragment(context, pathFolder);
    }

    PicturesFragment(Context context, String pathFolder) {
        this.context = context;
        this.pathFolder = pathFolder;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        // Show the up-key back arrow on Action Bar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View picturesFragment = inflater.inflate(R.layout.pictures_fragment, container, false);
        /*((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)context).getSupportActionBar().setHomeButtonEnabled(true);*/

        picturesRecView = picturesFragment.findViewById(R.id.picturesRecView);
        txtMsg = picturesFragment.findViewById(R.id.txtMsg);

        btnAdd = (FloatingActionButton) picturesFragment.findViewById(R.id.btnAdd_PicturesFragment);
        btnUpload = (FloatingActionButton) picturesFragment.findViewById(R.id.btnUpload_PicturesFragment);
        btnCamera = (FloatingActionButton) picturesFragment.findViewById(R.id.btnCamera_PicturesFragment);
        btnUrl = (FloatingActionButton) picturesFragment.findViewById(R.id.btnUrl_PicturesFragment);

        menuFABShow = AnimationUtils.loadAnimation(context, R.anim.menu_button_show);
        menuFABHide = AnimationUtils.loadAnimation(picturesFragment.getContext(), R.anim.menu_bottom_hide);

        addIsPressed = false;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimationButton(addIsPressed);
                setVisibilityButton(addIsPressed);
                addIsPressed = !addIsPressed;
            }
        });

        picturesRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    btnAdd.show();
                }
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    if (addIsPressed) {
                        setAnimationButton(addIsPressed);
                        setVisibilityButton(addIsPressed);
                        addIsPressed = !addIsPressed;
                    }
                    btnAdd.hide();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("PICTURES-FLAG", "Open Url Dialog");
            }
        });

        readPicturesFolder();
        return picturesFragment;
    }

    void readPicturesFolder() {
        try {
            /*// Get path to external storage: /storage/emulated/0
            String absolutePathToSDCard = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Path to Pictures folder: /storage/emulated/0/Pictures/
            String pathToPicturesFolder = absolutePathToSDCard + "/Pictures/";
            txtMsg.append("Path: " + pathToPicturesFolder + "\n");*/

            File pictureFile = new File(pathFolder);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.toLowerCase().endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
                }
            };
            allFiles = pictureFile.listFiles();
            pictureFiles = pictureFile.listFiles(filter);
            txtMsg.append( "Exist: " + pictureFile.exists() + ". Is Directory: " + pictureFile.isDirectory()
                    + ". Can Read: " + pictureFile.canRead() + "\n");
            if (pictureFiles == null)
                txtMsg.append("NULL");
            else {
                txtMsg.append("Picture/Item: " + pictureFiles.length + "/" + allFiles.length + "\n");
                // Load gallery with current path
                loadGallery(pathFolder);
            }
        }
        catch (Exception e) {
            txtMsg.append(e.getMessage());
        }
    }

    void loadGallery(String pathToPicturesFolder) {
        // The idea was to send a string path to the adapter, not a File object
        // The adapter will then create everything we need from the provided path
        // This implementation is not permanent
        PicturesAdapter picturesAdapter = new PicturesAdapter(context, pathToPicturesFolder);
        picturesRecView.setAdapter(picturesAdapter);
        picturesRecView.setLayoutManager(new GridLayoutManager(context, spanCount));
    }

    void setAnimationButton(boolean isPressed) {
        if (isPressed) {
            btnAdd.setImageResource(R.drawable.ic_round_add_24);
            btnUpload.startAnimation(menuFABHide);
            btnCamera.startAnimation(menuFABHide);
            btnUrl.startAnimation(menuFABHide);
        }
        else {
            btnAdd.setImageResource(R.drawable.ic_round_close_24);
            btnUpload.startAnimation(menuFABShow);
            btnCamera.startAnimation(menuFABShow);
            btnUrl.startAnimation(menuFABShow);
        }
    }

    void setVisibilityButton(boolean isPressed) {
        if (isPressed) {
            btnUpload.setVisibility(FloatingActionButton.INVISIBLE);
            btnCamera.setVisibility(FloatingActionButton.INVISIBLE);
            btnUrl.setVisibility(FloatingActionButton.INVISIBLE);
        }
        else {
            btnUpload.setVisibility(FloatingActionButton.VISIBLE);
            btnCamera.setVisibility(FloatingActionButton.VISIBLE);
            btnUrl.setVisibility(FloatingActionButton.VISIBLE);
        }
    }

    void openCamera() {
        try {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityFromFragment(this, takePhotoIntent, CAMERA_CAPTURED);
        }
        catch (Exception e) {
            Log.e("Error to open camera! ", e.getMessage());
        }
    }

    private File getFolderDirectory() {
        String pathToPictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File pictureDirectory = new File(pathToPictureFolder);
        if (!pictureDirectory.exists())
            pictureDirectory.mkdirs();
        return pictureDirectory;
    }

    void saveImage(Bitmap bitmap) {
        File pictureFile = new File(getFolderDirectory(), bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            Log.e("Error to save image! ", e.getMessage());
        }
        readPicturesFolder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURED) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveImage(bitmap);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update pictures view when LargeImage activity is finished
        txtMsg.setText("");
        readPicturesFolder();
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        saveImage(result);
    }

    // call the up-key back on Action Bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        main.onMsgFromFragToMain("PICTURES-FLAG", "Turn back folder");
        return true;
    }
}
