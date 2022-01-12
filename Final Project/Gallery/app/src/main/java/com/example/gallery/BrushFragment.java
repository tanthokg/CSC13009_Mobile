package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BrushFragment extends Fragment {

    Context context;
    EditImageView editImageView;
    static BrushFragment brushFragment;
    RecyclerView brushColorRecView;
    ImageButton btnClearBrush, btnCheckBrush;

    public static BrushFragment getInstance(Context context, EditImageView editImageView) {
        if (brushFragment != null)
           return brushFragment;
        return new BrushFragment(context, editImageView);
    }

    public BrushFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_brush_fragment, container, false);

        brushColorRecView = (RecyclerView) view.findViewById(R.id.brushColorRecView);
        btnClearBrush = (ImageButton) view.findViewById(R.id.btnClearBrush);
        btnCheckBrush = (ImageButton) view.findViewById(R.id.btnCheckBrush);
        editImageView.enableBrush();

        btnClearBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("BRUSH", "CLEAR", null);
            }
        });

        btnCheckBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("BRUSH", "CHECK", null);
            }
        });

        return view;
    }
}
