package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlbumsFragment extends Fragment {
    public static AlbumsFragment getInstance()
    {
        return new AlbumsFragment();
    }
    private FloatingActionButton btnAdd;
    private final int CAMERA_CAPTURED = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.albums_fragment, container, false);

        btnAdd = (FloatingActionButton) albumsFragment.findViewById(R.id.btnAdd_AlbumsFragment);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return albumsFragment;
    }
}