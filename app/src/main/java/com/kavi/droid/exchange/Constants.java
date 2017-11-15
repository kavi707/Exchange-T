package com.kavi.droid.exchange;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class Constants {

    public static final String LOG_TAG = "EXCHANGE-T";

    public static final String ASYNC_METHOD = "ASYNC";
    public static final String SYNC_METHOD = "SYNC";

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_DELETE = "DELETE";

    public static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT";
    public static final String HISTORY_FRAGMENT_TAG = "HISTORY_FRAGMENT";
    public static final String NOTIFICATION_FRAGMENT_TAG = "NOTIFICATION_FRAGMENT";
    public static final String MESSAGE_FRAGMENT_TAG = "MESSAGE_FRAGMENT";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT";

    //public static final String BASE_URL = "http://10.0.2.2:8000";
    //public static final String BASE_URL = "http://13.59.85.152:8000";
    public static final String BASE_URL = "http://10.140.48.254:8000";

    public static final String ADD_USER = "/system/user/";
    public static final String GET_FB_USER = "/system/user/fb/";
    public static final String GENERATE_AUTH_TOKEN = "/system/security/generateToken";

    public static final String ADD_TICKET_REQUEST = "/app/ticket_requests";
    public static final String GET_TICKET_REQUEST = "/app/ticket_requests";
    public static final String GET_MY_TICKET_REQUEST = "/app/advance/ticket_requests";
}
