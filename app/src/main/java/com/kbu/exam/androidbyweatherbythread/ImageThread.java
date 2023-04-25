package com.kbu.exam.androidbyweatherbythread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageThread extends Thread{
    private Context context;
    private String baseUrl;
    public ImageThread(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }
    Bitmap bitmap;
    void setBitmap(String url) {
        this.baseUrl = url;
    }
    @Override
    public void run() {
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000);
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            Log.e("ERROR_TAG_IMG_THREAD", "오류 발생 [".concat(e.getMessage()).concat("]"));
        }

    }
    Bitmap resultBitmap() {
        return bitmap;
    }
}
