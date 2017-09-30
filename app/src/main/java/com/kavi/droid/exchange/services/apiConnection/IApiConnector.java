package com.kavi.droid.exchange.services.apiConnection;

import com.kavi.droid.exchange.models.ApiClientResponse;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kavi707 on 6/11/16.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public interface IApiConnector {

    ApiClientResponse sendHttpGetOrDeleteRequest(String url, String requestMethod, Map<String, String> additionalHeaders);

    ApiClientResponse sendHttpJsonPostOrPutRequest(String url, String requestMethod, Map<String, String> additionalHeaders, JSONObject reqParams);
}
