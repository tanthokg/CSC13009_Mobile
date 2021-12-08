package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

public class FoldersFragment extends Fragment {
    Context context;
    RecyclerView folderPicturesRecView;
    private ArrayList<String> folderPaths;

    FoldersFragment(Context context) {this.context = context;}

    public static FoldersFragment getInstance(Context context) {
        return new FoldersFragment(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View foldersFragment = inflater.inflate(R.layout.folder_picture_fragment, container, false);

        ((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)context).getSupportActionBar().setTitle("Gallery");
        ((MainActivity)context).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity)context).getSupportActionBar().setDisplayShowHomeEnabled(true);

        folderPicturesRecView = (RecyclerView) foldersFragment.findViewById(R.id.folderPicturesRecView);
        folderPaths = new ArrayList<String>();

        readFolders();
        
        return foldersFragment;
    }

    private void readFolders() {
        //String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //File inputFile = new File(sdPath, "Pictures");

        String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File sdFile = new File(sdPath);
        File[] foldersSd = sdFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return !s.startsWith(".");
            }
        });

        String dcimPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File dcimFile = new File(dcimPath);
        File[] foldersDCIM = dcimFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return !s.startsWith(".");
            }
        });

        try {
            folderPaths.add(sdFile.getAbsolutePath());
            for (File folder : foldersSd) {
                if (folder.getAbsolutePath().toString().contains(".png")||folder.getAbsolutePath().toString().contains(".jpg"))
                    continue;
                folderPaths.add(folder.getAbsolutePath());
            }
            for (File folder : foldersDCIM) {
                if (folder.getAbsolutePath().toString().contains(".png")||folder.getAbsolutePath().toString().contains(".jpg"))
                    continue;
                folderPaths.add(folder.getAbsolutePath());
            }
        }
        catch (Exception e) {
            getActivity().finish();
        }

        loadFolders();
    }
    
    private void loadFolders() {
        FolderAdapter folderAdapter = new FolderAdapter(context, folderPaths);
        folderPicturesRecView.setAdapter(folderAdapter);
        folderPicturesRecView.setLayoutManager(new GridLayoutManager(context, 2));
    }
}
