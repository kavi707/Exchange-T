package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavi.droid.exchange.R;

/**
 * Created by kavi707 on 12/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class NotificationFragment extends Fragment {

    private View rootView;

    public NotificationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        setUpView(rootView);

        return rootView;
    }

    public void setUpView(View upView) {
        // TODO - Notification View content
    }
}
