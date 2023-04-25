package com.kbu.exam.androidbyweatherbythread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    int state = 1;
    int _state = 1;

    boolean trigger;

    String baseUrl = "http://192.168.0.27:8887/weather.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("중간수행평가");
        Switch glideSwitch = (Switch) findViewById(R.id.glideSwitch);
        TextView glideText = (TextView) findViewById(R.id.glideText);
        glideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    trigger = b;
                    glideText.setText("자체 통신 방법");
                } else {
                    trigger = b;
                    glideText.setText("Glide 사용");
                }
            }
        });
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _state = 1;
                viewResult();
            }
        });
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _state = 2;
                viewResult();
            }
        });
    }

    private void viewResult() {
        TextView viewSource = (TextView) findViewById(R.id.viewSource);
        ListView viewList = (ListView) findViewById(R.id.viewList);

        switch (state) {
            case 1:
                //Thread
                JsonThread thread = new JsonThread(baseUrl, MainActivity.this);
                switch (_state) {
                    case 1:
                        try {
                            onVisibilityViewer(true, false);
                            thread.start();
                            thread.join();
                            viewSource.setText(thread.resultData());
                        } catch (InterruptedException e) {
                            Toast.makeText(this, "오류 발생[".concat(e.getMessage()).concat("]"), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR_TAG_1_1", "오류 발생[".concat(e.getMessage()).concat("]"));
                        }
                        break;
                    case 2:
                        try {
                            onVisibilityViewer(false, true);
                            thread.start();
                            thread.join();
                            String json = thread.resultData();
                            ParsingJson parsingJson = new ParsingJson(MainActivity.this);
                            ArrayList<WeatherDTO> weatherDTOS = parsingJson.onParsingJsonData(json);
                            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, weatherDTOS, state, trigger);
                            viewList.setAdapter(customAdapter);
                        } catch (InterruptedException e) {
                            Log.e("ERROR_TAG_1_2", "오류 발생[".concat(e.getMessage()).concat("]"));
                            Toast.makeText(this, "오류 발생[".concat(e.getMessage()).concat("]"), Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                break;
            case 2:
                //Volley
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        switch (_state) {
                            case 1:
                                onVisibilityViewer(true, false);
                                viewSource.setText(response.toString());
                                break;
                            case 2:
                                onVisibilityViewer(false, true);
                                ParsingJson parsingJson = new ParsingJson(MainActivity.this, response);
                                ArrayList<WeatherDTO> weatherDTOS = parsingJson.onParsingJsonData("");
                                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, weatherDTOS, state, trigger);
                                viewList.setAdapter(customAdapter);
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR_TAG_VOLLEY", "에러 발생 [".concat(error.getMessage()).concat("]"));
                    }
                });
                queue.add(request);

                break;
        }
    }

    private void onVisibilityViewer(Boolean s1, Boolean s2) {
        ListView viewList = (ListView) findViewById(R.id.viewList);
        TextView viewSource = (TextView) findViewById(R.id.viewSource);
        ScrollView viewSourceScroll = (ScrollView) findViewById(R.id.viewSourceScroll);
        viewSourceScroll.setVisibility(s1 ? View.VISIBLE : View.GONE);
        viewSource.setVisibility(s1 ? View.VISIBLE : View.GONE);
        viewList.setVisibility(s2 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                state = 1;
                break;
            case R.id.item2:
                state = 2;
                break;
        }
        item.setChecked(true);
        return true;
    }
}