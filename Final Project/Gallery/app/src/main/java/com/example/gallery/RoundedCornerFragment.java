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

public class RoundedCornerFragment extends Fragment {
    static RoundedCornerFragment roundedCornerFragment;
    Slider roundedSlider;
    ImageButton btnClearRounded, btnCheckRounded;
    TextView valueRounded;
    Context context;
    static int degree, angleSquare;
    EditImageView editImageView;

    public static RoundedCornerFragment getInstance(Context context, EditImageView editImageView) {
        if (roundedCornerFragment != null)
            return roundedCornerFragment;
        roundedCornerFragment=  new RoundedCornerFragment(context, editImageView);
        return roundedCornerFragment;
    }

    private RoundedCornerFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
        degree = 0;
        angleSquare = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View roundedCornerFragment = inflater.inflate(R.layout.rounded_corner_fragment, container, false);
        roundedSlider = (Slider) roundedCornerFragment.findViewById(R.id.slider_rounded_corner);
        valueRounded = (TextView) roundedCornerFragment.findViewById(R.id.valueRoundedCorner);

        btnClearRounded = (ImageButton) roundedCornerFragment.findViewById(R.id.btnClearRounded);
        btnCheckRounded = (ImageButton) roundedCornerFragment.findViewById(R.id.btnCheckRounded);

        btnClearRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundedSlider.setValue(0);
                valueRounded.setText("0");
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROUNDED CORNER", "CLEAR", null);
            }
        });

        btnCheckRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROUNDED CORNER", "CHECK", null);
            }
        });

        roundedSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                valueRounded.setText(String.valueOf((int) value));
            }
        });
        roundedSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                degree = (int) slider.getValue();
                editImageView.setAngleRounded(degree);
            }
        });

        return roundedCornerFragment;
    }


}
