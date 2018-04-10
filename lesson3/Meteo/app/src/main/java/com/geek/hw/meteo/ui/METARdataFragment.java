package com.geek.hw.meteo.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.hw.meteo.MainActivity;
import com.geek.hw.meteo.MetarDataLoader;
import com.geek.hw.meteo.OwmDataLoader;
import com.geek.hw.meteo.R;
import com.geek.hw.meteo.models.CityData;
import com.geek.hw.meteo.models.MetarData;


public class METARdataFragment extends Fragment {

    private static float LAT;
    private static float LON;

    private final Handler handler = new Handler();
    private static final String LOG_TAG = METARdataFragment.class.getSimpleName();

    private TextView icao;
    private TextView name;
    private TextView metarRaw;
    private TextView observed;
    private TextView currTemp;
    private TextView dewpoint;
    private TextView humidity;
    private TextView flightRules;
    private TextView wind;
    private TextView visibility;
    private TextView clouds;
    private TextView altimeter;
    private TextView pressure;
    private TextView elevation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_metardata, container, false);

        LAT = MainActivity.LAT;
        LON = MainActivity.LON;

        icao = view.findViewById(R.id.icao);
        name = view.findViewById(R.id.name);
        metarRaw = view.findViewById(R.id.metar_raw);
        observed = view.findViewById(R.id.observed);
        currTemp = view.findViewById(R.id.text_current_temp);
        dewpoint = view.findViewById(R.id.text_dewpoint_temp);
        humidity = view.findViewById(R.id.text_humidity_p);
        flightRules = view.findViewById(R.id.text_fr_p);
        wind = view.findViewById(R.id.text_wind_p);
        visibility = view.findViewById(R.id.text_visibility_p);
        clouds = view.findViewById(R.id.text_clouds_p);
        altimeter = view.findViewById(R.id.text_alt_p);
        pressure = view.findViewById(R.id.text_press_p);
        elevation = view.findViewById(R.id.text_elev_p);

        if (LAT != 0 && LON != 0)
            updateMetarData(LAT, LON);
        else
            Log.i("METAR", "No METAR coordinates");

        return view;
    }


    private void updateMetarData(final float lon, final float lat){
        new Thread(){
            public void run() {
                final MetarData data = MetarDataLoader.getMetarData(getActivity(), lon, lat);

                if (data == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.metar_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderMetar(data);
                        }
                    });
                }
            }
        }.start();
    }


    private void renderMetar(MetarData metar) {
        try {
            icao.setText(metar.data.get(0).icao);
            String icaoName = metar.data.get(0).name + " Airport. " + getArguments().getString(MainActivity.CITY_NAME);
            name.setText(icaoName);
            metarRaw.setText(metar.data.get(0).rawText);

            String observ = metar.data.get(0).observed + " UTC";
            observed.setText(observ);

            String currT = metar.data.get(0).temperature.celsius + " ℃ / " + metar.data.get(0).temperature.fahrenheit + " F";
            currTemp.setText(currT);

            String dew = metar.data.get(0).dewpoint.celsius + " ℃ / " + metar.data.get(0).dewpoint.fahrenheit + " F";
            dewpoint.setText(dew);

            String humidit = metar.data.get(0).humidityPercent + "%";
            humidity.setText(humidit);

            String cloud = metar.data.get(0).clouds.get(0).text;
            clouds.setText(cloud);

            String winds = metar.data.get(0).wind.degrees + "° " + metar.data.get(0).wind.speedKts + " knots"
                            + " / " + knotsToMeters(metar.data.get(0).wind.speedKts) + " m/s";
            wind.setText(winds);

            String visible = metar.data.get(0).visibility.miles + " / " + metar.data.get(0).visibility.meters;
            visibility.setText(visible);

            flightRules.setText(metar.data.get(0).flightCategory);

            String alt = metar.data.get(0).barometer.hg + " Hg";
            altimeter.setText(alt);

            String pres = metar.data.get(0).barometer.mb + " hPa";
            pressure.setText(pres);

            String elev = metar.data.get(0).elevation.feet + " ft / " + metar.data.get(0).elevation.meters + "m  MSL";
            elevation.setText(elev);
        } catch (Exception e) {
            Log.d(LOG_TAG, getString(R.string.json_error));
            Toast.makeText(getActivity(), getString(R.string.json_error), Toast.LENGTH_LONG).show();
        }

    }

    private int knotsToMeters (int knots) {
        return (int)(knots * 0.514444);
    }


}
