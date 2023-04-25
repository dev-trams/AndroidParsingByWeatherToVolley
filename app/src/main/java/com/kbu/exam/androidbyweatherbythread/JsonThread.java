package com.kbu.exam.androidbyweatherbythread;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonThread extends Thread {

    private final String baseUrl;
    private final Context context;

    public JsonThread(String baseUrl, Context context) {
        this.baseUrl = baseUrl;
        this.context = context;
    }

    private String result = "";
    @Override
    public void run() {
        try {
            String line = "";

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            while ((line = reader.readLine()) != null) {
                result += line + '\n';
            }
            reader.close();
            streamReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            Log.e("ERROR_TAG_THREAD", "오류 발생 [".concat(e.getMessage()).concat("]"));
        }
    }

    public String resultData() {
        return result;
    }
}
