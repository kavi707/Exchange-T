package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

import java.util.Locale;

/**
 * Created by kavi707 on 12/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class LanguageFragment extends Fragment {

    private View rootView;

    private RelativeLayout englishRelativeLayout;
    private RelativeLayout sinhalaRelativeLayout;
    private RelativeLayout tamilRelativeLayout;

    private ImageView englishTickIcon;
    private ImageView sinhalaTickIcon;
    private ImageView tamilTickIcon;

    private CommonUtils commonUtils = new CommonUtils();

    public LanguageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_language, container, false);
        setUpView(rootView);
        ((LandingActivity) getActivity()).hideFloatingActionButton();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        // Set Action bar title
        ((LandingActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.e_nav_languages));
    }

    public void setUpView(View upView) {

        englishRelativeLayout = (RelativeLayout) upView.findViewById(R.id.englishRelativeLayout);
        sinhalaRelativeLayout = (RelativeLayout) upView.findViewById(R.id.sinhalaRelativeLayout);
        tamilRelativeLayout = (RelativeLayout) upView.findViewById(R.id.tamilRelativeLayout);

        englishTickIcon = (ImageView) upView.findViewById(R.id.englishTickIcon);
        sinhalaTickIcon = (ImageView) upView.findViewById(R.id.sinhalaTickIcon);
        tamilTickIcon = (ImageView) upView.findViewById(R.id.tamilTickIcon);

        englishRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad = "en"; // English
                SharedPreferenceManager.setSelectedLocal(getActivity(), languageToLoad);
                commonUtils.setLocal(getActivity());
                setLocalIndicator(languageToLoad);
                activityRelaunch();
            }
        });

        sinhalaRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad = "si"; // Sinhala
                SharedPreferenceManager.setSelectedLocal(getActivity(), languageToLoad);
                commonUtils.setLocal(getActivity());
                setLocalIndicator(languageToLoad);
                activityRelaunch();
            }
        });

        tamilRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad = "ta"; // Tamil
                SharedPreferenceManager.setSelectedLocal(getActivity(), languageToLoad);
                commonUtils.setLocal(getActivity());
                setLocalIndicator(languageToLoad);
                activityRelaunch();
            }
        });
    }

    private void setLocalIndicator(String languageToLoad) {
        if (languageToLoad.equals("en")) {
            englishTickIcon.setVisibility(View.VISIBLE);
            sinhalaTickIcon.setVisibility(View.INVISIBLE);
            tamilTickIcon.setVisibility(View.INVISIBLE);
        } else if (languageToLoad.equals("si")) {
            englishTickIcon.setVisibility(View.INVISIBLE);
            sinhalaTickIcon.setVisibility(View.VISIBLE);
            tamilTickIcon.setVisibility(View.INVISIBLE);
        } else if (languageToLoad.equals("ta")) {
            englishTickIcon.setVisibility(View.INVISIBLE);
            sinhalaTickIcon.setVisibility(View.INVISIBLE);
            tamilTickIcon.setVisibility(View.VISIBLE);
        } else {
            englishTickIcon.setVisibility(View.VISIBLE);
            sinhalaTickIcon.setVisibility(View.INVISIBLE);
            tamilTickIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void init() {
        String selectedLocal = SharedPreferenceManager.getSelectedLocal(getActivity());
        if (!selectedLocal.equals("NULL")) {
            setLocalIndicator(selectedLocal);
        } else {
            SharedPreferenceManager.setSelectedLocal(getActivity(), "en");
            setLocalIndicator(selectedLocal);
        }
    }

    private void activityRelaunch() {
        Intent refresh = new Intent(getActivity(), LandingActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }
}
