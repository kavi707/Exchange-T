package com.kavi.droid.exchange.services.connections;

import android.content.Context;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

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

        JSONObject reqObj = new JSONObject();
        JSONObject reqObjAdditional = new JSONObject();

        try {

            reqObj.put("name", user.getFirstName() + " " + user.getLastName());
            reqObj.put("username", user.getEmail());
            reqObj.put("password", user.getFbUserId());

            reqObjAdditional.put("phoneNumber", user.getPhoneNumber());
            reqObjAdditional.put("fbUserId", user.getFbUserId());
            reqObjAdditional.put("profilePicUrl", user.getProfilePicUrl());

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

        JSONObject reqObj = new JSONObject();

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

            String reqJsonString = reqObj.toString();

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                syncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                asyncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getTicketRequest(Context context, String taskMethod, JsonHttpResponseHandler responseHandler) {

        // Add filter for get latest month ticket request list
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(Constants.ALL_TICKET_REQUEST_LIMIT));
        long timeStampMonthAgo = calendar.getTimeInMillis() / 1000L;

        String url = Constants.BASE_URL + Constants.GET_TICKET_REQUEST +
                "?qry=select * where createdTime>" + timeStampMonthAgo;

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void getMyTicketRequests(Context context, String taskMethod, JsonHttpResponseHandler responseHandler) {

        String fbUserId = SharedPreferenceManager.getFBUserId(context);

        String url = Constants.BASE_URL + Constants.GET_MY_TICKET_REQUEST +
                "?qry=select * where entity.fbUserId=" + fbUserId;

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void submitPushToken(Context context, String taskMethod, String userId, String pushToken, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.SUBMIT_FCM_PUSH_TOKEN;

        JSONObject reqObj = new JSONObject();
        JSONObject reqObjPushData = new JSONObject();

        try {
            reqObj.put("userId", userId);

            reqObjPushData.put("regId", pushToken);
            reqObjPushData.put("notifier", Constants.NOTIFIER_GOOGLE);

            reqObj.put("push", reqObjPushData);

            String reqJsonString = reqObj.toString();

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                syncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                asyncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void checkCurrentTokenStatus(Context context, String taskMethod, JsonHttpResponseHandler responseHandler) {

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        String url = Constants.BASE_URL + Constants.CHECK_ACCESS_TOKEN_STATUS +
                "/" + authToken;

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void filterTicketRequest(Context context, String taskMethod, FilterTicketReq filterTicketReq, JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.GET_FILTER_TICKET_REQUEST;

        JSONObject reqObj = new JSONObject();
        JSONObject dateReqObj = new JSONObject();

        try {
            if (filterTicketReq.getTypeFilter() != -1)
                reqObj.put("type", filterTicketReq.getTypeFilter());

            if (filterTicketReq.getDateFilter() != null) {
                dateReqObj.put("from", filterTicketReq.getDateFilter().getFromDateTimestamp());
                if (filterTicketReq.getDateFilter().getToDateTimestamp() != -1)
                    dateReqObj.put("to", filterTicketReq.getDateFilter().getToDateTimestamp());
                reqObj.put("date", dateReqObj);
            }

            if (filterTicketReq.getDestinationFilter() != -1)
                reqObj.put("startToEnd", filterTicketReq.getDestinationFilter());

            if (filterTicketReq.getQtyFilter() != -1)
                reqObj.put("qty", filterTicketReq.getQtyFilter());

            String reqJsonString = reqObj.toString();

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                syncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                asyncHttpClient.post(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteMyTicketRequestFromId(Context context, String taskMethod, String requestId,
                                            JsonHttpResponseHandler responseHandler) {

        String url = Constants.BASE_URL + Constants.DELETE_TICKET_REQUEST +
                "/" + requestId;

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.delete(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.delete(url, null, responseHandler);
        }
    }
}
