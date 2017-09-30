package com.kavi.droid.exchange.services.apiConnection;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kavi707 on 6/11/16.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ApiClient {

    /*public String getMetaData(String taskMethod, String industryId, String countryShortCode) {

        String response = null;
        IApiConnector apiConnector;

        if (taskMethod.equals(Constants.ASYNC_METHOD)) {
            apiConnector = new AsyncApiConnector();
            response = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_METADATA_NEW_MAIN + industryId
                    + Constants.GET_METADATA_NEW_SUB + countryShortCode, Constants.HTTP_GET, null);
        } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
            apiConnector = new SyncApiConnector();
            response = apiConnector.sendHttpGetOrDeleteRequest(Constants.BASE_URL + Constants.GET_METADATA_NEW_MAIN + industryId
                    + Constants.GET_METADATA_NEW_SUB + countryShortCode, Constants.HTTP_GET, null);
        }

        return response;
    }*/

    /*public String addNewBrand(String taskMethod, String industryId, String brandName) {

        String response = null;
        JSONObject reqObj = new JSONObject();
        IApiConnector apiConnector;
        try {
            reqObj.put("IndustryId", industryId);
            reqObj.put("BrandName", brandName);

            if (taskMethod.equals(Constants.ASYNC_METHOD)) {
                apiConnector = new AsyncApiConnector();
                response = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL + Constants.ADD_NEW_BRAND,
                        Constants.HTTP_POST, null, reqObj);
            } else if (taskMethod.equals(Constants.SYNC_METHOD)) {
                apiConnector = new SyncApiConnector();
                response = apiConnector.sendHttpJsonPostOrPutRequest(Constants.BASE_URL + Constants.ADD_NEW_BRAND,
                        Constants.HTTP_POST, null, reqObj);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return response;
    }*/

    public ApiClientResponse addNewUser(String taskMethod, User user) {

        ApiClientResponse apiClientResponse = null;
        JSONObject reqObj = new JSONObject();
        JSONObject additionalData = new JSONObject();
        IApiConnector apiConnector;
        try {
            reqObj.put("name", user.getFirstName() + " " + user.getLastName());
            reqObj.put("username", user.getEmail());
            reqObj.put("password", user.getUserId());

            additionalData.put("phoneNumber", user.getPhoneNumber());
            additionalData.put("fbUserId", user.getUserId());
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
}
