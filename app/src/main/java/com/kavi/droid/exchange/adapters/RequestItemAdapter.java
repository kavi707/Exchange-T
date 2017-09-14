package com.kavi.droid.exchange.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.views.RequestItemView;

import java.util.List;

/**
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class RequestItemAdapter extends BaseAdapter {

    private List<TicketRequest> ticketRequestList;
    private Context context;

    public RequestItemAdapter(List<TicketRequest> ticketRequestList, Context context) {
        this.ticketRequestList = ticketRequestList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ticketRequestList.size();
    }

    @Override
    public Object getItem(int position) {
        return (ticketRequestList == null)?null:ticketRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestItemView requestItemView;
        if (convertView == null) {
            requestItemView = (RequestItemView) View.inflate(context, R.layout.view_request_item, null);
        } else {
            requestItemView = (RequestItemView) convertView;
        }

        requestItemView.setTicketRequest(ticketRequestList.get(position));
        return requestItemView;
    }
}
