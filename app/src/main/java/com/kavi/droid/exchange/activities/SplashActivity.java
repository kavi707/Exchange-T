package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kavi707 on 9/9/17.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SplashActivity extends ExchangeBaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private Context context = this;

    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Google Ads
        MobileAds.initialize(this, getResources().getString(R.string.ad_mob_app_id));

        setUpViews();
    }

    private void setUpViews() {

        // Check User is logged in or not
        if (SharedPreferenceManager.isUserLogIn(context)) {
            if (commonUtils.isOnline(context)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ApiCalls().checkCurrentTokenStatus(context, Constants.SYNC_METHOD,
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        if (statusCode == 200) {
                                            List<Integer> flags = new ArrayList<>();
                                            flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            new NavigationUtil(SplashActivity.this)
                                                    .to(LandingActivity.class)
                                                    .setTransitionAnim(NavigationUtil.ANIM_FADE_IN)
                                                    .setFlags(flags)
                                                    .finish()
                                                    .go();
                                        } else {
                                            // Logout from application - clear all persist data
                                            commonUtils.logoutApplication(context, true);

                                            List<Integer> flags = new ArrayList<>();
                                            flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            new NavigationUtil(SplashActivity.this)
                                                    .to(SignInActivity.class)
                                                    .setTransitionAnim(NavigationUtil.ANIM_FADE_IN)
                                                    .setFlags(flags)
                                                    .finish()
                                                    .go();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        // Logout from application - clear all persist data
                                        commonUtils.logoutApplication(context, true);

                                        List<Integer> flags = new ArrayList<>();
                                        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        new NavigationUtil(SplashActivity.this)
                                                .to(SignInActivity.class)
                                                .setTransitionAnim(NavigationUtil.ANIM_FADE_IN)
                                                .setFlags(flags)
                                                .finish()
                                                .go();
                                    }
                                });
                    }
                }).start();
            } else {
                Toast.makeText(context, "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
            }
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    List<Integer> flags = new ArrayList<>();
                    flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
                    flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    new NavigationUtil(SplashActivity.this)
                            .to(SignInActivity.class)
                            .setTransitionAnim(NavigationUtil.ANIM_FADE_IN)
                            .setFlags(flags)
                            .finish()
                            .go();

                }
            }, SPLASH_TIME_OUT);
        }
    }
}
