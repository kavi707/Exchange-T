package com.kavi.droid.exchange.models;

/**
 * Created by kavi707 on 9/30/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ApiClientResponse {

    private int httpStatusCode;
    private String responseObj;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(String responseObj) {
        this.responseObj = responseObj;
    }
}
