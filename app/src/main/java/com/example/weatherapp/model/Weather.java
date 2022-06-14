package com.example.weatherapp.model;

public class Weather {
    private String CurrentTime,WeatherState,TempMax,TempMin,image;

    public Weather() {
    }

    public Weather(String currentTime, String weatherState, String tempMax, String tempMin, String image) {
        CurrentTime = currentTime;
        WeatherState = weatherState;
        TempMax = tempMax;
        TempMin = tempMin;
        this.image = image;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getWeatherState() {
        return WeatherState;
    }

    public void setWeatherState(String weatherState) {
        WeatherState = weatherState;
    }

    public String getTempMax() {
        return TempMax;
    }

    public void setTempMax(String tempMax) {
        TempMax = tempMax;
    }

    public String getTempMin() {
        return TempMin;
    }

    public void setTempMin(String tempMin) {
        TempMin = tempMin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "CurrentTime='" + CurrentTime + '\'' +
                ", WeatherState='" + WeatherState + '\'' +
                ", TempMax='" + TempMax + '\'' +
                ", TempMin='" + TempMin + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
