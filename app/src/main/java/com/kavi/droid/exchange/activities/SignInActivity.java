package com.kavi.droid.exchange.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.apiConnection.ApiClient;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SignInActivity extends AppCompatActivity {

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    private Context context = this;
    private ProgressDialog progress;

    private FBManager fbManager = new FBManager();
    private CommonUtils commonUtils = new CommonUtils();
    private ApiClient apiClient = new ApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign_in);

        setUpViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpViews() {

        fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        fbLoginButton.setReadPermissions(fbManager.getFbReadPermissions());
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                SharedPreferenceManager.setIsLogIn(context, true);
                SharedPreferenceManager.setFBUserToken(context, loginResult.getAccessToken().getToken());

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        SharedPreferenceManager.setFBUserData(context, object.toString());

                        checkUserAvailability();
                    }
                });
                graphRequest.setParameters(fbManager.getProfileParameters());
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("TAG", String.valueOf("On Cancel"));
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", String.valueOf("On Error"));
                error.printStackTrace();
            }
        });
    }

    private void checkUserAvailability() {

        final User appUser = commonUtils.populateDataFromFB(SharedPreferenceManager.getFBUserData(context));

        if (commonUtils.isOnline(context)) {
            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(context);
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final ApiClientResponse response = apiClient.getUserFromFBId(Constants.SYNC_METHOD, appUser.getFbUserId());
                    if (response != null) {

                        if (response.getHttpStatusCode() == 200) {
                            String jsonString = response.getResponseObj();
                            persistUser(jsonString);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();

                                Intent landingIntent;
                                if (response.getHttpStatusCode() == 200) {
                                    landingIntent = new Intent(SignInActivity.this, LandingActivity.class);
                                } else {
                                    landingIntent = new Intent(SignInActivity.this, RegisterActivity.class);
                                }

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

    private boolean persistUser(String jsonString) {

        boolean isTokenPersist = false;

        try {
            JSONObject jsonData = new JSONObject(jsonString);

            JSONArray resDataArr = jsonData.getJSONArray("data");

            JSONObject jsonUserObj = resDataArr.getJSONObject(0).getJSONObject("data");

            SharedPreferenceManager.setIsUserDataCaptured(context, true);
            SharedPreferenceManager.setLoggedUserName(context, jsonUserObj.getString("name"));
            SharedPreferenceManager.setLoggedUserEmail(context, jsonUserObj.getString("username"));
            SharedPreferenceManager.setLoggedUserImageUrl(context, jsonUserObj.getJSONObject("additionalData")
                    .getString("profilePicUrl"));
            SharedPreferenceManager.setLoggedUserNumber(context, jsonUserObj.getJSONObject("additionalData")
                    .getString("phoneNumber"));

            isTokenPersist = generateAuthToken(jsonUserObj.getString("username"), jsonUserObj.getJSONObject("additionalData")
                    .getString("fbUserId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isTokenPersist;
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
