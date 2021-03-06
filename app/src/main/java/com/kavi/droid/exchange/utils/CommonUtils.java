package com.kavi.droid.exchange.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.login.LoginManager;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.models.User;
import com.kavi.droid.exchange.services.loginManagers.FBManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public void setLocal(Activity activity) {

        String languageToLoad;
        String selectedLocal = SharedPreferenceManager.getSelectedLocal(activity);
        if (!selectedLocal.equals("NULL")) {
            languageToLoad = selectedLocal;
        } else {
            SharedPreferenceManager.setSelectedLocal(activity, "en");
            languageToLoad = "en";
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public void logoutApplication(Context context, boolean isAlsoFromFB) {

        if (isAlsoFromFB) {
            LoginManager.getInstance().logOut();

            SharedPreferenceManager.setIsLogIn(context, false);
            SharedPreferenceManager.setIsUserDataCaptured(context, false);
            SharedPreferenceManager.setLoggedUserName(context, null);
            SharedPreferenceManager.setLoggedUserEmail(context, null);
            SharedPreferenceManager.setLoggedUserImageUrl(context, null);
            SharedPreferenceManager.setLoggedUserNumber(context, null);
            SharedPreferenceManager.setFBUserId(context, null);
        }

        SharedPreferenceManager.setNodegridAuthToken(context, null);
    }

    public User populateDataFromFB(String jsonString) {

        User appUser = null;

        if (jsonString != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonString);

                appUser = new User();

                // User Id
                String id = jsonData.getString("id");
                appUser.setFbUserId(id);

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

    public int getTypeFromName(String type) {
        if (type.equals("I Need")) {
            return TicketRequest.I_NEED;
        } else if (type.equals("I Have")) {
            return TicketRequest.I_HAVE;
        } else {
            return -1;
        }
    }

    public String getTypeFromInt(int type) {
        if (type == TicketRequest.I_NEED) {
            return "I Need";
        } else if (type == TicketRequest.I_HAVE) {
            return "I Have";
        } else {
            return "Unknown";
        }
    }

    public int getDestinationFromName(String destination) {

        if (destination.equals("COLOMBO - KANDY")) {
            return TicketRequest.COLOMBO_KANDY;
        } else if (destination.equals("KANDY - COLOMBO")) {
            return TicketRequest.KANDY_COLOMBO;
        } else if (destination.equals("COLOMBO - BADULLA")) {
            return TicketRequest.COLOMBO_BADULLA;
        } else if (destination.equals("BADULLA - COLOMBO")) {
            return TicketRequest.BADULLA_COLOMBO;
        } else if (destination.equals("COLOMBO - KURUNEGALA")) {
            return TicketRequest.COLOMBO_KURUNEGALA;
        } else if (destination.equals("KURUNEGALA - COLOMBO")) {
            return TicketRequest.KURUNEGALA_COLOMBO;
        } else if (destination.equals("COLOMBO - ANURADHAPURA")) {
            return TicketRequest.COLOMBO_ANURADHAPURA;
        } else if (destination.equals("ANURADHAPURA - COLOMBO")) {
            return TicketRequest.ANURADHAPURA_COLOMBO;
        } else if (destination.equals("COLOMBO - JAFFNA")) {
            return TicketRequest.COLOMBO_JAFFNA;
        } else if (destination.equals("JAFFNA - COLOMBO")) {
            return TicketRequest.JAFFNA_COLOMBO;
        } else if (destination.equals("COLOMBO - VAUNIYA")) {
            return TicketRequest.COLOMBO_VAUNIYA;
        } else if (destination.equals("VAUNIYA - COLOMBO")) {
            return TicketRequest.VAUNIYA_COLOMBO;
        } else {
            return -1;
        }
    }

    public String getDestinationFromInt(int destination) {

        if (destination == TicketRequest.COLOMBO_KANDY) {
            return "COLOMBO - KANDY";
        } else if (destination == TicketRequest.KANDY_COLOMBO) {
            return "KANDY - COLOMBO";
        } else if (destination == TicketRequest.COLOMBO_BADULLA) {
            return "COLOMBO - BADULLA";
        } else if (destination == TicketRequest.BADULLA_COLOMBO) {
            return "BADULLA - COLOMBO";
        } else if (destination == TicketRequest.COLOMBO_KURUNEGALA) {
            return "COLOMBO - KURUNEGALA";
        } else if (destination == TicketRequest.KURUNEGALA_COLOMBO) {
            return "KURUNEGALA - COLOMBO";
        } else if (destination == TicketRequest.COLOMBO_ANURADHAPURA) {
            return "COLOMBO - ANURADHAPURA";
        } else if (destination == TicketRequest.ANURADHAPURA_COLOMBO) {
            return "ANURADHAPURA - COLOMBO";
        } else if (destination == TicketRequest.COLOMBO_JAFFNA) {
            return "COLOMBO - JAFFNA";
        } else if (destination == TicketRequest.JAFFNA_COLOMBO) {
            return "JAFNA - COLOMBO";
        } else if (destination == TicketRequest.COLOMBO_VAUNIYA) {
            return "COLOMBO - VAUNIYA";
        } else if (destination == TicketRequest.VAUNIYA_COLOMBO) {
            return "VAUNIYA - COLOMBO";
        } else {
            return "Unknown - Unknown";
        }
    }

    public List<String> initDestinationList() {

        List<String> destinationNameList = new ArrayList<>();

        destinationNameList.add("Select Destination");
        destinationNameList.add("COLOMBO - KANDY");
        destinationNameList.add("KANDY - COLOMBO");
        destinationNameList.add("COLOMBO - BADULLA");
        destinationNameList.add("BADULLA - COLOMBO");
        destinationNameList.add("COLOMBO - KURUNEGALA");
        destinationNameList.add("KURUNEGALA - COLOMBO");
        destinationNameList.add("COLOMBO - ANURADHAPURA");
        destinationNameList.add("ANURADHAPURA - COLOMBO");
        destinationNameList.add("COLOMBO - JAFFNA");
        destinationNameList.add("JAFFNA - COLOMBO");
        destinationNameList.add("COLOMBO - VAUNIYA");
        destinationNameList.add("VAUNIYA - COLOMBO");

        return destinationNameList;
    }

    public List<String> initTypeList() {

        List<String> typeNameList = new ArrayList<>();

        typeNameList.add("Select Type");
        typeNameList.add("I Need");
        typeNameList.add("I Have");

        return typeNameList;
    }

    public List<String> initQtyList() {

        List<String> qtyList = new ArrayList<>();

        qtyList.add("Select Quantity");
        qtyList.add("01");
        qtyList.add("02");
        qtyList.add("03");
        qtyList.add("04");
        qtyList.add("05");
        qtyList.add("06");
        qtyList.add("07");
        qtyList.add("08");
        qtyList.add("09");
        qtyList.add("10");

        return qtyList;
    }

    public String orderDate(int year, int month, int day) {

        String dateString = null;
        try {
            String givenDate = year + "-" + month + "-" + day;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(givenDate);

            dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);

            dateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public long getTimestampFromDate(String date) {
        long dateTimestamp = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
            Date gotDate = dateFormat.parse(date);
            dateTimestamp = gotDate.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTimestamp;
    }

    public long getTimestampFromDateTime(String date) {
        long dateTimestamp = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss", Locale.ENGLISH);

            Date gotDate = dateFormat.parse(date);
            dateTimestamp = gotDate.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTimestamp;
    }

    public List<TicketRequest> getTicketRequestList(JSONObject jsonData) {

        List<TicketRequest> ticketRequestList = null;

        try {
            ticketRequestList = new ArrayList<>();

            JSONArray jsonResArray = jsonData.getJSONArray("res");
            JSONObject resObj, entityObj;
            TicketRequest ticketRequest;
            for (int i = 0; i < jsonResArray.length(); i++) {
                resObj = jsonResArray.getJSONObject(i).getJSONObject("data");
                entityObj = resObj.getJSONObject("entity");

                ticketRequest = new TicketRequest();
                ticketRequest.setId(jsonResArray.getJSONObject(i).getString("_id"));
                ticketRequest.setReqDate(resObj.getLong("createdTime"));
                ticketRequest.setFbId(entityObj.getString("fbUserId"));
                ticketRequest.setName(entityObj.getString("name"));
                ticketRequest.setUserPicUrl(entityObj.getString("profilePicUrl"));
                ticketRequest.setPhoneNo(entityObj.getString("phoneNo"));
                ticketRequest.setEmail(entityObj.getString("email"));
                ticketRequest.setReqType(entityObj.getInt("type"));
                ticketRequest.setStartToEnd(entityObj.getInt("startToEnd"));
                ticketRequest.setQty(entityObj.getInt("qty"));
                ticketRequest.setTicketDate(entityObj.getString("ticketDate"));
                ticketRequest.setTicketTime(entityObj.getString("ticketTime"));
                ticketRequest.setTicketDay(entityObj.getString("ticketDay"));
                ticketRequest.setReqDescription(entityObj.getString("reqNote"));
                ticketRequest.setTicketStatus(entityObj.getInt("ticketStatus"));

                ticketRequestList.add(ticketRequest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ticketRequestList;
    }

    public String getUserIdFromResponse(JSONObject userRes) {

        String userId = null;

        try {
            userId = userRes.getJSONObject("res").getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public String getUserIdFromArrayResponse(JSONObject userRes) {

        String userId = null;

        try {
            userId = userRes.getJSONArray("res").getJSONObject(0).getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public List<TicketRequest> getINeedTicketList(List<TicketRequest> fullTicketRequestList) {
        List<TicketRequest> ticketRequestList = new ArrayList<>();

        for (TicketRequest ticketRequest : fullTicketRequestList) {
            if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
                ticketRequestList.add(ticketRequest);
            }
        }

        return ticketRequestList;
    }

    public List<TicketRequest> getIHaveTicketList(List<TicketRequest> fullTicketRequestList) {
        List<TicketRequest> ticketRequestList = new ArrayList<>();;

        for (TicketRequest ticketRequest : fullTicketRequestList) {
            if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
                ticketRequestList.add(ticketRequest);
            }
        }

        return ticketRequestList;
    }
}
