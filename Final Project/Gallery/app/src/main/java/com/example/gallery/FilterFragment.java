package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterFragment extends Fragment {
    RecyclerView filterRecView;
    Context context;
    int[] filters = { 1, 2, 3, 4, 5, 6, 7, 8};

    FilterFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View filterFragment = inflater.inflate(R.layout.filter_image_fragment, container, false);
        filterRecView = (RecyclerView) filterFragment.findViewById(R.id.filterRecView);
        FilterAdapter adapter = new FilterAdapter(filters, context);
        filterRecView.setAdapter(adapter);
        filterRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        return filterFragment;
    }
}
