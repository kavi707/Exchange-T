package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

import org.json.JSONObject;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SignInActivity extends AppCompatActivity {

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    private Context context = this;

    private FBManager fbManager = new FBManager();

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

                        Intent landingIntent;
                        if (SharedPreferenceManager.isUserDataCaptured(context)) {
                            landingIntent = new Intent(SignInActivity.this, LandingActivity.class);
                        } else {
                            landingIntent = new Intent(SignInActivity.this, RegisterActivity.class);
                        }

                        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(landingIntent);

                        finish();
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
}
