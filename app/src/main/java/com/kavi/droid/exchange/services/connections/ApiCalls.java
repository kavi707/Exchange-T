package com.kavi.droid.exchange.services.connections;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;
import com.kavi.droid.exchange.services.connections.dto.UpdateUserReq;
import com.kavi.droid.exchange.services.connections.exceptions.ExNoInternetException;
import com.kavi.droid.exchange.services.connections.exceptions.ExNonSuccessException;
import com.kavi.droid.exchange.services.connections.exceptions.ExServerErrorException;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by kavi707 on 10/3/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ApiCalls {

    public final String TAG = this.getClass().getName();

    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String APPLICATION_JSON = "application/json";

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SyncHttpClient syncHttpClient = new SyncHttpClient();

    /**
     * Create JsonHttpResponseHandler object
     * @param apiCallResponseHandler ApiCallResponseHandler object
     * @return JsonHttpResponseHandler
     */
    private JsonHttpResponseHandler getJsonHttpResponseHandler(final ApiCallResponseHandler apiCallResponseHandler) {
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "getJsonHttpResponseHandler: onSuccess: responseJsonString -> " + response.toString());
                apiCallResponseHandler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(final int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                if (errorResponse != null) {
                    Log.d(TAG, "getJsonHttpResponseHandler: onFailure: errorResponseJsonString -> " + errorResponse.toString());
                }

                Throwable connectionThrowable;

                if (throwable instanceof ConnectTimeoutException ||
                        throwable instanceof NetworkErrorException ||
                        throwable instanceof ConnectException ||
                        throwable instanceof SocketTimeoutException) {
                    connectionThrowable = new ExNoInternetException(throwable);
                    apiCallResponseHandler.onNoInternet(connectionThrowable);
                } else {
                    if (statusCode == 500) {
                        connectionThrowable = new ExServerErrorException(throwable);
                        apiCallResponseHandler.onServiceError(errorResponse, connectionThrowable);
                    } if (statusCode == 401) {
                        connectionThrowable = new ExServerErrorException(throwable);
                        apiCallResponseHandler.onUnAuthorized(errorResponse, connectionThrowable);
                    } else {
                        connectionThrowable = new ExNonSuccessException(throwable);
                    }
                }

                apiCallResponseHandler.onNonSuccess(statusCode, errorResponse, connectionThrowable);
            }
        };

        return responseHandler;
    }

    public void addNewUser(Context context, String taskMethod, User user, ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.ADD_USER;
        Log.d(TAG, "addNewUser: POST: url -> " + url);

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
            Log.d(TAG, "addNewUser: reqJsonString -> " + reqJsonString);

            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

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

    public void getUserFromFBId(String taskMethod, String fbUserId, ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.GET_FB_USER + fbUserId;
        Log.d(TAG, "getUserFromFBId: GET: url -> " + url);

        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD))
            syncHttpClient.get(url, null, responseHandler);
        else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void generateAuthToken(String taskMethod, String username, String password, ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.GENERATE_AUTH_TOKEN;
        Log.d(TAG, "generateAuthToken: POST: url -> " + url);

        RequestParams requestParams = new RequestParams();

        requestParams.put("username", username);
        requestParams.put("password", password);
        Log.d(TAG, "addNewUser: requestParams -> " + requestParams.toString());

        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.post(url, requestParams, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.post(url, requestParams, responseHandler);
        }
    }

    public void createTicketRequest(Context context, String taskMethod, TicketRequest ticketRequest,
                                    ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.ADD_TICKET_REQUEST;
        Log.d(TAG, "createTicketRequest: POST: url -> " + url);

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
            reqObj.put("ticketStatus", ticketRequest.getTicketStatus());

            String reqJsonString = reqObj.toString();
            Log.d(TAG, "createTicketRequest: reqJsonString -> " + reqJsonString);

            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

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

    public void getTicketRequest(Context context, String taskMethod, ApiCallResponseHandler apiCallResponseHandler) {

        // Add filter for get latest month ticket request list
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(Constants.ALL_TICKET_REQUEST_LIMIT));
        long timeStampMonthAgo = calendar.getTimeInMillis() / 1000L;

        String url = Constants.BASE_URL + Constants.GET_TICKET_REQUEST +
                "?qry=select * where createdTime>" + timeStampMonthAgo + "&sort=createdTime:-1";
        Log.d(TAG, "getTicketRequest: GET: url -> " + url);

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void getMyTicketRequests(Context context, String taskMethod, ApiCallResponseHandler apiCallResponseHandler) {

        String fbUserId = SharedPreferenceManager.getFBUserId(context);

        String url = Constants.BASE_URL + Constants.GET_MY_TICKET_REQUEST +
                "?qry=select * where entity.fbUserId=" + fbUserId + "&sort=createdTime:-1";
        Log.d(TAG, "getMyTicketRequests: GET: url -> " + url);

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void submitPushToken(Context context, String taskMethod, String userId, String pushToken, ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.SUBMIT_FCM_PUSH_TOKEN;
        Log.d(TAG, "submitPushToken: POST: url -> " + url);

        JSONObject reqObj = new JSONObject();
        JSONObject reqObjPushData = new JSONObject();

        try {
            reqObj.put("userId", userId);

            reqObjPushData.put("regId", pushToken);
            reqObjPushData.put("notifier", Constants.NOTIFIER_GOOGLE);

            reqObj.put("push", reqObjPushData);

            String reqJsonString = reqObj.toString();
            Log.d(TAG, "submitPushToken: reqJsonString -> " + reqJsonString);

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

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

    public void checkCurrentTokenStatus(Context context, String taskMethod, ApiCallResponseHandler apiCallResponseHandler) {

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);

        String url = Constants.BASE_URL + Constants.CHECK_ACCESS_TOKEN_STATUS +
                "/" + authToken;
        Log.d(TAG, "checkCurrentTokenStatus: GET: url -> " + url);

        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.get(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.get(url, null, responseHandler);
        }
    }

    public void filterTicketRequest(Context context, String taskMethod, FilterTicketReq filterTicketReq,
                                    ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.GET_FILTER_TICKET_REQUEST;
        Log.d(TAG, "filterTicketRequest: POST: url -> " + url);

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
            Log.d(TAG, "filterTicketRequest: reqJsonString -> " + reqJsonString);

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

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
                                            ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.DELETE_TICKET_REQUEST +
                "/" + requestId;
        Log.d(TAG, "deleteMyTicketRequestFromId: DELETE: url -> " + url);

        String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
        JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

        if (taskMethod.equals(Constants.SYNC_METHOD)) {
            syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            syncHttpClient.delete(url, null, responseHandler);
        } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
            asyncHttpClient.delete(url, null, responseHandler);
        }
    }

    public void updateUser(Context context, String taskMethod, String userId, UpdateUserReq updateUserReq,
                           ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.UPDATE_USER +
                "/" + userId;
        Log.d(TAG, "updateUserFromId: PUT: url -> " + url);

        JSONObject reqObj = new JSONObject();
        JSONObject dateReqObj = new JSONObject();

        try {

            if (updateUserReq.getEmail() != null)
                reqObj.put("username", updateUserReq.getEmail());

            if (updateUserReq.getName() != null)
                reqObj.put("name", updateUserReq.getName());

            if (updateUserReq.getUpdateUserAdditionDataReq() != null) {
                if (updateUserReq.getUpdateUserAdditionDataReq().getPhoneNumber() != null)
                    dateReqObj.put("phoneNumber", updateUserReq.getUpdateUserAdditionDataReq().getPhoneNumber());
                if (updateUserReq.getUpdateUserAdditionDataReq().getProfilePicUrl() != null)
                    dateReqObj.put("profilePicUrl", updateUserReq.getUpdateUserAdditionDataReq().getProfilePicUrl());
                if (updateUserReq.getUpdateUserAdditionDataReq().getFbUserId() != null)
                    dateReqObj.put("fbUserId", updateUserReq.getUpdateUserAdditionDataReq().getFbUserId());

                reqObj.put("additionalData", dateReqObj);
            }

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

            String reqJsonString = reqObj.toString();
            Log.d(TAG, "updateUser: reqJsonString -> " + reqJsonString);

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                syncHttpClient.put(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                asyncHttpClient.put(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateTicketStatus(Context context, String taskMethod, String ticketId, int newStatus,
                                   ApiCallResponseHandler apiCallResponseHandler) {

        String url = Constants.BASE_URL + Constants.UPDATE_TICKET_REQUEST +
                "/" + ticketId;
        Log.d(TAG, "updateUserFromId: PUT: url -> " + url);

        JSONObject reqObj = new JSONObject();

        try {

            reqObj.put("ticketStatus", newStatus);

            String authToken = SharedPreferenceManager.getNodegridAuthToken(context);
            JsonHttpResponseHandler responseHandler = getJsonHttpResponseHandler(apiCallResponseHandler);

            String reqJsonString = reqObj.toString();
            Log.d(TAG, "updateTicketStatus: reqJsonString -> " + reqJsonString);

            if (taskMethod.equals(Constants.SYNC_METHOD)) {
                syncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                syncHttpClient.put(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            } else if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                asyncHttpClient.addHeader(HEADER_AUTHORIZATION, authToken);
                asyncHttpClient.put(context, url, new StringEntity(reqJsonString), APPLICATION_JSON, responseHandler);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
