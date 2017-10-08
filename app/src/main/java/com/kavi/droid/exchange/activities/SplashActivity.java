package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SplashActivity extends ExchangeBaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpViews();
    }

    private void setUpViews() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity
                Intent startIntent;
                // Check User is logged in or not
                if (SharedPreferenceManager.isUserLogIn(context)) {
                    startIntent = new Intent(SplashActivity.this, LandingActivity.class);
                } else {
                    startIntent = new Intent(SplashActivity.this, SignInActivity.class);
                }

                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startIntent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
