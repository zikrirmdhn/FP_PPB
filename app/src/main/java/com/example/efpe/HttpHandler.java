package com.example.efpe;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class HttpHandler {
    @RequiresApi(Build.VERSION_CODES.O)
    public JSONObject uploadImage(String requestUrl, byte[] imageByteArray) {
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", imageBase64);
        } catch (JSONException e) {
            Log.e("HttpHandler", "JSON Exception: " + e.getMessage());
            return null;
        }

        HttpURLConnection conn = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            Log.d("HttpHandler", "Starting uploadImage to URL: " + requestUrl);
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            writer.write(jsonObject.toString());
            writer.flush();

            int responseCode = conn.getResponseCode();
            Log.d("HttpHandler", "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Log.d("HttpHandler", "Response: " + response.toString());

                // Parse the response as JSON
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse;
                } catch (JSONException e) {
                    Log.e("HttpHandler", "JSONException while parsing response: " + e.getMessage());
                    return null;
                }
            } else {
                Log.e("HttpHandler", "HTTP error code: " + responseCode);
                return null;
            }
        } catch (MalformedURLException e) {
            Log.e("HttpHandler", "MalformedURLException: " + e.getMessage());
            return null;
        } catch (ProtocolException e) {
            Log.e("HttpHandler", "ProtocolException: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("HttpHandler", "IOException: " + e.getMessage());
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e("HttpHandler", "IOException closing writer: " + e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("HttpHandler", "IOException closing reader: " + e.getMessage());
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
