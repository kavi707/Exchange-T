package com.kavi.droid.exchange.services.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by kavi707 on 11/15/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ExTFirebaseMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAG", "From: " + remoteMessage.getFrom());
        Log.d("TAG", "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
