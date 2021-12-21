package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
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

public class RotateFragment extends Fragment implements FragmentCallbacks {

    Slider rotateSlider;
    ImageButton rotateSquare, flipBtn;
    TextView valueRotate;
    Bitmap originalBmp, editBmp;
    Context context;
    static int degree, currentValue;

    public RotateFragment(Context context, Bitmap originalBmp) {
        this.context = context;
        this.originalBmp = originalBmp;
        this.editBmp = originalBmp.copy(originalBmp.getConfig(), true);
        this.degree = 0;
        this.currentValue = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rotateFragment = inflater.inflate(R.layout.rotate_fragment, container, false);
        rotateSlider = rotateFragment.findViewById(R.id.slider_rotate);
        valueRotate = rotateFragment.findViewById(R.id.valueRotate);
        rotateSquare = rotateFragment.findViewById(R.id.rotate_square);
        flipBtn = rotateFragment.findViewById(R.id.flipBtn);

        rotateSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBmp = rotate(90);
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", editBmp);
            }
        });

        flipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBmp = flip();
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", editBmp);
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
                Bitmap edit = rotate((int) slider.getValue());
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", edit);
            }
        });

        return rotateFragment;
    }

    private Bitmap rotate(int value) {
        Matrix matrix = new Matrix();
        matrix.postRotate(value - degree);
        currentValue = value;
        return Bitmap.createBitmap(editBmp, 0, 0, editBmp.getWidth(), editBmp.getHeight(), matrix, true);
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        if (null == result) {
            rotateSlider.setValue(0);
            valueRotate.setText("0");
            editBmp = originalBmp;
            currentValue = 0;
            degree = 0;
        }
        else {
            rotateSlider.setValue(0);
            editBmp = result;
            degree = currentValue;
        }
    }

    private Bitmap flip() {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        return Bitmap.createBitmap(editBmp, 0, 0, editBmp.getWidth(), editBmp.getHeight(), matrix, true);
    }
}
