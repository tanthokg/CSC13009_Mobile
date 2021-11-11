package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class PicturesFragment extends Fragment {
    private RecyclerView galleryRecView;
    private TextView txtMsg;
    private File[] pictureFiles;
    Context context;

    PicturesFragment(Context context) {
        this.context = context;
    }

    public static PicturesFragment getInstance(Context context)
    {
        return new PicturesFragment(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View picturesFragment = inflater.inflate(R.layout.pictures_fragment, container, false);
        galleryRecView = picturesFragment.findViewById(R.id.picturesRecView);
        txtMsg = picturesFragment.findViewById(R.id.txtMsg);
        readPicturesFolder();
        return picturesFragment;
    }

    void readPicturesFolder() {
        try {
            // Get path to external storage: /storage/emulated/0
            String absolutePathToSDCard = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Path to Pictures folder: /storage/emulated/0/Pictures/
            String pathToPicturesFolder = absolutePathToSDCard + "/Pictures/";
            txtMsg.append("Path: " + pathToPicturesFolder + "\n");
            File pictureFile = new File(pathToPicturesFolder);
            pictureFiles = pictureFile.listFiles();
            txtMsg.append( "Exist: " + pictureFile.exists() + ". Is Directory: " + pictureFile.isDirectory()
                    + ". Can Read: " + pictureFile.canRead() + "\n");
            if (pictureFiles == null)
                txtMsg.append("NULL");
            else {
                txtMsg.append("File Count: " + pictureFiles.length + "\n");
                // Load gallery with current path
                loadGallery(pathToPicturesFolder);
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
        GalleryAdapter galleryAdapter = new GalleryAdapter(context, pathToPicturesFolder);
        galleryRecView.setAdapter(galleryAdapter);
        galleryRecView.setLayoutManager(new GridLayoutManager(context, 3));
    }
}
