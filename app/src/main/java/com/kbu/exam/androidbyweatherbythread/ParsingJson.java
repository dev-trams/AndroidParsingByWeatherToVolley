package com.kbu.exam.androidbyweatherbythread;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingJson {
    Context context;
    JSONArray volleyArray;
    public ParsingJson(Context context) {
        this.context = context;
    }
    public ParsingJson(Context context, JSONArray volleyArray) {
        this.context = context;
        this.volleyArray= volleyArray;
    }
    public ArrayList<WeatherDTO> onParsingJsonData(String json) {
        WeatherDTO weatherDTO = null;
        ArrayList<WeatherDTO> weatherDTOS = new ArrayList<>();
        try {
            JSONArray rootArray = volleyArray!=null ? volleyArray : new JSONArray(json);
            for (int i = 0; i < rootArray.length(); i++) {
                JSONObject rootObject = rootArray.getJSONObject(i);
                String country = rootObject.getString(rootObject.names().getString(0));
                String flagImgUrl = rootObject.getString(rootObject.names().getString(1));
                String weather = rootObject.getString(rootObject.names().getString(2));
                String temp = rootObject.getString(rootObject.names().getString(3));
                weatherDTO = new WeatherDTO(country, flagImgUrl, weather, temp);
                weatherDTOS.add(weatherDTO);
            }
        } catch (JSONException e) {
            Toast.makeText(context, "오류 발생 [".concat(e.getMessage()).concat("]"), Toast.LENGTH_SHORT).show();
            Log.e("ERROR_TAG_JSON", "오류 발생 [".concat(e.getMessage()).concat("]"));
        }
        return weatherDTOS;
    }
}
