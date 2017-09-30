package com.kavi.droid.exchange.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class CommonUtils {

    /**
     * check the internet connection in the device for running application
     * @param context
     * @return boolean
     */
    public boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    public ApiClientResponse generateApiClientResponse(int statusCode, String response) {

        ApiClientResponse apiClientResponse = null;

        if (response != null) {
            apiClientResponse.setHttpStatusCode(statusCode);
            apiClientResponse.setResponseObj(response);
        }

        return apiClientResponse;
    }

    public User populateDataFromFB(String jsonString) {

        User appUser = null;

        if (jsonString != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonString);

                appUser = new User();

                // User Id
                String id = jsonData.getString("id");
                appUser.setUserId(id);

                // User first name
                if (!jsonData.isNull("first_name")) {
                    String firstName = jsonData.getString("first_name");
                    appUser.setFirstName(firstName);
                } else {
                    appUser.setFirstName("");
                }

                // User last name
                if (!jsonData.isNull("last_name")) {
                    String lastName = jsonData.getString("last_name");
                    appUser.setLastName(lastName);
                } else {
                    appUser.setLastName("");
                }

                // Email
                if (!jsonData.isNull("email")) {
                    String email = jsonData.getString("email");
                    appUser.setEmail(email);
                } else {
                    appUser.setEmail("");
                }

                // Profile pic
                URL profilePic = null;
                try {
                    profilePic = new URL("https://graph.facebook.com/" + id + "/picture?width=" +
                            FBManager.PROFILE_PIC_WIDTH + "&height=" + FBManager.PROFILE_PIC_HEIGHT);
                    appUser.setProfilePicUrl(profilePic.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return appUser;
    }
}
