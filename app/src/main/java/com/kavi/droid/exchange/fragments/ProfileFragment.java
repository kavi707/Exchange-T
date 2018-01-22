package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.services.connections.ApiCallResponseHandler;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.connections.dto.UpdateUserAdditionDataReq;
import com.kavi.droid.exchange.services.connections.dto.UpdateUserReq;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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
    private ProgressDialog progress;

    private ImageLoadingManager imageLoadingManager;
    private CommonUtils commonUtils = new CommonUtils();

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

    @Override
    public void onResume() {
        super.onResume();
        // Set Action bar title
        ((LandingActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.e_nav_my_profile));
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
                updateProfileData();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataFromPreferences();
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

        switch (name.length) {
            case 1:
                firstNameEditText.setText(name[0]);
                break;
            case 2:
                firstNameEditText.setText(name[0]);
                lastNameEditText.setText(name[1]);
                break;
        }
        emailEditText.setText(SharedPreferenceManager.getLoggedUserEmail(getActivity()));
        numberEditText.setText(SharedPreferenceManager.getLoggedUserNumber(getActivity()));

        imageLoadingManager.loadRoundCornerImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(getActivity()),
                userImageView);
    }

    private boolean isDirty() {

        boolean isDirty = false;
        String name[] = SharedPreferenceManager.getLoggedUserName(getActivity()).split(" ");

        switch (name.length) {
            case 0:
            case 1:
                isDirty = true;
                break;
            case 2:
                if (!name[0].endsWith(firstNameEditText.getText().toString())) isDirty = true;
                if (!name[1].endsWith(lastNameEditText.getText().toString())) isDirty = true;
                break;
        }

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

    private void updateProfileData() {

        final String newName = firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString();
        final String newEmail = emailEditText.getText().toString();
        final String newPhoneNumber = numberEditText.getText().toString();

        UpdateUserAdditionDataReq updateUserAdditionDataReq = new UpdateUserAdditionDataReq(
                newPhoneNumber,
                SharedPreferenceManager.getLoggedUserImageUrl(getActivity()),
                SharedPreferenceManager.getFBUserId(getActivity())
        );

        final UpdateUserReq updateUserReq = new UpdateUserReq(
                newEmail,
                newName,
                updateUserAdditionDataReq
        );

        if (commonUtils.isOnline(getActivity())) {

            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(getActivity());
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ApiCalls().updateUser(getActivity(), Constants.SYNC_METHOD,
                            SharedPreferenceManager.getUserId(getActivity()), updateUserReq,
                            new ApiCallResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {

                                    SharedPreferenceManager.setLoggedUserName(getActivity(), newName);
                                    SharedPreferenceManager.setLoggedUserEmail(getActivity(), newEmail);
                                    SharedPreferenceManager.setLoggedUserNumber(getActivity(), newPhoneNumber);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                            enableDisableSaveBtn(false);
                                            Toast.makeText(getActivity(), getResources().getString(R.string.e_toast_user_update_success), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onNonSuccess(int statusCode, JSONObject response, final Throwable throwable) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                            enableDisableSaveBtn(true);
                                            Toast.makeText(getActivity(), getResources().getString(R.string.e_toast_user_update_error), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                }
            }).start();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.e_toast_device_internet_error), Toast.LENGTH_SHORT).show();
        }
    }
}
