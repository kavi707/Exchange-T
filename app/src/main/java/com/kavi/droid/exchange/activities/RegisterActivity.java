package com.kavi.droid.exchange.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.apiConnection.ApiClient;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

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
    private ApiClient apiClient = new ApiClient();

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

        user = populateDataFromFB();

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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String response = apiClient.addNewUser(Constants.SYNC_METHOD, user);
                            if (response != null) {

                                SharedPreferenceManager.setIsUserDataCaptured(context, true);
                                SharedPreferenceManager.setLoggedUserName(context, firstNameEditText.getText().toString());
                                SharedPreferenceManager.setLoggedUserEmail(context, emailEditText.getText().toString());
                                SharedPreferenceManager.setLoggedUserImageUrl(context, user.getProfilePicUrl());
                                SharedPreferenceManager.setLoggedUserNumber(context, numberEditText.getText().toString());

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

    private User populateDataFromFB() {

        User appUser = null;
        String jsonString = SharedPreferenceManager.getFBUserData(context);

        if (jsonString != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonString);

                appUser = new User();

                // User Id
                String id = jsonData.getString("id");
                appUser.setUserId(id);

                // User first name
                if (!jsonData.isNull("first_name")) {
                    String firstName = jsonData.getString("first_name");
                    appUser.setFirstName(firstName);
                } else {
                    appUser.setFirstName("");
                }

                // User last name
                if (!jsonData.isNull("last_name")) {
                    String lastName = jsonData.getString("last_name");
                    appUser.setLastName(lastName);
                } else {
                    appUser.setLastName("");
                }

                // Email
                if (!jsonData.isNull("email")) {
                    String email = jsonData.getString("email");
                    appUser.setEmail(email);
                } else {
                    appUser.setEmail("");
                }

                // Profile pic
                URL profilePic = null;
                try {
                    profilePic = new URL("https://graph.facebook.com/" + id + "/picture?width=" +
                            FBManager.PROFILE_PIC_WIDTH + "&height=" + FBManager.PROFILE_PIC_HEIGHT);
                    appUser.setProfilePicUrl(profilePic.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return appUser;
    }
}
