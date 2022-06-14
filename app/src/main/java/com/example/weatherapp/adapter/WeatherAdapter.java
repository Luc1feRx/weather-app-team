package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends BaseAdapter {
    List<Weather> list;
    Context context;
    int layout;

    public WeatherAdapter(Context context, int layout,List<Weather> list) {
        this.list = list;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout,null);
        TextView txtNgay =view.findViewById(R.id.txtCurrentTime);
        TextView txtWeatherState =view.findViewById(R.id.txtWeatherState);
        ImageView imgWeather =view.findViewById(R.id.imgWeather);
        TextView txtTempMax =view.findViewById(R.id.txtTempMax);
        TextView txtTempMin =view.findViewById(R.id.txtTempMin);

        Weather weather = list.get(i);
        txtNgay.setText(weather.getCurrentTime());
        txtWeatherState.setText(weather.getWeatherState());
        Picasso.get().load(weather.getImage()).into(imgWeather);
        txtTempMax.setText(weather.getTempMax() + " °C");
        txtTempMin.setText(weather.getTempMin() + " °C");
        return view;
    }
}
