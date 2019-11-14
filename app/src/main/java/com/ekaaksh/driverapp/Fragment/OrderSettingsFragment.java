package com.ekaaksh.driverapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.ekaaksh.driverapp.R;

public class OrderSettingsFragment extends Fragment {

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.order_settings_layout, container, false);



        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Order Settings");

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.action_notification) != null)
            menu.findItem(R.id.action_notification).setVisible(true);

        if (menu.findItem(R.id.action_help) != null)
            menu.findItem(R.id.action_help).setVisible(true);
    }

 }
