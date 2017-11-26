package com.kavi.droid.exchange.services.connections.dto;

/**
 * Created by kavi707 on 11/23/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class FilterTicketReq {

    private int typeFilter;
    private FilterTicketReqDate dateFilter;
    private int destinationFilter;
    private int qtyFilter;

    public int getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(int typeFilter) {
        this.typeFilter = typeFilter;
    }

    public FilterTicketReqDate getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(FilterTicketReqDate dateFilter) {
        this.dateFilter = dateFilter;
    }

    public int getDestinationFilter() {
        return destinationFilter;
    }

    public void setDestinationFilter(int destinationFilter) {
        this.destinationFilter = destinationFilter;
    }

    public int getQtyFilter() {
        return qtyFilter;
    }

    public void setQtyFilter(int qtyFilter) {
        this.qtyFilter = qtyFilter;
    }
}
