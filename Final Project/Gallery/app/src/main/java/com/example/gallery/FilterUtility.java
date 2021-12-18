package com.example.gallery;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class FilterUtility {

    ColorMatrix selected;

    public FilterUtility() {
        this.selected = new ColorMatrix(new float[20]);
    }

    public Bitmap setFilter(Bitmap bitmap, String name) {
        Bitmap editedBitmap = bitmap.copy(bitmap.getConfig(), true);
        ColorMatrix colorMatrix = new ColorMatrix();
        setColorMatrix(name, colorMatrix);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        Canvas canvas = new Canvas(editedBitmap);
        canvas.drawBitmap(editedBitmap,0, 0, paint);
        return editedBitmap;
    }

    private void setColorMatrix(String name, ColorMatrix colorMatrix) {
        switch (name) {
            case "No Effect":
                setNoEffect(colorMatrix);
                break;
            case "Auto":
                setAuto(colorMatrix);
                break;
            case "Cream":
                setCream(colorMatrix);
                break;
            case "Forest":
                setForest(colorMatrix);
                break;
            case "Cozy":
                setCozy(colorMatrix);
                break;
            case "Blossom":
                setBlossom(colorMatrix);
                break;
            case "Evergreen":
                setEvergreen(colorMatrix);
                break;
            case "Grayscale":
                setGrayscale(colorMatrix);
                break;
            case "Sharpen":
                setSharpen(colorMatrix);
                break;
            case "Vintage":
                setVintage(colorMatrix);
                break;
            default:
                break;
        }
    }

    private void setNoEffect(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0
        });
    }

    private void setAuto(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                1.2f, 0, 0, 0, 0,
                0, 1.2f, 0, 0, 0,
                0, 0, 1.2f, 0, 0,
                0, 0, 0, 1, 0
        });
    }

    private void setCream(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0.8f, 0, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0.8f, 0, 0,
                0, 0, 0, 1, 0
        });
    }

    private void setForest(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0.5f, 0, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0.8f, 0, 50,
                0, 0, 0, 1, 0
        });
    }

    private void setCozy(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0.75f, 0, 0, 0, 0,
                0, 0, 0.75f, 0, 0,
                0, 0, 0, 0, 50,
                0.5f, 0, 0, 0, 0
        });
    }

    private void setBlossom(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0.5f, 0, 0, 0, 0,
                0, 0.5f, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0, 0.5f, 0
        });
    }

    private void setEvergreen(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0, 0, 0, 0, 100,
                0, 1, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0, 0.5f, 0
        });
    }

    private void setGrayscale(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                0.33f, 0.33f, 0.33f, 0, 0,
                0.33f, 0.33f, 0.33f, 0, 0,
                0.33f, 0.33f, 0.33f, 0, 0,
                0, 0, 0, 1, 0
        });
    }

    private void setSharpen(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1.5f, 0
        });
    }

    private void setVintage(ColorMatrix colorMatrix) {
        colorMatrix.set(new float[] {
                1, 0, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0.5f, 0, 0,
                0, 0, 0, 0.5f, 0
        });
    }

    public void setSelectedFilter(String filter) {
        setColorMatrix(filter, selected);
    }

    private ColorMatrix setModify() {
        float[] newColorMatrixArray = selected.getArray();
        float[] modifyColorMatrixArray = new float[20];
        float[] original = {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0
        };

        // Tìm sự chênh lệch giữa mã màu của ảnh gốc và ảnh được chỉnh sửa
        for (int i = 0; i < newColorMatrixArray.length; i++) {
            modifyColorMatrixArray[i] = original[i] - newColorMatrixArray[i];
        }
        return new ColorMatrix(modifyColorMatrixArray);
    }

    public void reset(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(setModify()));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap,0, 0, paint);
    }
}
