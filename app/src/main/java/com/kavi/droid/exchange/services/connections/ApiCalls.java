package com.kavi.droid.exchange.services.connections;

import android.content.Context;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by kavi707 on 10/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ApiCalls {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String APPLICATION_JSON = "application/json";

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SyncHttpClient syncHttpClient = new SyncHttpClient();

    public void addNewUser(Context context, String taskMethod, User user, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.ADD_USER;

        RequestParams requestParams = new RequestParams();
        JSONObject reqObj = new JSONObject();
        JSONObject reqObjAdditional = new JSONObject();

        try {

            reqObj.put("name", user.getFirstName() + " " + user.getLastName());
            reqObj.put("username", user.getEmail());
            reqObj.put("password", user.getFbUserId());

            requestParams.put("name", user.getFirstName() + " " + user.getLastName());
            requestParams.put("username", user.getEmail());
            requestParams.put("password", user.getFbUserId());

            reqObjAdditional.put("phoneNumber", user.getPhoneNumber());
            reqObjAdditional.put("fbUserId", user.getFbUserId());
            reqObjAdditional.put("profilePicUrl", user.getProfilePicUrl());

            requestParams.put("additional", reqObjAdditional);
            reqObj.put("additional", reqObjAdditional);

            String reqJsonString = reqObj.toString();

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                syncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

    public void generateAuthToken(String taskMethod, String username, String password, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.GENERATE_AUTH_TOKEN;
        RequestParams requestParams = new RequestParams();

        requestParams.put("username", username);
        requestParams.put("password", password);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.post(url, requestParams, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.post(url, requestParams, responseHandler);
        }
    }

    public void createTicketRequest(Context context, String taskMethod, TicketRequest ticketRequest,
                                    JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.ADD_TICKET_REQUEST;
        RequestParams requestParams = new RequestParams();

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        requestParams.put("fbUserId", SharedPreferenceManager.getFBUserId(context));
        requestParams.put("name", ticketRequest.getName());
        requestParams.put("profilePicUrl", ticketRequest.getUserPicUrl());
        requestParams.put("phoneNo", ticketRequest.getPhoneNo());
        requestParams.put("email", ticketRequest.getEmail());
        requestParams.put("type", ticketRequest.getReqType());
        requestParams.put("startToEnd", ticketRequest.getStartToEnd());
        requestParams.put("qty", ticketRequest.getQty());
        requestParams.put("ticketDate", ticketRequest.getTicketDate());
        requestParams.put("ticketTime", ticketRequest.getTicketTime());
        requestParams.put("ticketDay", ticketRequest.getTicketDay());
        requestParams.put("reqNote", ticketRequest.getReqDescription());

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.post(url, requestParams, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.post(url, requestParams, responseHandler);
        }
    }

    public void getTicketRequest(Context context, String taskMethod, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.GET_TICKET_REQUEST;

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.get(url, null, responseHandler);
        }
    }
}
