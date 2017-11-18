package com.kavi.droid.exchange.services.notifications;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

/**
 * Created by kavi707 on 11/15/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ExTFirebaseIdService extends FirebaseInstanceIdService {

    private Context context = this;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG", "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        SharedPreferenceManager.setFCMPushToken(context, refreshedToken);
    }
}
