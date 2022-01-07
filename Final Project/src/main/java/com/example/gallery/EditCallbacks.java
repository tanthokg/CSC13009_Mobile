package com.example.gallery;

import android.graphics.Bitmap;

public interface EditCallbacks {
    public void onMsgFromFragToEdit(String sender, String request, Bitmap bitmap);
}
