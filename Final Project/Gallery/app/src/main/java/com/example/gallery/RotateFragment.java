package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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

public class RotateFragment extends Fragment {
    static RotateFragment rotateFragment;
    Slider rotateSlider;
    ImageButton rotateSquare, flipBtn, btnClearRotate, btnCheckRotate;
    TextView valueRotate;
    Context context;
    static int degree, angleSquare;
    EditImageView editImageView;

    public static RotateFragment getInstance(Context context, EditImageView editImageView) {
        rotateFragment =  new RotateFragment(context, editImageView);
        return rotateFragment;
    }

    private RotateFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
        degree = 0;
        angleSquare = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rotateFragment = inflater.inflate(R.layout.rotate_fragment, container, false);
        rotateSlider = (Slider) rotateFragment.findViewById(R.id.slider_rotate);
        valueRotate = (TextView) rotateFragment.findViewById(R.id.valueRotate);
        rotateSquare = (ImageButton) rotateFragment.findViewById(R.id.rotate_square);
        flipBtn = (ImageButton) rotateFragment.findViewById(R.id.flipBtn);
        btnClearRotate = (ImageButton) rotateFragment.findViewById(R.id.btnClearRotate);
        btnCheckRotate = (ImageButton) rotateFragment.findViewById(R.id.btnCheckRotate);

        rotateSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.setAngleRotate(90 + angleSquare);
                angleSquare += 90;
            }
        });

        flipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.flip();
            }
        });

        btnClearRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateSlider.setValue(0);
                valueRotate.setText("0");
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE", "CLEAR", null);
            }
        });

        btnCheckRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE", "CHECK", null);
            }
        });

        rotateSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                valueRotate.setText(String.valueOf((int) value));
            }
        });
        rotateSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                degree = (int) slider.getValue();
                editImageView.setAngleRotate(degree);
            }
        });

        return rotateFragment;
    }
}
