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

import com.google.android.material.slider.Slider;

public class EraserFragment extends Fragment {

    Context context;
    EditImageView editImageView;
    static EraserFragment eraserFragment;
    TextView txtEraserSize;
    Slider sliderEraserSize;
    ImageButton btnCheckEraser;

    public static EraserFragment getInstance(Context context, EditImageView editImageView) {
        if (eraserFragment != null)
            return eraserFragment;
        eraserFragment = new EraserFragment(context, editImageView);
        return eraserFragment;
    }

    private EraserFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_eraser_fragment, container, false);

        txtEraserSize = (TextView) view.findViewById(R.id.txtEraserSize);
        sliderEraserSize = (Slider) view.findViewById(R.id.sliderEraserSize);
        btnCheckEraser = (ImageButton) view.findViewById(R.id.btnCheckEraser);
        editImageView.enableEraser();

        btnCheckEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("ERASER", "CHECK", null);
            }
        });

        sliderEraserSize.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                txtEraserSize.setText(String.valueOf((int) slider.getValue()));
            }
        });

        sliderEraserSize.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                editImageView.setEraserSize((int) slider.getValue());
            }
        });

        return view;
    }
}
