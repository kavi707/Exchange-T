package com.kavi.droid.exchange.services.connections.dto;

/**
 * Created by kavi707 on 11/23/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class FilterTicketReqDate {

    private long fromDateTimestamp;
    private long toDateTimestamp;

    public long getFromDateTimestamp() {
        return fromDateTimestamp;
    }

    public void setFromDateTimestamp(long fromDateTimestamp) {
        this.fromDateTimestamp = fromDateTimestamp;
    }

    public long getToDateTimestamp() {
        return toDateTimestamp;
    }

    public void setToDateTimestamp(long toDateTimestamp) {
        this.toDateTimestamp = toDateTimestamp;
    }
}
