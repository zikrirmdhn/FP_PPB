package com.example.efpe;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityController {
    private final Context context;
    private final MainActivityCallback callback;

    public MainActivityController(Context context, MainActivityCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @SuppressLint("Recycle")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void saveImageFile(Intent intent) {
        try {
            if (intent == null) {
                throw new Exception("Error passing image file!");
            }

            Bitmap imageBitmap = intent.getParcelableExtra("data");
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
            String dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.getTime()).toString();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, dateNow + ".png");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/");

            Uri imageUri = contentResolver.insert(dirUri, contentValues);

            try {
                Objects.requireNonNull(contentResolver.openOutputStream(imageUri)).write(imageBitmap.getRowBytes());
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
            try {
                throw ex;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public byte[] bitmapToByteArray(Bitmap imageBitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return byteStream.toByteArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadImage(String requestUrl, byte[] imageByteArray) {
        try {
            Executor executor = Executors.newSingleThreadExecutor();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            String uploadImageApi = requestUrl + "/upload-photo";

            Toast.makeText(context, "Uploading image...", Toast.LENGTH_SHORT).show();

            executor.execute(() -> {
                HttpHandler httpHandler = new HttpHandler();
                JSONObject result = httpHandler.uploadImage(uploadImageApi, imageByteArray);

                uiHandler.post(() -> {
                    if (result != null) {
                        Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show();

                        // Start the new activity and pass the response data
                        Intent intent = new Intent(context, RecipeActivity.class);
                        intent.putExtra("response_json", result.toString());
                        context.startActivity(intent);

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
