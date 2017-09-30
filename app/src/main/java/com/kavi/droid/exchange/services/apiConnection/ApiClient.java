package com.kavi.droid.exchange.services.apiConnection;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kavi707 on 6/11/16.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ApiClient {

    public ApiClientResponse addNewUser(String taskMethod, User user) {

        ApiClientResponse apiClientResponse = null;
        JSONObject reqObj = new JSONObject();
        JSONObject additionalData = new JSONObject();
        IApiConnector apiConnector;
        try {
            reqObj.put("name", user.getFirstName() + " " + user.getLastName());
            reqObj.put("username", user.getEmail());
            reqObj.put("password", user.getFbUserId());

            additionalData.put("phoneNumber", user.getPhoneNumber());
            additionalData.put("fbUserId", user.getFbUserId());
            additionalData.put("profilePicUrl", user.getProfilePicUrl());

            reqObj.put("additional", additionalData);

            if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                apiConnector = new AsyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL + Constants.ADD_USER,
                        Constants.HTTP_POST, null, reqObj);
            } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
                apiConnector = new SyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL + Constants.ADD_USER,
                        Constants.HTTP_POST, null, reqObj);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return apiClientResponse;
    }

    public ApiClientResponse getUserFromFBId(String taskMethod, String fbUserId) {

        ApiClientResponse apiClientResponse = null;
        IApiConnector apiConnector;

        if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            apiConnector = new AsyncApiConnector();
            apiClientResponse = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_FB_USER
                    + fbUserId, Constants.HTTP_GET, null);
        } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
            apiConnector = new SyncApiConnector();
            apiClientResponse = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_FB_USER
                    + fbUserId, Constants.HTTP_GET, null);
        }

        return apiClientResponse;
    }

    public ApiClientResponse generateAuthToken(String taskMethod, String username, String password) {

        ApiClientResponse apiClientResponse = null;
        JSONObject reqObj = new JSONObject();
        IApiConnector apiConnector;
        try {
            reqObj.put("username", username);
            reqObj.put("password", password);

            if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                apiConnector = new AsyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL
                        + Constants.GENERATE_AUTH_TOKEN, Constants.HTTP_POST, null, reqObj);
            } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
                apiConnector = new SyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL
                        + Constants.GENERATE_AUTH_TOKEN, Constants.HTTP_POST, null, reqObj);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return apiClientResponse;
    }
}
