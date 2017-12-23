package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

/**
 * Created by kaviDroid on 12/23/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ProfileFragment extends Fragment {

    private View rootView;

    private AdView myProfileAdView;
    private Button saveDataBtn;
    private Button cancelBtn;
    private ImageView userImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText numberEditText;

    private ImageLoadingManager imageLoadingManager;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpView(rootView);
        ((LandingActivity) getActivity()).hideFloatingActionButton();

        return rootView;
    }

    public void setUpView(View upView) {

        imageLoadingManager = new ImageLoadingManager(getActivity());

        userImageView = (ImageView) upView.findViewById(R.id.userImageView);
        firstNameEditText = (EditText) upView.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) upView.findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) upView.findViewById(R.id.emailEditText);
        numberEditText = (EditText) upView.findViewById(R.id.numberEditText);
        saveDataBtn = (Button) upView.findViewById(R.id.saveDataBtn);
        cancelBtn = (Button) upView.findViewById(R.id.cancelBtn);

        saveDataBtn.setEnabled(false);
        enableDisableSaveBtn(false);

        // Google Ads
        myProfileAdView = (AdView) upView.findViewById(R.id.myProfileAdView);
        myProfileAdView.loadAd(new AdRequest.Builder().build());

        loadDataFromPreferences();
        initEditTextFocusListeners();

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initEditTextFocusListeners() {
        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) isDirty();
            }
        });

        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) isDirty();
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) isDirty();
            }
        });

        numberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) isDirty();
            }
        });
    }

    private void loadDataFromPreferences() {

        String[] name = SharedPreferenceManager.getLoggedUserName(getActivity()).split(" ");

        firstNameEditText.setText(name[0]);
        lastNameEditText.setText(name[1]);
        emailEditText.setText(SharedPreferenceManager.getLoggedUserEmail(getActivity()));
        numberEditText.setText(SharedPreferenceManager.getLoggedUserNumber(getActivity()));

        imageLoadingManager.loadImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(getActivity()),
                userImageView, true);
    }

    private boolean isDirty() {

        boolean isDirty = false;
        String name[] = SharedPreferenceManager.getLoggedUserName(getActivity()).split(" ");

        if (!name[0].endsWith(firstNameEditText.getText().toString())) isDirty = true;

        if (!name[1].endsWith(lastNameEditText.getText().toString())) isDirty = true;

        if (!SharedPreferenceManager.getLoggedUserEmail(getActivity())
                .endsWith(emailEditText.getText().toString())) isDirty = true;

        if (!SharedPreferenceManager.getLoggedUserNumber(getActivity())
                .endsWith(numberEditText.getText().toString())) isDirty = true;

        if (isDirty) {
            saveDataBtn.setEnabled(true);
            enableDisableSaveBtn(true);
        } else {
            saveDataBtn.setEnabled(false);
            enableDisableSaveBtn(false);
        }

        return isDirty;
    }

    private void enableDisableSaveBtn(boolean isEnable) {

        if (isEnable) {
            saveDataBtn.setBackground(getResources().getDrawable(R.drawable.style_border_square_b));
            saveDataBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            saveDataBtn.setBackground(getResources().getDrawable(R.drawable.style_border_square_g));
            saveDataBtn.setTextColor(getResources().getColor(R.color.grey));
        }
    }

    private void saveProfileNewData() {

        String newName = firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newPhoneNumber = numberEditText.getText().toString();

        SharedPreferenceManager.setLoggedUserName(getActivity(), newName);
        SharedPreferenceManager.setLoggedUserEmail(getActivity(), newEmail);
        SharedPreferenceManager.setLoggedUserNumber(getActivity(), newPhoneNumber);
    }
}
