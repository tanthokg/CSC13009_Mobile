package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

public class BrushFragment extends Fragment {

    Context context;
    EditImageView editImageView;
    static BrushFragment brushFragment;
    RecyclerView brushColorRecView;
    TextView txtBrushSize;
    Slider sliderBrushSize;
    ImageButton btnCheckBrush;
    ColorAdapter adapter;

    String[] colors = {"#000000", "#FFFFFF", "#FF0000", "#8B0000", "#FF6347", "#FFD700", "#B8860B", "#FFFF00", "#006400", "#00FF00", "#98FB98", "#008B8B",
            "#00FFFF", "#1E90FF", "#00008B", "#0000FF", "#8A2BE2", "#7B68EE", "#9400D3", "#FF00FF", "#FF1493", "#FFC0CB", "#FFE4C4", "#8B4513", "#808080"};

    public static BrushFragment getInstance(Context context, EditImageView editImageView) {
        brushFragment = new BrushFragment(context, editImageView);
        return brushFragment;
    }

    private BrushFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_brush_fragment, container, false);

        brushColorRecView = (RecyclerView) view.findViewById(R.id.brushColorRecView);
        txtBrushSize = (TextView) view.findViewById(R.id.txtBrushSize);
        sliderBrushSize = (Slider) view.findViewById(R.id.sliderBrushSize);
        btnCheckBrush = (ImageButton) view.findViewById(R.id.btnCheckBrush);
        editImageView.enableBrush();

        btnCheckBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("BRUSH", "CHECK", null);
            }
        });

        sliderBrushSize.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                txtBrushSize.setText(String.valueOf((int) slider.getValue()));
            }
        });

        sliderBrushSize.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                editImageView.setBrushSize((int) slider.getValue());
            }
        });

        adapter = new ColorAdapter(colors, context, editImageView);
        brushColorRecView.setAdapter(adapter);
        brushColorRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}
