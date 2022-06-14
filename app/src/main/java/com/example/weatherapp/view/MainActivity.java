package com.example.weatherapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.adapter.Adapter_5days;
import com.example.weatherapp.adapter.WeatherAdapter;
import com.example.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static final String API_KEY = "40c82e62884150aa6636b44e13562407";
    private static final Integer CHANNEL_ID = 1;
    EditText edtCity;
    Button btnOK,btnStep, btnMap;
    ImageView imgTT;
    TextView txtNameTP,txtNameQG,txtNhietdo,txtTT,txtCloud,txtWind,txtSteam,txtDate, txtco, txtno, txtno2,txto3, txtso2, txtnh3, txtState;
    String city ="";
    String cityIntent = "";
    List<Weather> list1 = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter_5days adapter_5days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        //lvWeather5days.setNestedScrollingEnabled(false);//tắt thanh trượt
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setNestedScrollingEnabled(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = edtCity.getText().toString().trim();
                if (city.equals("")){
                    city ="hanoi";
                }else{
                    getJsonWeather(city);
                    getJsonWeather5days(city);
                    getJsonAirPollution(city);
                }
            }
        });

        if (city == ""){
            getJsonWeather("hanoi");
            getJsonWeather5days("hanoi");
            getJsonAirPollution("hanoi");
        }else{
            getJsonWeather(city);
            getJsonWeather5days(city);
            getJsonAirPollution(city);
        }

        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TempNextDay.class);
                intent.putExtra("city",cityIntent.toLowerCase(Locale.ROOT));
                startActivity(intent);
            }
        });

        //5days/3hour


        //map
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });


    }

    public void sendNotification(String name, String temp, String temperState){
        //create notification
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goodweather);
        Notification notification = new NotificationCompat.Builder(this, com.example.weatherapp.view.Notification.CHANNEL_ID)
                .setContentTitle(name)
                .setContentText(temperState + " " + temp+"°C")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.cardview_light_background))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1, notification);
        }
    }




    public void getJsonWeather(String city){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric&lang=vi";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);

                            String icon = weatherObj.getString("icon");
                            String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";
                            Picasso.get().load(urlIcon).into(imgTT);
                            String temperState = weatherObj.getString("main");
                            txtTT.setText(temperState);

                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");//nhiet do
                            txtNhietdo.setText(temp+"°C");
                            String humidity = main.getString("humidity");//độ ẩm
                            txtSteam.setText(humidity+"%");

                            JSONObject wind = response.getJSONObject("wind");
                            String windSpeed = wind.getString("speed");//tốc độ gió
                            txtWind.setText(windSpeed+" m/s");

                            JSONObject cloud = response.getJSONObject("clouds");
                            String all = cloud.getString("all");// % mây
                            txtCloud.setText(all +" %");

                            String sNgay = response.getString("dt");
                            long lNgay = Long.parseLong(sNgay);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd-MM-yyyy HH:mm:ss");
                            Date date = new Date(lNgay*1000);
                            String currentTime = dateFormat.format(date);//ngày giờ cập nhật
                            txtDate.setText(currentTime);

                            String name = response.getString("name");//thành phố
                            cityIntent = name;
                            txtNameTP.setText("Thành phố "+name);

                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");//Quốc gia
                            txtNameQG.setText("Quốc gia "+country);

                            sendNotification(name, temp, temperState);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố "+city, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    public void getJsonWeather5days(String city){
        String url1 = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&lang=vi&appid="+API_KEY;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listData = response.getJSONArray("list");
                            for (int i = 0; i < listData.length(); i++) {
                                JSONObject weatherObj = listData.getJSONObject(i);

                                String sNgay = weatherObj.getString("dt_txt");
//                                long lNgay = Long.parseLong(sNgay);
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("   EEEE\ndd-MM-yyyy");
//                                Date date = new Date(lNgay*1000);
//                                String currentTime = dateFormat.format(date);//ngày giờ cập nhật

                                JSONObject main = weatherObj.getJSONObject("main");
                                String tempMax = main.getString("temp_max");// nhiệt độ max
                                String tempMin = main.getString("temp_min");// nhiệt độ min

                                JSONArray weatherArray = weatherObj.getJSONArray("weather");
                                JSONObject object = weatherArray.getJSONObject(0);
                                String icon = object.getString("icon");
                                String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";//icon thời tiết
                                String stateWeather = object.getString("description");//tt thời tiết

                                Double maxtemp = Double.valueOf(tempMax);
                                String MaxTempString = String.valueOf(maxtemp.intValue());

                                Double mintemp = Double.valueOf(tempMin);
                                String MinTempString = String.valueOf(mintemp.intValue());

                                list1.add(new Weather(sNgay,stateWeather,MaxTempString,MinTempString,urlIcon));
                                adapter_5days = new Adapter_5days(list1,MainActivity.this);
                                recyclerView.setAdapter(adapter_5days);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố "+city, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void getJsonAirPollution(String city){
        String getLocateCity = "http://api.openweathermap.org/geo/1.0/direct?q="+city+"&limit=1&appid="+API_KEY;
        Log.d("TAG", getLocateCity);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getLocateCity, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    String getlat = jsonObject.getString("lat");
                    String getlon = jsonObject.getString("lon");
                    String getAirpollution = "http://api.openweathermap.org/data/2.5/air_pollution?lat="+getlat+"&lon="+getlon+"&appid="+API_KEY;
                    Log.d("d", getAirpollution);
                    RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getAirpollution, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list");
                                JSONObject object = jsonArray.getJSONObject(0);
                                JSONObject jsonObjectComponents = object.getJSONObject("components");
                                JSONObject jsonObjectMain = object.getJSONObject("main");
                                String aqi = jsonObjectMain.getString("aqi");
                                Integer state = Integer.valueOf(aqi);
                                if(state == 5){
                                    txtState.setText("Quality index: 5 (Very Poor)");
                                    txtState.setTextColor(Color.parseColor("#ba2014"));
                                }else if(state == 4){
                                    txtState.setText("Quality index: 4 (Poor)");
                                    txtState.setTextColor(Color.parseColor("#f5584c"));
                                }else if(state == 3){
                                    txtState.setText("Quality index: 3 (Moderate)");
                                    txtState.setTextColor(Color.parseColor("#e1e80e"));
                                }else if(state == 2){
                                    txtState.setText("Quality index: 2 (Fair)");
                                    txtState.setTextColor(Color.parseColor("#9fe80e"));
                                }else if(state == 1){
                                    txtState.setText("Quality index: 1 (Good)");
                                    txtState.setTextColor(Color.parseColor("#07fa24"));
                                }
                                String getco = jsonObjectComponents.getString("co");
                                txtco.setText(getco + "μg/m3");
                                String getno = jsonObjectComponents.getString("no");
                                txtno.setText(getno + "μg/m3");
                                String getno2 = jsonObjectComponents.getString("no2");
                                txtno2.setText(getno2 + "μg/m3");
                                String geto3 = jsonObjectComponents.getString("o3");
                                txto3.setText(geto3 + "μg/m3");
                                String getso2 = jsonObjectComponents.getString("so2");
                                txtso2.setText(getso2 + "μg/m3");
                                String getnh3 = jsonObjectComponents.getString("nh3");
                                txtnh3.setText(getnh3 + "μg/m3");
                                Log.d("getAirPollution", getco);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           Toast.makeText(MainActivity.this, "Không có dữ liệu không khí cho thành phố "+city, Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue1.add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void mapping() {
        edtCity = findViewById(R.id.edtCity);
        btnOK = findViewById(R.id.btnOK);
        btnStep = findViewById(R.id.btnStep);
        txtNameTP = findViewById(R.id.txtNameTP);
        txtNameQG = findViewById(R.id.txtNameQG);
        txtNhietdo = findViewById(R.id.txtNhietdo);
        txtTT = findViewById(R.id.txtTT);
        txtCloud = findViewById(R.id.txtCloud);
        txtWind = findViewById(R.id.txtWind);
        txtSteam = findViewById(R.id.txtSteam);
        txtDate = findViewById(R.id.txtDate);
        imgTT = findViewById(R.id.imgTT);
        recyclerView = findViewById(R.id.lvWeather5days);

        //mapping air pollution
        txtco = findViewById(R.id.txtco);
        txtno = findViewById(R.id.txtno);
        txtno2 = findViewById(R.id.txtno2);
        txto3 = findViewById(R.id.txto3);
        txtso2 = findViewById(R.id.txtso2);
        txtnh3 = findViewById(R.id.txtnh3);
        txtState = findViewById(R.id.txtstate);

        //map
        btnMap = findViewById(R.id.btnmap);
    }
}