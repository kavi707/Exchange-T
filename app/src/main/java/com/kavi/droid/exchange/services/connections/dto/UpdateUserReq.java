package com.kavi.droid.exchange.services.connections.dto;

import android.support.annotation.NonNull;

/**
 * Created by kavi707 on 12/25/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class UpdateUserReq {

    private String email;
    private String name;
    private UpdateUserAdditionDataReq updateUserAdditionDataReq;

    public UpdateUserReq() {
    }

    public UpdateUserReq(@NonNull String email,
                         @NonNull String name,
                         @NonNull UpdateUserAdditionDataReq updateUserAdditionDataReq) {
        this.email = email;
        this.name = name;
        this.updateUserAdditionDataReq = updateUserAdditionDataReq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateUserAdditionDataReq getUpdateUserAdditionDataReq() {
        return updateUserAdditionDataReq;
    }

    public void setUpdateUserAdditionDataReq(UpdateUserAdditionDataReq updateUserAdditionDataReq) {
        this.updateUserAdditionDataReq = updateUserAdditionDataReq;
    }
}
