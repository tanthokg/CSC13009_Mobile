package com.example.gallery;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

public  class RoundedCornerFragment extends Fragment implements FragmentCallbacks {

    Slider roundedCornerSlider;
    ImageButton btnClearRounded, btnCheckRounded;
    TextView valueRoundedCorner;
    Bitmap originalBmp, editBmp, currentBmp;
    Context context;
   static int degree;

    public RoundedCornerFragment(Context context, Bitmap originalBmp) {
        this.context = context;
        this.originalBmp = originalBmp;
        this.editBmp = originalBmp.copy(originalBmp.getConfig(), true);
        this.currentBmp = editBmp;
      degree = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View roundedConnerFragment = inflater.inflate(R.layout.rounded_corner_fragment, container, false);
        roundedCornerSlider = (Slider) roundedConnerFragment.findViewById(R.id.slider_rounded_corner);
        valueRoundedCorner = (TextView) roundedConnerFragment.findViewById(R.id.valueRoundedCorner);
        btnClearRounded = (ImageButton) roundedConnerFragment.findViewById(R.id.btnClearRounded);
        btnCheckRounded = (ImageButton) roundedConnerFragment.findViewById(R.id.btnCheckRounded);

        btnClearRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundedCornerSlider.setValue(0);
                valueRoundedCorner.setText("0");
                currentBmp = editBmp;
            degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROUNDED-FLAG", "CLEAR", editBmp);
            }
        });
        btnCheckRounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = 0;
                ((EditImageActivity) context).onMsgFromFragToEdit("ROUNDED-FLAG", "SAVE", currentBmp);
            }
        });
        roundedCornerSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                valueRoundedCorner.setText(String.valueOf((int) value));
            }
        });
        roundedCornerSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                currentBmp = roundedCorner((int) slider.getValue());
                ((EditImageActivity) context).onMsgFromFragToEdit("ROUNDED-FLAG", "UPDATE", currentBmp);
            }
        });

        return roundedConnerFragment;
    }

    @Override
    public  void onResume()
    {
        super.onResume();
        roundedCornerSlider.setValue(0);
        valueRoundedCorner.setText("0");
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        degree=0;
        if (null == result)
        {
            editBmp = originalBmp;
            currentBmp = originalBmp;

        }
        else {
            editBmp = result;
            currentBmp = result;
        }
    }


    private Bitmap roundedCorner(int value)
    {

        // image size
        int width = currentBmp.getWidth();
        int height = currentBmp.getHeight();
        // create bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // set canvas for painting
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        // config paint
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        // config rectangle for embedding
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        // draw rect to canvas
        canvas.drawRoundRect(rectF, value, value , paint);

        // create Xfer mode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // draw source image to canvas
        canvas.drawBitmap(editBmp, rect, rect, paint);

        // return final image
        return result;

    }
}