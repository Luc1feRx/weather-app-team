package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Adapter_5days extends RecyclerView.Adapter<Adapter_5days.ViewHolder> {
    List<Weather> weathers;
    Context context;

    public Adapter_5days(List<Weather> weathers, Context context) {
        this.weathers = weathers;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_5days.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_5days,parent,false);
        return new Adapter_5days.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_5days.ViewHolder holder, int position) {
        Weather weather = weathers.get(position);
        holder.txtNgay.setText(weather.getCurrentTime());
//        holder.txtWeatherState.setText(weather.getWeatherState());
        Picasso.get().load(weather.getImage()).into(holder.imgWeather);
        Double tempMax = Double.valueOf(weather.getTempMax()) / 10;
        Double tempMin = Double.valueOf(weather.getTempMin()) / 10;
        holder.txtTempMax.setText(String.valueOf(tempMax.intValue()) + " °C");
        holder.txtTempMin.setText(String.valueOf(tempMin.intValue()) + " °C");
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNgay;
//        TextView txtWeatherState;
        ImageView imgWeather;
        TextView txtTempMax;
        TextView txtTempMin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNgay =itemView.findViewById(R.id.txtCurrentTime);
//            txtWeatherState =itemView.findViewById(R.id.txtWeatherState);
            imgWeather =itemView.findViewById(R.id.imgWeather);
            txtTempMax =itemView.findViewById(R.id.txtTempMax);
            txtTempMin =itemView.findViewById(R.id.txtTempMin);

        }
    }
}
