package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class EditImageView extends View {

    private Paint paint;
    private Path path;
    private Bitmap editBitmap, viewBitmap;
    private Canvas customCanvas;
    private float dx, dy;

    public EditImageView(Context context) {
        super(context);
        init();
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        customCanvas = new Canvas(viewBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();

        if (editBitmap != null) {
            canvas.drawBitmap(editBitmap, rect.left, rect.top, null);
            canvas.drawBitmap(viewBitmap, rect.left, rect.top, null);
        }
    }

    public void setBitmapResource(Bitmap resource, int widthView, int heightView) {
        int widthBitmap = resource.getWidth();
        int heightBitmap = resource.getHeight();
        float scaleWidth = (float) (widthView * 1.0 / resource.getWidth());
        float scaleHeight = (float) (heightView * 1.0 / resource.getHeight());
        float scale = Math.min(scaleWidth, scaleHeight);
        int width = (int) (widthBitmap * scale);
        int height = (int) (heightBitmap * scale);

        this.editBitmap = Bitmap.createScaledBitmap(resource, width, height, true);
        invalidate();
    }

    public Bitmap getBitmapResource() {
        return editBitmap;
    }
}
