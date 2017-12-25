package com.kavi.droid.exchange.services.connections.dto;

/**
 * Created by kavi707 on 12/25/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class UpdateUserAdditionDataReq {

    private String phoneNumber;
    private String profilePicUrl;
    private String fbUserId;

    public UpdateUserAdditionDataReq() {
    }

    public UpdateUserAdditionDataReq(String phoneNumber, String profilePicUrl, String fbUserId) {
        this.fbUserId = fbUserId;
        this.phoneNumber = phoneNumber;
        this.profilePicUrl = profilePicUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }
}
