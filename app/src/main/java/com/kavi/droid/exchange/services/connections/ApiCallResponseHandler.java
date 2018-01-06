package com.kavi.droid.exchange.services.connections;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.SignInActivity;
import com.kavi.droid.exchange.utils.NavigationUtil;

import org.json.JSONObject;

/**
 * Created by pl36586 on 1/5/18.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public abstract class ApiCallResponseHandler implements OnApiCallResponse {

    protected ApiCallResponseHandler() {
    }

    @Override
    public void onNoInternet(Throwable throwable) {
    }

    @Override
    public void onUnAuthorized(JSONObject response, Throwable throwable) {
    }

    @Override
    public void onServiceError(JSONObject response, Throwable throwable) {
    }

    @Override
    public void onNonSuccess(int statusCode, JSONObject response, Throwable throwable) {
    }
}
