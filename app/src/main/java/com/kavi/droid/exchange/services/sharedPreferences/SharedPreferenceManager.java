package com.kavi.droid.exchange.services.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class SharedPreferenceManager {

    private static final String EXCHANGE_T_SHARED_PREFERENCES = "EXCHANGE_T_SHARED_PREFERENCES";

    //Shared Preference constants
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String FB_USER_TOKEN = "FB_USER_TOKEN";
    private static final String FB_USER_ID = "FB_USER_ID";
    private static final String USER_ID = "USER_ID";
    private static final String NODEGRID_AUTH_TOKEN = "NODEGRID_AUTH_TOKEN";
    private static final String FB_USER_DATA = "FB_USER_DATA";
    private static final String IS_USER_DATA_CAPTURED = "IS_USER_DATA_CAPTURED";
    private static final String IS_I_NEED_SELECTED = "IS_I_NEED_SELECTED";
    private static final String LOGGED_USER_NAME = "LOGGED_USER_NAME";
    private static final String LOGGED_USER_EMAIL = "LOGGED_USER_EMAIL";
    private static final String LOGGED_USER_IMAGE_URL = "LOGGED_USER_IMAGE_URL";
    private static final String LOGGED_USER_NUMBER = "LOGGED_USER_NUMBER";
    private static final String FCM_PUSH_TOKEN = "FCM_PUSH_TOKEN";
    private static final String LAST_FILTER_OBJECT = "LAST_FILTER_OBJECT";
    private static final String IS_TICKET_STATUS_UPDATED = "IS_TICKET_STATUS_UPDATED";

    /**
     * Store boolean shared preference value
     * @param preferenceKey preference key
     * @param preferenceValue preference value
     */
    private static void writeBooleanSharePreference(Context context, String preferenceKey, boolean preferenceValue) {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(preferenceKey, preferenceValue);

        // Commit the edits!
        editor.commit();
    }

    /**
     * Get boolean share preference value
     * @param preferenceKey preference key
     * @return boolean value
     */
    private static boolean readBooleanSharePreference(Context context, String preferenceKey) {
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);
        return settings.getBoolean(preferenceKey, false);
    }

    /**
     * Store String shared preference value
     * @param preferenceKey preference key
     * @param preferenceValue preference value
     */
    private static void writeStringSharePreference(Context context, String preferenceKey, String preferenceValue) {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preferenceKey, preferenceValue);

        // Commit the edits!
        editor.commit();
    }

    /**
     * Get String share preference value
     * @param preferenceKey preference key
     * @return String value
     */
    private static String readStringSharePreference(Context context, String preferenceKey) {
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);
        return settings.getString(preferenceKey, "NULL");
    }

    /**
     * Store String shared preference value
     * @param preferenceKey preference key
     * @param preferenceValue preference value
     */
    private static void writeObjectSharePreference(Context context, String preferenceKey, Object preferenceValue) {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonString = gson.toJson(preferenceValue);

        editor.putString(preferenceKey, jsonString);

        // Commit the edits!
        editor.commit();
    }

    /**
     * Get String share preference value
     * @param preferenceKey preference key
     * @return String value
     */
    private static Object readObjectSharePreference(Context context, String preferenceKey, Class objectClass) {
        SharedPreferences settings = context.getSharedPreferences(EXCHANGE_T_SHARED_PREFERENCES, 0);

        Gson gson = new Gson();
        String jsonString = settings.getString(preferenceKey, "NULL");

        return gson.fromJson(jsonString, objectClass);
    }

    /**********************************************************************************************/
    /************************ Shared Preference Getters & Setters *********************************/
    /**********************************************************************************************/

    public static void setIsLogIn(Context context, boolean value){
        writeBooleanSharePreference(context, IS_USER_LOGGED_IN, value);
    }

    public static boolean isUserLogIn(Context context){
        return readBooleanSharePreference(context, IS_USER_LOGGED_IN);
    }

    public static void setFBUserToken(Context context, String value) {
        writeStringSharePreference(context, FB_USER_TOKEN, value);
    }

    public static String getFBUserToken(Context context){
        return readStringSharePreference(context, FB_USER_TOKEN);
    }

    public static void setFBUserId(Context context, String value) {
        writeStringSharePreference(context, FB_USER_ID, value);
    }

    public static String getFBUserId(Context context){
        return readStringSharePreference(context, FB_USER_ID);
    }

    public static void setUserId(Context context, String value) {
        writeStringSharePreference(context, USER_ID, value);
    }

    public static String getUserId(Context context){
        return readStringSharePreference(context, USER_ID);
    }

    public static void setNodegridAuthToken(Context context, String value) {
        writeStringSharePreference(context, NODEGRID_AUTH_TOKEN, value);
    }

    public static String getNodegridAuthToken(Context context){
        return readStringSharePreference(context, NODEGRID_AUTH_TOKEN);
    }

    public static void setFBUserData(Context context, String value) {
        writeStringSharePreference(context, FB_USER_DATA, value);
    }

    public static String getFBUserData(Context context){
        return readStringSharePreference(context, FB_USER_DATA);
    }

    public static void setIsUserDataCaptured(Context context, boolean value){
        writeBooleanSharePreference(context, IS_USER_DATA_CAPTURED, value);
    }

    public static boolean isUserDataCaptured(Context context){
        return readBooleanSharePreference(context, IS_USER_DATA_CAPTURED);
    }

    public static void setLoggedUserName(Context context, String value) {
        writeStringSharePreference(context, LOGGED_USER_NAME, value);
    }

    public static String getLoggedUserName(Context context){
        return readStringSharePreference(context, LOGGED_USER_NAME);
    }

    public static void setLoggedUserEmail(Context context, String value) {
        writeStringSharePreference(context, LOGGED_USER_EMAIL, value);
    }

    public static String getLoggedUserEmail(Context context){
        return readStringSharePreference(context, LOGGED_USER_EMAIL);
    }

    public static void setLoggedUserImageUrl(Context context, String value) {
        writeStringSharePreference(context, LOGGED_USER_IMAGE_URL, value);
    }

    public static String getLoggedUserImageUrl(Context context){
        return readStringSharePreference(context, LOGGED_USER_IMAGE_URL);
    }

    public static void setLoggedUserNumber(Context context, String value) {
        writeStringSharePreference(context, LOGGED_USER_NUMBER, value);
    }

    public static String getLoggedUserNumber(Context context){
        return readStringSharePreference(context, LOGGED_USER_NUMBER);
    }

    public static void setFCMPushToken(Context context, String value) {
        writeStringSharePreference(context, FCM_PUSH_TOKEN, value);
    }

    public static String getFCMPushToken(Context context){
        return readStringSharePreference(context, FCM_PUSH_TOKEN);
    }

    public static void setIsINeedTypeSelected(Context context, boolean value){
        writeBooleanSharePreference(context, IS_I_NEED_SELECTED, value);
    }

    public static boolean isINeedTypeSelected(Context context){
        return readBooleanSharePreference(context, IS_I_NEED_SELECTED);
    }

    public static void setLastFilterObject(Context context, FilterTicketReq value) {
        writeObjectSharePreference(context, LAST_FILTER_OBJECT, value);
    }

    public static FilterTicketReq getLastFilterObject(Context context){
        return (FilterTicketReq) readObjectSharePreference(context, LAST_FILTER_OBJECT, FilterTicketReq.class);
    }

    public static void setIsTicketStatusUpdated(Context context, boolean value){
        writeBooleanSharePreference(context, IS_TICKET_STATUS_UPDATED, value);
    }

    public static boolean isTicketStatusUpdated(Context context){
        return readBooleanSharePreference(context, IS_TICKET_STATUS_UPDATED);
    }
}
