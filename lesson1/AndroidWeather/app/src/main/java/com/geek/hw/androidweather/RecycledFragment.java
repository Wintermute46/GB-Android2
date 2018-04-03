package com.geek.hw.androidweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

interface StartDetailView{
    void startDetailView(CityModel city);
}

public class RecycledFragment extends Fragment implements StartDetailView, View.OnClickListener {
    private RecyclerView recyclerView;
    private FloatingActionButton floatButton;
    private CustomAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycled, container, false);

        if(savedInstanceState == null)
            adapter = new CustomAdapter(cityModels(), this);

            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            floatButton = view.findViewById(R.id.float_add_button);
            floatButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                floatButton.callOnClick();
                return true;
            case R.id.menu_item_clear:
                adapter.clear();
                return true;
                default: return super.onOptionsItemSelected(item);
        }
    }


    private List<CityModel> cityModels() {
        List<CityModel> result = new ArrayList<>();
        String[] cities = getResources().getStringArray(R.array.cities);
        for (int i = 0; i < cities.length; i++)
            result.add(new CityModel(cities[i]));
        return result;
    }

    @Override
    public void startDetailView(CityModel city) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CityModel.NAME, city.getCityName());
        bundle.putInt(CityModel.TEMPERATURE, city.getTemperature());
        bundle.putString(CityModel.WEATHER, city.getWeather().toString());
        detailFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.float_add_button:
                String text = "NewCity" + recyclerView.getAdapter().getItemCount();
                adapter.addCity(text);
        }
    }
}
