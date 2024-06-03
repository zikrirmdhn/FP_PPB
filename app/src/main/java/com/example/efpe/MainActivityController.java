package com.example.efpe;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityController {
    private final Context context;
    private final MainActivityCallback callback;

    public MainActivityController(Context context, MainActivity callback) {
        this.context = context;
        this.callback = callback;
    }

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public void saveImageFile(Intent intent) throws Exception {
        try {
            if (intent == null) {
                throw new Exception("Error passing image file!");
            }

            Bitmap imageBitmap = intent.getParcelableExtra("data", Bitmap.class);
            if (imageBitmap == null) {
                throw new Exception("Error passing image file!");
            }

            Uri dirUri;
            ContentResolver contentResolver = context.getContentResolver();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dirUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            } else {
                dirUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            ContentValues contentValues = new ContentValues();
            Date date = new Date();
            CharSequence dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.getTime());
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, dateNow + ".png");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/");

            Uri imageUri = contentResolver.insert(dirUri, contentValues);

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                Toast.makeText(context, "Image saved to the gallery", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                throw ex;
            }

            Toast.makeText(context, "Image has been saved to the Gallery", Toast.LENGTH_SHORT).show();

            // update the image view
            callback.updateImageView(imageBitmap);

            // show upload button
            callback.showUploadButton();

        } catch (Exception ex) {
            throw ex;
        }
    }

    public byte[] bitmapToByteArray(Bitmap imageBitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return byteStream.toByteArray();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public void uploadImage(String requestUrl, byte[] imageByteArray) {
        try {
            Executor executor = Executors.newSingleThreadExecutor();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            String uploadImageApi = requestUrl + "/api/upload-photo";

            Toast.makeText(context, "Uploading image...", Toast.LENGTH_SHORT).show();

            executor.execute(() -> {
                HttpHandler httpHandler = new HttpHandler();
                boolean result = httpHandler.uploadImage(uploadImageApi, imageByteArray);

                uiHandler.post(() -> {
                    if (result) {
                        Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } catch (Exception ex) {
            throw ex;
        }
    }
}

