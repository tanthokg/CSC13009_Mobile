package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterFragment extends Fragment {

    Context context;
    EditImageView editImageView;
    static FilterFragment filterFragment;

    RecyclerView filterRecView;
    ImageButton btnClearFilter, btnCheckFilter;
    FilterAdapter adapter;
    static Bitmap bitmap;

    String[] filters = { "No Effect", "Auto", "Cream", "Forest", "Cozy", "Blossom", "Evergreen", "Grayscale", "Sharpen", "Vintage"};

    public static FilterFragment getInstance(Context context, EditImageView editImageView, Bitmap bmp) {
        filterFragment = new FilterFragment(context, editImageView, bmp);
        return filterFragment;
    }

    private FilterFragment(Context context, EditImageView editImageView, Bitmap bitmap) {
        this.context = context;
        this.editImageView = editImageView;
        this.bitmap = bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_image_fragment, container, false);

        filterRecView = view.findViewById(R.id.filterRecView);
        btnClearFilter = view.findViewById(R.id.btnClearFilter);
        btnCheckFilter = view.findViewById(R.id.btnCheckFilter);

        btnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("FILTER", "CLEAR", null);
            }
        });

        btnCheckFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("FILTER", "CHECK", null);
            }
        });

        adapter = new FilterAdapter(filters, context, bitmap, editImageView);
        filterRecView.setAdapter(adapter);
        filterRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}
