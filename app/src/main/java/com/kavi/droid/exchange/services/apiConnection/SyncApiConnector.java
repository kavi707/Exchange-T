package com.kavi.droid.exchange.services.apiConnection;

import android.util.Log;

import com.kavi.droid.exchange.Constants;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kavi707 on 6/11/16.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class SyncApiConnector implements IApiConnector {

    private String requestUrl;
    private String requestMethod;
    private String postOrPutRequestUrl;
    private Map<String, String> getAdditionalHeaders;
    private JSONObject reqParams;
    private String httpCommonResponse  = "NULL";

    /**
     * Method for sending HTTP GET or DELETE requests to api
     * @param url End point url (String)
     * @param requestMethod Request method HTTP GET or DELETE (String)
     * @param additionalHeaders Request HTTP headers (Map<String, String> - header key & heade value)
     * @return NodeGridResponse object
     */
    @Override
    public String sendHttpGetOrDeleteRequest(String url, String requestMethod, Map<String, String> additionalHeaders) {

        Log.d("SyncApiConnector", "SyncApiConnector:sendHttpGetOrDeleteRequest");
        this.requestUrl = url;
        this.requestMethod = requestMethod;
        this.getAdditionalHeaders = additionalHeaders;

        httpCommonResponse = sendHttpRequest();

        return httpCommonResponse;
    }

    /**
     * Background Task for send HTTP GET or DELETE Request
     */
    private String sendHttpRequest() {
        String responseResult = null;
        InputStream inputStream = null;

        try {
            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpRequest");
            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpRequest / Req Url : " + requestUrl);

            URL reqUrl = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();

            if (requestMethod.equals(Constants.HTTP_DELETE))
                connection.setRequestMethod("DELETE");

            if (getAdditionalHeaders != null) {
                Set<String> headerKeys = getAdditionalHeaders.keySet();
                for (String key : headerKeys) {
                    connection.setRequestProperty(key, getAdditionalHeaders.get(key));
                }
            }

            if (connection.getRequestMethod().equals(Constants.HTTP_DELETE)) {
                // If request is HTTP DELETE, creating the response object
                // In HTTP DELETE ignore the response object java httpClient
                int deleteStatusCode = connection.getResponseCode();
                if (deleteStatusCode == 200) {
                    responseResult = "{\"status\":\"SUCCESS\", \"msg\":\"Data deleted successfully\"}";
                } else if (deleteStatusCode == 204) {
                    responseResult = "{\"name\":\"AppError\", \"message\":\"No Content found to delete\"}";
                } else {
                    responseResult = "{\"name\":\"AppError\", \"message\":" + connection.getResponseMessage() + "}";
                }
            } else {
                // If request is not and HTTP DELETE, then read the input stream and extract json string
                inputStream = connection.getInputStream();

                if (inputStream != null) {
                    responseResult = convertInputStreamToString(inputStream);
                } else {
                    responseResult = "{\"name\":\"AppError\", \"message\":" + connection.getResponseMessage() + "}";
                }
            }

            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpGetOrDeleteRequest / Response : " + responseResult);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException ex) {
            Log.d("AsyncApiConnector", "AsyncApiConnector:SendHttpJSONPostTask / Exception: " + ex.toString());
            responseResult = "{\"name\":\"ExceptionError\", \"message\":\"Connection Timeout\"}";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseResult;
    }

    /**
     * Create String object from InputStream
     * @param inputStream HttpResponse extracted InputStream
     * @return String object
     * @throws IOException
     */
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /**
     * Method for sending HTTP POST requests to api using JSON data
     * @param url End point url (String)
     * @param requestMethod Request method HTTP POST or PUT (String)
     * @param additionalHeader Request HTTP headers (Map<String, String> - header key & heade value)
     * @param reqParams POST request body parameters (JSONObject)
     * @return NodeGridResponse object
     */
    @Override
    public String sendHttpJsonPostOrPutRequest(String url, String requestMethod, Map<String, String> additionalHeader, JSONObject reqParams) {
        Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPostOrPutRequest");
        this.postOrPutRequestUrl = url;
        this.requestMethod = requestMethod;
        this.getAdditionalHeaders = additionalHeader;
        this.reqParams = reqParams;

        httpCommonResponse = sendHttpJsonPost();

        return httpCommonResponse;
    }

    /**
     * Background Task for send HTTP POST or PUT with JSON data
     */
    private String sendHttpJsonPost() {
        String responseResult = "";

        try {
            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPost / Req Url : " + postOrPutRequestUrl);
            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPost / Req Params : " + reqParams.toString());

            URL url = new URL(postOrPutRequestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (getAdditionalHeaders != null) {
                Set<String> headerKeys = getAdditionalHeaders.keySet();
                for (String key : headerKeys) {
                    connection.setRequestProperty(key, getAdditionalHeaders.get(key));
                }
            }
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestMethod(requestMethod);
            OutputStreamWriter post = new OutputStreamWriter(connection.getOutputStream());
            post.write(reqParams.toString());
            post.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.equals("null"))
                    responseResult += inputLine;
            }
            post.close();
            in.close();

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                if (responseResult.equals("")) {
                    responseResult = "{\"status\":\"success\"}";
                }
                Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPost / Status: Success Response : " + responseResult);
            }
            if (statusCode == 404) {
                if (responseResult.equals("")) {
                    responseResult = "{\"name\":\"AppError\", \"message\":\"End point not found\"}";
                } else {
                    Object json = new JSONTokener(responseResult).nextValue();
                    if (!(json instanceof JSONObject)) {
                        responseResult = "{\"name\":\"AppError\", \"message\":\"End point not found\"}";
                    }
                }
            } else {
                Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPost / Status code: " + String.valueOf(statusCode));
            }

        } catch (HttpHostConnectException ex) {
            Log.d("AsyncApiConnector", "AsyncApiConnector:SendHttpJSONPostTask / Exception: " + ex.toString());
            responseResult = "{\"name\":\"ExceptionError\", \"message\":\"Connection Refused\"}";
        } catch (ConnectTimeoutException ex) {
            Log.d("AsyncApiConnector", "AsyncApiConnector:SendHttpJSONPostTask / Exception: " + ex.toString());
            responseResult = "{\"name\":\"ExceptionError\", \"message\":\"Connection Timeout\"}";
        } catch (Exception ex) {
            Log.d("SyncApiConnector", "SyncApiConnector:sendHttpJsonPost / Exception: " + ex.toString());
        }

        return responseResult;
    }
}
