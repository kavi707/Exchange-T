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
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.apiConnection.ApiClient;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RegisterActivity extends AppCompatActivity {

    private Button saveDataBtn;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText numberEditText;

    private Context context = this;
    private User user;
    private ProgressDialog progress;

    private CommonUtils commonUtils = new CommonUtils();
    private ApiClient apiClient = new ApiClient(context);

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

                            ApiClientResponse response = apiClient.addNewUser(Constants.SYNC_METHOD, user);
                            if (response != null) {

                                SharedPreferenceManager.setIsUserDataCaptured(context, true);
                                SharedPreferenceManager.setLoggedUserName(context, firstNameEditText.getText().toString());
                                SharedPreferenceManager.setLoggedUserEmail(context, emailEditText.getText().toString());
                                SharedPreferenceManager.setLoggedUserImageUrl(context, user.getProfilePicUrl());
                                SharedPreferenceManager.setLoggedUserNumber(context, numberEditText.getText().toString());

                                generateAuthToken(user.getEmail(), user.getFbUserId());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        Intent landingIntent = new Intent(RegisterActivity.this, LandingActivity.class);
                                        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(landingIntent);

                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Issue in user registration. Please try again from while", Toast.LENGTH_LONG).show();
                                        progress.dismiss();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(context, "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean generateAuthToken(String username, String password) {

        boolean isTokenGenerated = false;
        ApiClientResponse apiClientResponse = apiClient.generateAuthToken(Constants.SYNC_METHOD, username, password);
        if (apiClientResponse != null) {

            int statusCode = apiClientResponse.getHttpStatusCode();
            String jsonString = apiClientResponse.getResponseObj();

            if (statusCode == 200) {
                try {
                    JSONObject jsonData = new JSONObject(jsonString);
                    JSONObject resData = jsonData.getJSONObject("res").getJSONObject("data");

                    String authToken = resData.getString("accessToken");
                    SharedPreferenceManager.setNodegridAuthToken(context, authToken);

                    isTokenGenerated = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return isTokenGenerated;
    }
}
