package com.geek.hw.androidweather;

public class CityModel {
    public final static String NAME = "name";
    public final static String TEMPERATURE = "temperature";
    public final static String WEATHER = "weather";
    private String cityName;
    private int temperature;
    private Weather weather;


    public CityModel(String cityName) {
        this.cityName = cityName;
        this.temperature = -5 + (int) (Math.random() * 30);
        Weather[] weathers = Weather.values();
        this.weather = weathers[(int) (Math.random() * weathers.length)];
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

}

enum Weather {
    SUNNY,
    CLOUDY,
    RAIN,
    STORM,
    SNOW
}