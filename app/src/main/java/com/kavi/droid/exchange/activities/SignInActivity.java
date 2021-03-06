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
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.connections.ApiCallResponseHandler;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SignInActivity extends ExchangeBaseActivity {

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    private Context context = this;
    private ProgressDialog progress;

    private FBManager fbManager = new FBManager();
    private CommonUtils commonUtils = new CommonUtils();

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

                    new ApiCalls().getUserFromFBId(Constants.SYNC_METHOD, appUser.getFbUserId(), new ApiCallResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {

                            if (statusCode == 200) {
                                persistUser(response);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    // Submit push token to service
                                    submitFCMPushToken();

                                    List<Integer> flags = new ArrayList<>();
                                    flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    new NavigationUtil(SignInActivity.this)
                                            .to(LandingActivity.class)
                                            .setFlags(flags)
                                            .finish()
                                            .go();
                                }
                            });
                        }

                        @Override
                        public void onNonSuccess(int statusCode, JSONObject response, Throwable throwable) {

                            final int getStatusCode = statusCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progress.dismiss();
                                    if (getStatusCode == 404) {
                                        List<Integer> flags = new ArrayList<>();
                                        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        new NavigationUtil(SignInActivity.this)
                                                .to(RegisterActivity.class)
                                                .setFlags(flags)
                                                .finish()
                                                .go();

                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.e_toast_sign_in_error), Toast.LENGTH_LONG).show();
                                    }
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

    private void persistUser(JSONObject jsonData) {

        try {
            JSONArray resDataArr = jsonData.getJSONArray("res");

            JSONObject jsonUserObj = resDataArr.getJSONObject(0).getJSONObject("data");

            SharedPreferenceManager.setIsUserDataCaptured(context, true);
            SharedPreferenceManager.setLoggedUserName(context, jsonUserObj.getString("name"));
            SharedPreferenceManager.setLoggedUserEmail(context, jsonUserObj.getString("username"));
            SharedPreferenceManager.setLoggedUserImageUrl(context, jsonUserObj.getJSONObject("additionalData")
                    .getString("profilePicUrl"));
            SharedPreferenceManager.setLoggedUserNumber(context, jsonUserObj.getJSONObject("additionalData")
                    .getString("phoneNumber"));
            SharedPreferenceManager.setFBUserId(context, jsonUserObj.getJSONObject("additionalData")
                    .getString("fbUserId"));
            SharedPreferenceManager.setUserId(context, commonUtils.getUserIdFromArrayResponse(jsonData));

            generateAuthToken(jsonUserObj.getString("username"), jsonUserObj.getJSONObject("additionalData")
                    .getString("fbUserId"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

            @Override
            public void onNonSuccess(int statusCode, JSONObject response, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getResources().getString(R.string.e_toast_sign_in_error), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    private void submitFCMPushToken() {

        String fbUserId = SharedPreferenceManager.getFBUserId(context);

        new ApiCalls().submitPushToken(context, Constants.ASYNC_METHOD, fbUserId,
                SharedPreferenceManager.getFCMPushToken(context),
                new ApiCallResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        // Push Token submitted successfully.
                    }

                    @Override
                    public void onNonSuccess(int statusCode, JSONObject response, final Throwable throwable) {
                        super.onNonSuccess(statusCode, response, throwable);
                    }
                });
    }
}
