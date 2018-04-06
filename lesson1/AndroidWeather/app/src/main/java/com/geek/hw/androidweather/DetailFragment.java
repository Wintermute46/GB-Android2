package com.geek.hw.androidweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailFragment extends Fragment {

    private TextView cityName, cityWeather, cityTemperature;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        cityName = view.findViewById(R.id.detail_city_name);
        cityName.setText(getArguments().getString(CityModel.NAME));

        cityWeather = view.findViewById(R.id.detail_weather);
        String weather = getArguments().getString(CityModel.WEATHER);

        cityTemperature = view.findViewById(R.id.detail_temperature);
        String temp = getArguments().getInt(CityModel.TEMPERATURE) + getResources().getString(R.string.weather_temperature_unit_c);
        cityTemperature.setText(temp);

        imageView = view.findViewById(R.id.detail_image_weather);
        imageView.setImageResource(R.drawable.ic_weather_rain);

        switch (Weather.valueOf(weather)) {
            case SUNNY:
                imageView.setImageResource(R.drawable.ic_weather_sunny);
                cityWeather.setText(getResources().getString(R.string.weather_sunny));
                break;
            case CLOUDY:
                imageView.setImageResource(R.drawable.ic_weather_cloudy);
                cityWeather.setText(getResources().getString(R.string.weather_cloudy));
                break;
            case RAIN:
                imageView.setImageResource(R.drawable.ic_weather_rain);
                cityWeather.setText(getResources().getString(R.string.weather_rain));
                break;
            case SNOW:
                imageView.setImageResource(R.drawable.ic_weather_snow);
                cityWeather.setText(getResources().getString(R.string.weather_snow));
                break;
            case STORM:
                imageView.setImageResource(R.drawable.ic_weather_storm);
                cityWeather.setText(getResources().getString(R.string.weather_storm));
                break;
                default:
                    imageView.setImageResource(R.drawable.ic_weather_na);
                    break;
        }

        return view;
    }
}
