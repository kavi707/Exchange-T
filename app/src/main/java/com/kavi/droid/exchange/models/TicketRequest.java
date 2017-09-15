package com.kavi.droid.exchange.models;

import java.util.Date;

/**
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class TicketRequest {

    public static final int I_NEED = 0;
    public static final int I_HAVE = 1;

    public static final int COLOMBO_KANDY = 0;
    public static final int KANDY_COLOMBO = 1;
    public static final int COLOMBO_BADULLA = 2;
    public static final int BADULLA_COLOMBO = 3;

    public static final int COLOMBO_KURUNEGALA = 4;
    public static final int KURUNEGALA_COLOMBO = 5;
    public static final int COLOMBO_ANURADHAPURA = 6;
    public static final int ANURADHAPURA_COLOMBO = 7;
    public static final int COLOMBO_JAFNA = 8;
    public static final int JAFNA_COLOMBO = 9;
    public static final int COLOMBO_VAUNIYA = 10;
    public static final int VAUNIYA_COLOMBO = 11;

    private int reqType;
    private int startToEnd;
    private String reqDescription;
    private long reqDate;
    private String ticketDate;
    private String ticketTime;
    private String ticketDay;
    private String userPicUrl;

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public int getStartToEnd() {
        return startToEnd;
    }

    public void setStartToEnd(int startToEnd) {
        this.startToEnd = startToEnd;
    }

    public String getReqDescription() {
        return reqDescription;
    }

    public void setReqDescription(String reqDescription) {
        this.reqDescription = reqDescription;
    }

    public long getReqDate() {
        return reqDate;
    }

    public void setReqDate(long reqDate) {
        this.reqDate = reqDate;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getTicketDay() {
        return ticketDay;
    }

    public void setTicketDay(String ticketDay) {
        this.ticketDay = ticketDay;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
