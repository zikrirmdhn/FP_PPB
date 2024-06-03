package com.example.efpe;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class HttpHandler {
    @RequiresApi(Build.VERSION_CODES.O)
    public boolean uploadImage(String requestUrl, byte[] imageByteArray) {
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", imageBase64);
        } catch (Exception e) {
            Log.e("HttpHandler", "Exception: " + e.getMessage());
            return false;
        }

        try {
            boolean flag = false;
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();

            flag = conn.getResponseCode() == HttpURLConnection.HTTP_OK;
            conn.disconnect();
            return flag;
        } catch (MalformedURLException e) {
            Log.e("HttpHandler", "MalformedURLException: " + e.getMessage());
            return false;
        } catch (ProtocolException e) {
            Log.e("HttpHandler", "ProtocolException: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("HttpHandler", "IOException: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e("HttpHandler", "Exception: " + e.getMessage());
            return false;
        }
    }
}

