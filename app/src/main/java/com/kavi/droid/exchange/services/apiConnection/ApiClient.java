package com.kavi.droid.exchange.services.apiConnection;

import android.content.Context;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kavi707 on 6/11/16.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ApiClient {

    private Context context;

    public ApiClient(Context context) {
        this.context = context;
    }

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

    public ApiClientResponse createTicketRequest(String taskMethod, TicketRequest ticketRequest) {

        ApiClientResponse apiClientResponse = null;
        JSONObject reqObj = new JSONObject();
        Map<String, String> headers = new HashMap<>();
        IApiConnector apiConnector;

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        try {
            reqObj.put("fbUserId", SharedPreferenceManager.getFBUserId(context));
            reqObj.put("name", ticketRequest.getName());
            reqObj.put("profilePicUrl", ticketRequest.getUserPicUrl());
            reqObj.put("phoneNo", ticketRequest.getPhoneNo());
            reqObj.put("email", ticketRequest.getEmail());
            reqObj.put("type", ticketRequest.getReqType());
            reqObj.put("startToEnd", ticketRequest.getStartToEnd());
            reqObj.put("qty", ticketRequest.getQty());
            reqObj.put("ticketDate", ticketRequest.getTicketDate());
            reqObj.put("ticketTime", ticketRequest.getTicketTime());
            reqObj.put("ticketDay", ticketRequest.getTicketDay());
            reqObj.put("reqNote", ticketRequest.getReqDescription());

            headers.put("Authorization", authToken);

            if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                apiConnector = new AsyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL
                        + Constants.ADD_TICKET_REQUEST, Constants.HTTP_POST, headers, reqObj);
            } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
                apiConnector = new SyncApiConnector();
                apiClientResponse = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL
                        + Constants.ADD_TICKET_REQUEST, Constants.HTTP_POST, headers, reqObj);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return apiClientResponse;
    }

    public ApiClientResponse getTicketRequest(String taskMethod) {

        ApiClientResponse apiClientResponse = null;
        IApiConnector apiConnector;

        Map<String, String> headers = new HashMap<>();
        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
        headers.put("Authorization", authToken);

        if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            apiConnector = new AsyncApiConnector();
            apiClientResponse = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_TICKET_REQUEST,
                    Constants.HTTP_GET, headers);
        } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
            apiConnector = new SyncApiConnector();
            apiClientResponse = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_TICKET_REQUEST,
                    Constants.HTTP_GET, headers);
        }

        return apiClientResponse;
    }
}
