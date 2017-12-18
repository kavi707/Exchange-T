package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;

/**
 * Created by kavi707 on 12/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class SettingsFragment extends Fragment {

    private View rootView;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setUpView(rootView);
        ((LandingActivity) getActivity()).hideFloatingActionButton();

        return rootView;
    }

    public void setUpView(View upView) {
        // TODO - Notification View content
    }
}
