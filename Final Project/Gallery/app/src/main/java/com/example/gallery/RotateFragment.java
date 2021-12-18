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
    ImageButton rotateSquare, flipBtn;
    TextView valueRotate;
    Bitmap originalBmp, editBmp;
    Context context;
    static int degree;
    int squareAngel = 90;

    public RotateFragment(Context context, Bitmap originalBmp) {
        this.context = context;
        this.originalBmp = originalBmp;
        this.editBmp = originalBmp.copy(originalBmp.getConfig(), true);
        this.degree = 0;
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
                editBmp = rotate(90 + degree);
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
                editBmp = rotate((int) slider.getValue());
                ((EditImageActivity) context).onMsgFromFragToEdit("ROTATE-FLAG", editBmp);
            }
        });

        return rotateFragment;
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        if (null == result) {
            // TODO: not change display
            rotateSlider.setValue(0);
            valueRotate.setText("0");
            editBmp = originalBmp;
            degree = 0;
        }
        else {
            editBmp = result;
        }
    }

    // TODO: 90 + 90 = 180 -> rotate 180 -> not changed
    private Bitmap rotate(int value) {
        /*Matrix matrix = new Matrix();
        matrix.postRotate(value - degree);
        currentValue = value;
        return Bitmap.createBitmap(editBmp, 0, 0, editBmp.getWidth(), editBmp.getHeight(), matrix, true);*/

        int width = originalBmp.getWidth();
        int height = originalBmp.getHeight();

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
        matrix.postTranslate(-editBmp.getWidth()/2, -editBmp.getHeight()/2);
        matrix.postRotate(value - degree);
        // Save current angle
        degree = value;
        matrix.postTranslate(px, py);
        canvas.drawBitmap(editBmp, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG ));
        matrix.reset();

        return rotatedBitmap;
    }

    private Bitmap flip() {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        degree = degree * -1;
        return Bitmap.createBitmap(editBmp, 0, 0, editBmp.getWidth(), editBmp.getHeight(), matrix, true);
    }
}
