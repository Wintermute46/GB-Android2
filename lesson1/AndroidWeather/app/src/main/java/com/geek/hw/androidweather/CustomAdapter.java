package com.geek.hw.androidweather;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

interface OnCustomAdapterListener{
    void detailCity(int position);
    void removeCity(int position);
}

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MHolder> implements OnCustomAdapterListener{

    private List<CityModel> mCities;
    private StartDetailView startDetailView;

    public CustomAdapter(List<CityModel> cities, StartDetailView startDetailView) {
        mCities = cities;
        this.startDetailView = startDetailView;
    }

    @Override
    public MHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MHolder(item);
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {
        holder.bind(mCities.get(position).getCityName(), this );
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public List<CityModel> getList() {
        return mCities;
    }

    @Override
    public void detailCity(int position) {
        CityModel city = mCities.get(position);
        startDetailView.startDetailView(city);
    }

    @Override
    public void removeCity(int position) {
        mCities.remove(position);
        notifyDataSetChanged();
    }

    public void addCity(String name) {
        mCities.add(new CityModel(name));
        notifyDataSetChanged();
    }

    public void clear() {
        mCities.clear();
        notifyDataSetChanged();
    }

    static class MHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        private TextView cityName, contextView;
        private OnCustomAdapterListener callbacks;

        public MHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            contextView = itemView.findViewById(R.id.txtOption);
            contextView.setOnClickListener(this);
        }

        void bind(String name, OnCustomAdapterListener callbacks) {
            this.callbacks = callbacks;
            cityName.setText(name);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_detail:
                    if (callbacks != null) callbacks.detailCity(getAdapterPosition());
                    return true;
                case R.id.menu_delete:
                    if (callbacks != null) callbacks.removeCity(getAdapterPosition());
                    return true;
                default: return false;
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txtOption : {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();
                    break;
                }
            }

        }
    }

}
