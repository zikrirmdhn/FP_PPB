package com.example.efpe;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        TextView classIdxTextView = findViewById(R.id.classIdxTextView);
        TextView messageTextView = findViewById(R.id.messageTextView);
        TextView recipeTitleTextView = findViewById(R.id.recipeTitleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView cookTimeTextView = findViewById(R.id.cookTimeTextView);

        // Get the response JSON string from the intent
        String jsonString = getIntent().getStringExtra("response_json");
        try {
            JSONObject response = new JSONObject(jsonString);

            // Parse the JSON and set the data to TextViews
            String classIdx = response.getString("class_idx");
            String message = response.getString("message");
            JSONObject recipe = response.getJSONObject("recipe");
            String title = recipe.getString("title");
            String description = recipe.getString("description");
            String cookTime = recipe.getString("cook_time");

            classIdxTextView.setText(classIdx);
            messageTextView.setText(message);
            recipeTitleTextView.setText(title);
            descriptionTextView.setText(description);
            cookTimeTextView.setText(cookTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
