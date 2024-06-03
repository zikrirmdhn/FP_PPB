package com.example.efpe;

import android.graphics.Bitmap;

public interface MainActivityCallback {
    void updateImageView(Bitmap imageBitmap);
    void showUploadButton();
}