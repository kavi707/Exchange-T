package com.kavi.droid.exchange;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class Constants {

    public static final String LOG_TAG = "EXCHANGE-T";

    public static final String EVENT_TRAIN = "Train";

    public static final String ASYNC_METHOD = "ASYNC";
    public static final String SYNC_METHOD = "SYNC";

    public static final int ALL_TICKET_REQUEST_LIMIT = 15; // Get latest 14 days ticket request

    public static final String NOTIFIER_GOOGLE = "google";

    public static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT";
    public static final String HISTORY_FRAGMENT_TAG = "HISTORY_FRAGMENT";
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT";
    public static final String NOTIFICATION_FRAGMENT_TAG = "NOTIFICATION_FRAGMENT";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT";

    // For Emulator
    //public static final String BASE_URL = "http://10.0.2.2:8000";

    // Amazon instance 2017
//    public static final String BASE_URL = "http://13.59.85.152:8000";
    public static final String BASE_URL = BuildConfig.SERVER_URL;

    // Amazon instance 2018
    //public static final String BASE_URL = "http://18.217.205.19:8000";

    public static final String ADD_USER = "/system/user/";
    public static final String UPDATE_USER = "/app/users/updateUser";
    public static final String GET_FB_USER = "/system/user/fb/";
    public static final String GENERATE_AUTH_TOKEN = "/system/security/generateToken";

    public static final String ADD_TICKET_REQUEST = "/app/ticket_requests";
    public static final String GET_TICKET_REQUEST = "/app/advance/ticket_requests";
    public static final String UPDATE_TICKET_REQUEST = "/app/ticket_requests";
    public static final String DELETE_TICKET_REQUEST = "/app/ticket_requests";
    public static final String GET_MY_TICKET_REQUEST = "/app/advance/ticket_requests";
    public static final String SUBMIT_FCM_PUSH_TOKEN = "/app/push/push_tokens/store";
    public static final String CHECK_ACCESS_TOKEN_STATUS = "/system/security/tokenStatus";
    public static final String GET_FILTER_TICKET_REQUEST = "/app/ticket_requests/filter";
}
