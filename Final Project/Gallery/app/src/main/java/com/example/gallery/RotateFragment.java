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

public class RotateFragment extends Fragment implements FragmentCallbacks {

    Slider rotateSlider;
    ImageButton rotateSquare, flipBtn, btnClearRotate, btnCheckRotate;
    TextView valueRotate;
    Bitmap originalBmp, editBmp, currentBmp;
    Context context;
    static int degree;

    public RotateFragment(Context context, Bitmap originalBmp) {
        this.context = context;
        this.originalBmp = originalBmp;
        this.editBmp = originalBmp.copy(originalBmp.getConfig(), true);
        this.currentBmp = editBmp;
        degree = 0;
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
                currentBmp = rotate(90 + degree);
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", "UPDATE", currentBmp);
            }
        });

        flipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBmp = flip();
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", "UPDATE", currentBmp);
            }
        });

        btnClearRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateSlider.setValue(0);
                valueRotate.setText("0");
                currentBmp = editBmp;
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", "CLEAR", editBmp);
            }
        });

        btnCheckRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", "SAVE", currentBmp);
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
                currentBmp = rotate((int) slider.getValue());
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", "UPDATE", currentBmp);
            }
        });

        return rotateFragment;
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        rotateSlider.setValue(0);
        valueRotate.setText("0");
        degree = 0;
        if (null == result) {
            editBmp = originalBmp;
            currentBmp = originalBmp;
        }
        else {
            editBmp = result;
            currentBmp = result;
        }
    }

    // TODO: 90 + 90 = 180 -> rotate 180 -> not changed
    private Bitmap rotate(int value) {
        int width = editBmp.getWidth();
        int height = editBmp.getHeight();

        // Set width and height of rotated bitmap
        int newWidth = width, newHeight = height;
        if (((value - degree) == 90 || (value - degree) == -90) && (degree == 0 || degree == -180 || degree == 180)){
            newWidth = height;
            newHeight = width;
        }
        else if ((value - degree) != 0 && (value - degree != 180) && (value - degree) != -180) {
            double radAngle = Math.toRadians(value);
            double cosAngle = Math.abs(Math.cos(radAngle));
            double sinAngle = Math.abs(Math.sin(radAngle));
            newWidth = (int) (width *cosAngle + height *sinAngle);
            newHeight = (int) (width *sinAngle + height *cosAngle);
        }

        // Create a new bitmap to rotate
        Bitmap rotatedBitmap = Bitmap.createBitmap(newWidth, newHeight, originalBmp.getConfig());
        Canvas canvas = new Canvas(rotatedBitmap);

        // Get 2 edges of bitmap and depend on them to rotate
        Rect rect = new Rect(0,0, newWidth, newHeight);
        Matrix matrix = new Matrix();
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        matrix.postTranslate((float) (-currentBmp.getWidth()/2), (float) (-currentBmp.getHeight()/2));
        matrix.postRotate(value - degree);
        // Save current angle
        degree = value;
        matrix.postTranslate(px, py);
        canvas.drawBitmap(currentBmp, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG ));
        matrix.reset();

        return rotatedBitmap;
    }

    private Bitmap flip() {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        degree = degree * -1;
        return Bitmap.createBitmap(currentBmp, 0, 0, currentBmp.getWidth(), currentBmp.getHeight(), matrix, true);
    }
}
