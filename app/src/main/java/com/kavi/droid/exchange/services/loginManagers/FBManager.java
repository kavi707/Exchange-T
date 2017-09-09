package com.kavi.droid.exchange.services.loginManagers;

import android.os.Bundle;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kavi707 on 9/9/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class FBManager {

    public static final int PROFILE_PIC_WIDTH = 150;
    public static final int PROFILE_PIC_HEIGHT = 200;

    /**
     * Initiate FB permission ArrayList
     * @return ArrayList<String>
     */
    public List<String> getFbReadPermissions() {
        List<String> fbReadPermissions;
        fbReadPermissions = new ArrayList<String>(){{
            add("email");
            add("public_profile");
            add("user_friends");
            add("user_birthday");
            add("user_location");
        }};

        return fbReadPermissions;
    }

    /**
     * Initiate FB retrieve data bundle
     * @return Bundle
     */
    public Bundle getProfileParameters() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
        return parameters;
    }
}
