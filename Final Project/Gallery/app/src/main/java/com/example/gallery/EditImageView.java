package com.example.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EditImageView extends View {

    private Paint paint;
    private Path path;
    private Bitmap editBitmap;
    private Canvas customCanvas;
    private Matrix matrix;
    private ColorMatrix colorMatrix;
    private float dx, dy;
    private int angleRotate;
    private int angleRounded;
    private List<Bitmap> listBitmap;
    private List<Path> listPath;
    private boolean isRotate, isBrush, isRounded;
    private float[] originalCMatrix = new float[] {
        1, 0, 0, 0, 0,
        0, 1, 0, 0, 0,
        0, 0, 1, 0, 0,
        0, 0, 0, 1, 0 };
    private List<float[]> colorMatrixList;

    public EditImageView(Context context) {
        super(context);
        init();
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
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

        matrix = new Matrix();
        listBitmap = new ArrayList<Bitmap>();
        listPath = new ArrayList<Path>();
        isRotate = false;
        isBrush = false;
        isRounded = false;

        colorMatrix = new ColorMatrix(originalCMatrix);
        //prevCMatrix = originalCMatrix;
        colorMatrixList = new ArrayList<float[]>();
        colorMatrixList.add(originalCMatrix);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isRotate) {
            Paint mPaint = new Paint();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(mPaint);
            isRotate = false;
        }
//        if (isRounded) {
//            Paint mPaint = new Paint();
//            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            canvas.drawPaint(mPaint);
//            isRounded = false;
//        }

        if (editBitmap != null) {
            matrix.setRotate(angleRotate, editBitmap.getWidth()/2, editBitmap.getHeight()/2);

            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(editBitmap, matrix, paint);


        }
    }

    public void setBitmapResource(Bitmap resource, int widthView, int heightView) {
        int widthBitmap = resource.getWidth();
        int heightBitmap = resource.getHeight();
        float scaleWidth = (float) (widthView * 1.0 / resource.getWidth());
        float scaleHeight = (float) (heightView * 1.0 / resource.getHeight());
        float scale = Math.min(scaleWidth, scaleHeight);
        int width = (int) (widthBitmap * scaleWidth);
        int height = (int) (heightBitmap * scale);

        this.editBitmap = Bitmap.createScaledBitmap(resource, width, height, true);
        customCanvas = new Canvas(editBitmap);
        invalidate();
    }

    public Bitmap getBitmapResource() {
        return editBitmap;
    }

    public void setAngleRotate(int rotate) {
        angleRotate = rotate;
        isRotate = true;
        invalidate();
    }
    public void setAngleRounded(int rounded) {
        angleRounded = rounded;
        rounded(angleRounded);
        isRounded = true;
        invalidate();
    }

    public void reset() {
        angleRotate = 0;
        angleRounded = 0;
        isBrush = false;
        colorMatrix.set(originalCMatrix);
        colorMatrixList.clear();
        colorMatrixList.add(originalCMatrix);
        invalidate();
    }

    public void clearRotate() {
        angleRotate = 0;

        invalidate();
    }
    public void clearRoundedCorner() {
        angleRounded = 0;

        invalidate();
    }

    public void flip() {
        matrix.postScale(-1, 1);
        angleRotate = angleRotate * -1;
        invalidate();
    }

    public void rounded(int angleRounded)
    {
        // image size
        int width = editBitmap.getWidth();
        int height = editBitmap.getHeight();
        // create bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // set canvas for painting
        Canvas _canvas = new Canvas(result);
        _canvas.drawARGB(0, 0, 0, 0);

        // config paint
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        // config rectangle for embedding
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        // draw rect to canvas
        _canvas.drawRoundRect(rectF, angleRounded, angleRounded , paint);

        // create Xfer mode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // draw source image to canvas
        _canvas.drawBitmap(editBitmap, rect, rect, paint);
    }
    public void saveImage() {
        addLastBitmap(getEditBitmap());
        isBrush = false;
        colorMatrixList.clear();
        colorMatrixList.add(colorMatrix.getArray());
        invalidate();
    }

    public void addLastBitmap(Bitmap bitmap) {
        listBitmap.add(bitmap);
    }

    public Bitmap getEditBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(true);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isBrush) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    addLastBitmap(getEditBitmap());
                    break;
            }

            return true;
        }
        return false;
    }

    private void touchUp() {
        path.reset();
    }

    private void touchMove(float x, float y) {
        float spaceX = Math.abs(x - dx);
        float spaceY = Math.abs(y - dy);

        if (spaceX >= 5 || spaceY >= 5) {
            path.quadTo(x, y, (x+dx)/2, (y+dy)/2);
            dx = x;
            dy = y;
            customCanvas.drawPath(path, paint);
            listPath.add(path);
            invalidate();
        }
    }

    private void touchStart(float x, float y) {
        path.moveTo(x, y);
        dx = x;
        dy = y;
    }

    public void enableBrush() {
        paint.setXfermode(null);
        paint.setShader(null);
        paint.setMaskFilter(null);
        isBrush = true;
    }

    public void clearBrush() {
        /*paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        for (Path drawnPath : listPath) {
            customCanvas.drawPath(drawnPath, paint);
        }
        listPath.clear();
        invalidate();*/
    }

    public void setColorMatrix(ColorMatrix cMatrix) {
        colorMatrixList.add(cMatrix.getArray());
        colorMatrix = cMatrix;
        invalidate();
    }

    public void clearFilter() {
        colorMatrix = new ColorMatrix(colorMatrixList.get(0));
        colorMatrixList.clear();
        colorMatrixList.add(colorMatrix.getArray());
        invalidate();
    }
}
