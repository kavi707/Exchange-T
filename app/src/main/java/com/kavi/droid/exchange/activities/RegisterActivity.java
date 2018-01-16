package com.kavi.droid.exchange.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.connections.ApiCallResponseHandler;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RegisterActivity extends ExchangeBaseActivity {

    private Button saveDataBtn;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText numberEditText;

    private Context context = this;
    private User user;
    private ProgressDialog progress;

    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpViews();
    }

    private void setUpViews() {

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        saveDataBtn = (Button) findViewById(R.id.saveDataBtn);

        user = commonUtils.populateDataFromFB(SharedPreferenceManager.getFBUserData(context));

        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonUtils.isOnline(context)) {
                    if (progress == null) {
                        progress = LoadingProgressBarDialog.createProgressDialog(context);
                    }
                    progress.show();

                    user.setPhoneNumber(numberEditText.getText().toString());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            new ApiCalls().addNewUser(context, Constants.SYNC_METHOD, user, new ApiCallResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    SharedPreferenceManager.setIsUserDataCaptured(context, true);
                                    SharedPreferenceManager.setLoggedUserName(context, firstNameEditText.getText().toString()
                                            + " " + lastNameEditText.getText().toString());
                                    SharedPreferenceManager.setLoggedUserEmail(context, emailEditText.getText().toString());
                                    SharedPreferenceManager.setLoggedUserImageUrl(context, user.getProfilePicUrl());
                                    SharedPreferenceManager.setLoggedUserNumber(context, numberEditText.getText().toString());
                                    SharedPreferenceManager.setFBUserId(context, user.getFbUserId());
                                    SharedPreferenceManager.setUserId(context, commonUtils.getUserIdFromResponse(response));

                                    generateAuthToken(user.getEmail(), user.getFbUserId());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();

                                            List<Integer> flags = new ArrayList<>();
                                            flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            new NavigationUtil(RegisterActivity.this)
                                                    .to(LandingActivity.class)
                                                    .setFlags(flags)
                                                    .finish()
                                                    .go();
                                        }
                                    });
                                }

                                @Override
                                public void onNonSuccess(int statusCode, JSONObject response, Throwable throwable) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, getResources().getString(R.string.e_toast_user_registration_error), Toast.LENGTH_LONG).show();
                                            progress.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.e_toast_device_internet_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generateAuthToken(String username, String password) {

        new ApiCalls().generateAuthToken(Constants.SYNC_METHOD, username, password, new ApiCallResponseHandler(){

            @Override
            public void onSuccess(int statusCode, JSONObject response) {

                if (statusCode == 200) {
                    try {
                        JSONObject resData;
                        if (response.get("res") instanceof JSONObject) {
                            resData = response.getJSONObject("res").getJSONObject("data");
                        } else {
                            resData = response.getJSONArray("res").getJSONObject(0).getJSONObject("data");
                        }

                        String authToken = resData.getString("accessToken");
                        SharedPreferenceManager.setNodegridAuthToken(context, authToken);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
