package com.example.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.adapter.WeatherAdapter;
import com.example.weatherapp.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class days5_3hour extends AppCompatActivity {

    static final String API_KEY = "40c82e62884150aa6636b44e13562407";
    TextView txtCity;
    String city ="";
    WeatherAdapter weatherAdapter;
    ListView lvWeather;
    List<Weather> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days53hour);
        lvWeather = findViewById(R.id.lvWeather5days);
        txtCity = findViewById(R.id.txtCity5days);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        txtCity.setText("Thành phố "+city);
        getJson5days();
        //list.add(new Weather("Thứ 7","20-12-2022","Clouds","20","20","http://openweathermap.org/img/wn/04d.png"));
        weatherAdapter = new WeatherAdapter(this,R.layout.item_weather,list);
        lvWeather.setAdapter(weatherAdapter);
    }

    private void getJson5days() {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&lang=vi&appid="+API_KEY;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listData = response.getJSONArray("list");
                            for (int i = 0; i < listData.length(); i++) {
                                JSONObject weatherObj = listData.getJSONObject(i);

                                String sNgay = weatherObj.getString("dt");
                                long lNgay = Long.parseLong(sNgay);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("   EEEE\ndd-MM-yyyy");
                                Date date = new Date(lNgay*1000);
                                String currentTime = dateFormat.format(date);//ngày giờ cập nhật

                                JSONObject main = weatherObj.getJSONObject("main");
                                String tempMax = main.getString("temp_max");// nhiệt độ max
                                String tempMin = main.getString("temp_min");// nhiệt độ min

                                JSONArray weatherArray = weatherObj.getJSONArray("weather");
                                JSONObject object = weatherArray.getJSONObject(0);
                                String icon = object.getString("icon");
                                String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";//icon thời tiết
                                String stateWeather = object.getString("description");//tt thời tiết

                                list.add(new Weather(currentTime,stateWeather,tempMax,tempMin,urlIcon));
                                weatherAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(days5_3hour.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}