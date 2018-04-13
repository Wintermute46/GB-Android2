package com.geek.hw.meteo.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.hw.meteo.MainActivity;
import com.geek.hw.meteo.OwmDataLoader;
import com.geek.hw.meteo.R;
import com.geek.hw.meteo.models.CityData;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

///////////////////////////////////////////////////////////////////////////
// Fragment with OpenStreetMap weather data
///////////////////////////////////////////////////////////////////////////

public class OWMdataFragment extends Fragment {

    private final Handler handler = new Handler();
    private static final String LOG_TAG = OWMdataFragment.class.getSimpleName();
    private final static String ICON_URL = "http://openweathermap.org/img/w/%s.png";
    private String city;

    private TextView cityTextView;
    private TextView updatedTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private ImageView weatherIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_owmdata, container, false);

        cityTextView = view.findViewById(R.id.city_field);
        updatedTextView = view.findViewById(R.id.updated_field);
        detailsTextView = view.findViewById(R.id.details_field);
        currentTemperatureTextView = view.findViewById(R.id.current_temperature_field);
        weatherIcon = view.findViewById(R.id.weather_icon);

        city = getArguments().getString(MainActivity.CITY_NAME);

        updateWeatherData(city);

        return view;
    }

///////////////////////////////////////////////////////////////////////////
// Get the weather
///////////////////////////////////////////////////////////////////////////

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final CityData data = OwmDataLoader.getOwmData(getActivity(), city);

                if (data == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(data);
                        }
                    });
                }

            }
        }.start();
    }

///////////////////////////////////////////////////////////////////////////
// Display the weather data
///////////////////////////////////////////////////////////////////////////

    private void renderWeather(CityData data) {
        try {
            String name = data.name.toUpperCase(Locale.US) + ", " + data.sys.country;
            cityTextView.setText(name);

            String description = "";
            long id = 0;

            if(data.weather.size() != 0){
                description = data.weather.get(0).description.toUpperCase(Locale.US);
                id = data.weather.get(0).id;
            }

            String details = description + "\n" + getString(R.string.text_humidity)
                    + data.main.humidity + "%" + "\n" + getString(R.string.text_press) + data.main.pressure + " hPa";
            detailsTextView.setText(details);

            String currT = String.format("%.2f", data.main.tempBig) + " ℃";
            currentTemperatureTextView.setText(currT);

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(data.dt * 1000));
            String upd = "Last update: " + updatedOn;
            updatedTextView.setText(upd);

            MainActivity.LAT = data.coord.lat;
            MainActivity.LON = data.coord.lon;

            setWeatherIcon(data.weather.get(0).icon);

        } catch (Exception e) {
            Log.d(LOG_TAG, getString(R.string.json_error));
            Toast.makeText(getActivity(), getString(R.string.json_error), Toast.LENGTH_LONG).show();
        }
    }

///////////////////////////////////////////////////////////////////////////
// Download the weather icon
///////////////////////////////////////////////////////////////////////////

    private void setWeatherIcon(String icon){

        final String iconURL = String.format(ICON_URL, icon);

        new Thread() {

        public void run() {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder builder = HttpUrl.parse(iconURL).newBuilder();
            final Request request = new Request.Builder().url(builder.build().toString()).build();

            try {
                final Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();

                final Bitmap img = BitmapFactory.decodeStream(inputStream);

                if(img != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            weatherIcon.setImageBitmap(img);
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.server_img_error), Toast.LENGTH_LONG).show();
            }
        }
        }.start();
    }

}
