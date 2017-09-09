package com.kavi.droid.exchange.services.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class SharedPreferenceManager {

    private static final String EXCHANGE_T_SHARED_PREFERENCES = "EXCHANGE_T_SHARED_PREFERENCES";

    //Shared Preference constants
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String FB_USER_TOKEN = "FB_USER_TOKEN";
    private static final String FB_USER_DATA = "FB_USER_DATA";
    private static final String IS_USER_DATA_CAPTURED = "IS_USER_DATA_CAPTURED";

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

    public static void setFBUserData(Context context, String value) {
        writeStringSharePreference(context, FB_USER_DATA, value);
    }

    public static String getFBUserData(Context context){
        return readStringSharePreference(context, FB_USER_DATA);
    }

    public static void setIsUserDataCaptured(Context context, boolean value){
        writeBooleanSharePreference(context, IS_USER_LOGGED_IN, value);
    }

    public static boolean isUserDataCaptured(Context context){
        return readBooleanSharePreference(context, IS_USER_LOGGED_IN);
    }
}
