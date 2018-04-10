package com.geek.hw.meteo.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.text.InputType;
import android.widget.Spinner;

import com.geek.hw.meteo.R;

public class SelectCityDialog extends DialogFragment {

    private final static String SETTINGS_STORAGE = "MeteoCitySelector";
    private final static String SETTINGS_STORAGE_SPINNER_ID = "SpinnerId";
    private SharedPreferences appSettings;
    private int spinnerId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.change_city_dialog));
        Resources r = getResources();

        appSettings = getActivity().getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE);

        if (appSettings.contains(SETTINGS_STORAGE_SPINNER_ID))
            spinnerId = appSettings.getInt(SETTINGS_STORAGE_SPINNER_ID, 0);

        final Spinner spinner = new Spinner(getActivity());

        String[] items = getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> spinItems = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                                                                    items);
        spinner.setAdapter(spinItems);
        spinner.setSelection(spinnerId);

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        spinner.setPadding(px, px, px, px);
        builder.setView(spinner);
        builder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                spinnerId = spinner.getSelectedItemPosition();
                ((AddCityDialogListener)getActivity()).onSelectCity(spinner.getSelectedItem().toString());
            }
        });
        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putInt(SETTINGS_STORAGE_SPINNER_ID, spinnerId);
        editor.apply();
    }
}