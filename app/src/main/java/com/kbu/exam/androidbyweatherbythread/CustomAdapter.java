package com.kbu.exam.androidbyweatherbythread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<WeatherDTO> {
    private Context context;
    private int state;
    private boolean tg;
    private ArrayList<WeatherDTO> weathers;

    public CustomAdapter(@NonNull Context context, ArrayList<WeatherDTO> weathers, int state, boolean tg) {
        super(context, R.layout.item, weathers);
        this.context = context;
        this.weathers = weathers;
        this.state = state;
        this.tg = tg;
    }
    public WeatherDTO getItem(int position) {
        return weathers.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        WeatherDTO weather = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item, null);
        holder.imageView = convertView.findViewById(R.id.image);
        holder.count = convertView.findViewById(R.id.count);
        holder.temp = convertView.findViewById(R.id.temp);
        holder.imageView2 = convertView.findViewById(R.id.image2);
        ImageThread imageThread = new ImageThread(context, weather.getFlag());
        holder.count.setText(weather.getCountry());
        holder.temp.setText(weather.getTemperature() + "'C");
        if(tg) {
            Glide.with(context)
                    .load(weather.getFlag())
                    .into(holder.imageView);
//            Toast.makeText(context, "글라이드 사용중", Toast.LENGTH_SHORT).show();
        } else {
            if (state == 1) {
                try {
                    imageThread.start();
                    imageThread.join();
                    imageThread.setBitmap(weather.getFlag());
                    holder.imageView.setImageBitmap(imageThread.resultBitmap());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                RequestQueue queue = Volley.newRequestQueue(context);
                ImageRequest request = new ImageRequest(weather.getFlag(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR_TAG_IMG_VOLLEY", "오류 발생 [".concat(error.getMessage()).concat("]"));
                    }
                });
                queue.add(request);
            }
        }
        if (weather.getWeather().equals("맑음")){
            holder.imageView2.setImageResource(R.drawable.nb01);
        } else if (weather.getWeather().equals("비")) {
            holder.imageView2.setImageResource(R.drawable.nb08);
        } else if (weather.getWeather().equals("흐림")) {
            holder.imageView2.setImageResource(R.drawable.nb04);
        } else if (weather.getWeather().equals("우박")) {
            holder.imageView2.setImageResource(R.drawable.nb07);
        } else {
            holder.imageView2.setImageResource(R.drawable.nb11);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView count;
        TextView temp;
        ImageView imageView2;
    }
}
