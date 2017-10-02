package com.kavi.droid.exchange.services.connections;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kavi707 on 10/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ApiCalls {

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SyncHttpClient syncHttpClient = new SyncHttpClient();

    public void addNewUser(String taskMethod, User user, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.ADD_USER;

        RequestParams requestParams = new RequestParams();
        JSONObject requestAdditionalParams = new JSONObject();

        try {
            requestParams.put("name", user.getFirstName() + " " + user.getLastName());
            requestParams.put("username", user.getEmail());
            requestParams.put("password", user.getFbUserId());

            requestAdditionalParams.put("phoneNumber", user.getPhoneNumber());
            requestAdditionalParams.put("fbUserId", user.getFbUserId());
            requestAdditionalParams.put("profilePicUrl", user.getProfilePicUrl());

            requestParams.put("additional", requestAdditionalParams);

            if (taskMethod.equals(Constants.SYNC_METHOD))
                syncHttpClient.post(url, requestParams, responseHandler);
            else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.post(url, requestParams, responseHandler);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getUserFromFBId(String taskMethod, String fbUserId, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.GET_FB_USER + fbUserId;

        if (taskMethod.equals(Constants.SYNC_METHOD))
            syncHttpClient.get(url, null, responseHandler);
        else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void generateAuthToken(String taskMethod, String username, String password) {

        String url = Constants.BASE_URL + Constants.GENERATE_AUTH_TOKEN;
    }
}
