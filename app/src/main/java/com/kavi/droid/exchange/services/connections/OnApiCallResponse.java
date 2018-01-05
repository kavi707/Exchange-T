package com.kavi.droid.exchange.services.connections;

import org.json.JSONObject;

/**
 * Created by pl36586 on 12/26/17.
 */

public interface OnApiCallResponse {
    void onSuccess(int statusCode, JSONObject response);
    void onNoInternet();
    void onUnAuthorized(JSONObject response, Throwable throwable);
    void onNonSuccess(int statusCode, JSONObject response, Throwable throwable);
    void onServiceError(JSONObject response, Throwable throwable);
}
