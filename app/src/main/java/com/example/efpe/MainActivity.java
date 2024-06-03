package com.example.efpe;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;

import androidx.core.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityCallback {

    private MainActivityController controller;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private ImageView cameraView;
    private Button takePictureButton;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainActivityController(this, this);
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleCameraResult);

        cameraView = findViewById(R.id.Camera_view);
        takePictureButton = findViewById(R.id.Btn_take_pict);
        uploadButton = findViewById(R.id.Btn_upload);

        takePictureButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Btn_take_pict) {
            takePicture();
        } else if (v.getId() == R.id.Btn_upload) {
            uploadImage();
        }
    }


    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    private void uploadImage() {
        try {
            Drawable drawable = cameraView.getDrawable();
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                byte[] imageByteArray = controller.bitmapToByteArray(bitmap);
                controller.uploadImage(getString(R.string.BACKEND_URL), imageByteArray);
            } else {
                // Handle case when drawable is null
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void handleCameraResult(@NonNull ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                //controller.saveImageFile(data);
            }
        }
    }

    @Override
    public void updateImageView(Bitmap imageBitmap) {
        cameraView.setImageBitmap(imageBitmap);
    }

    @Override
    public void showUploadButton() {
        uploadButton.setVisibility(View.VISIBLE);
    }
}