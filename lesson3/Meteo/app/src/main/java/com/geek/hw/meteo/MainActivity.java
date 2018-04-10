package com.geek.hw.meteo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.geek.hw.meteo.ui.AddCityDialogListener;
import com.geek.hw.meteo.ui.METARdataFragment;
import com.geek.hw.meteo.ui.OWMdataFragment;
import com.geek.hw.meteo.ui.SelectCityDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddCityDialogListener {

    public static float LAT;
    public static float LON;

    private final static String SETTINGS_STORAGE = "MeteoCitySelection";
    public final static String CITY_NAME = "city";
    private static String selectedCity;
    private NavigationView navigationView;
    private SharedPreferences appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appSettings = getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String[] cities = getResources().getStringArray(R.array.cities);
        if (appSettings.contains(CITY_NAME))
            selectedCity = appSettings.getString(CITY_NAME, cities[(int)(Math.random() * cities.length)]);
        else
            selectedCity = cities[(int)(Math.random() * cities.length)];

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null)
            setScreen(R.id.nav_open_weather,selectedCity);

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(CITY_NAME, selectedCity);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_city:
                new SelectCityDialog().show(getSupportFragmentManager(), "branch_filter_mode_dialog");
                return true;
                default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_open_weather:
                setScreen(R.id.nav_open_weather, selectedCity);
                break;
            case R.id.nav_metar:
                setScreen(R.id.nav_metar, selectedCity);
                break;
        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSelectCity(String city) {
        selectedCity = city;
        setScreen(R.id.nav_open_weather, selectedCity);
    }

    private void setScreen(int itemId, String city) {
        Fragment fragment;

        switch (itemId) {
            case R.id.nav_open_weather:
                fragment = new OWMdataFragment();
                break;
            case R.id.nav_metar:
                fragment = new METARdataFragment();
                break;
                default: fragment = new OWMdataFragment();
                    break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, city);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        navigationView.setCheckedItem(itemId);
    }

}
