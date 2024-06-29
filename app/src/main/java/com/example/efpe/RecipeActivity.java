package com.example.efpe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        Button scanMore = findViewById(R.id.scanMorebutton);
        ImageView recipeImageView = findViewById(R.id.recipeImageView);
        TextView classIdxTextView = findViewById(R.id.classIdxTextView);
        TextView recipeTitleTextView = findViewById(R.id.recipeTitleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView cookTimeTextView = findViewById(R.id.cookTimeTextView);
        ListView ingredients = findViewById(R.id.list_ingredient);
        ListView step = findViewById(R.id.list_step);

        String jsonString = getIntent().getStringExtra("response_json");
        try {
            JSONObject response = new JSONObject(jsonString);

            // Parse the JSON and set the data to TextViews
            String classIdx = response.getString("class_idx");
            JSONObject recipe = response.getJSONObject("recipe");
            String title = recipe.getString("title");
            String description = recipe.getString("description");
            String cookTime = recipe.getString("cook_time");
            String imageUrl = recipe.getString("image_url");


            classIdxTextView.setText(classIdx);
            recipeTitleTextView.setText(title);
            descriptionTextView.setText(description);
            cookTimeTextView.setText(cookTime);

            JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
            ArrayList<String> ingredientsList = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                ingredientsList.add(ingredientsArray.getString(i));
            }
            ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredientsList);
            ingredients.setAdapter(ingredientsAdapter);


            JSONArray stepsArray = recipe.getJSONArray("directions");
            ArrayList<String> stepsList = new ArrayList<>();
            for (int i = 0; i < stepsArray.length(); i++) {
                stepsList.add(stepsArray.getString(i));
            }
            ArrayAdapter<String> stepsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stepsList);
            step.setAdapter(stepsAdapter);

            new DownloadImageTask(recipeImageView).execute(imageUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        scanMore.setOnClickListener(v ->{
            Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
