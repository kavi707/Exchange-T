package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
import com.kavi.droid.exchange.models.TicketRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class HomeFragment extends Fragment {

    private View rootView;
    private ListView ticketRequestListView;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> ticketRequestList = new ArrayList<>();

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpView(rootView);

        return rootView;
    }

    public void setUpView(View upView) {

        ticketRequestListView = (ListView) upView.findViewById(R.id.ticketRequestListView);

        TicketRequest ticketRequest  = new TicketRequest();
        ticketRequest.setReqType(TicketRequest.I_HAVE);
        ticketRequest.setStartToEnd(TicketRequest.COLOMBO_KANDY);
        ticketRequest.setReqDate(1505391126);
        ticketRequest.setTicketDate("15 Sep 2017");
        ticketRequest.setTicketTime("4.50 pm");
        ticketRequest.setTicketDay("Friday");
        ticketRequest.setReqDescription("I have 2 tickets for 15th Sep 2017 4.50 pm Colombo - Kandy train tickets");

        TicketRequest ticketRequest1  = new TicketRequest();
        ticketRequest1.setReqType(TicketRequest.I_NEED);
        ticketRequest1.setStartToEnd(TicketRequest.COLOMBO_KANDY);
        ticketRequest1.setReqDate(1505394200);
        ticketRequest1.setTicketDate("15 Sep 2017");
        ticketRequest1.setTicketTime("5.10 pm");
        ticketRequest1.setTicketDay("Friday");
        ticketRequest1.setReqDescription("I need 1 ticket for 15th Sep 2017 5.10 pm Colombo - Kandy train tickets");

        ticketRequestList.add(ticketRequest);
        ticketRequestList.add(ticketRequest1);

        requestItemAdapter = new RequestItemAdapter(ticketRequestList, getActivity());
        ticketRequestListView.setAdapter(requestItemAdapter);
    }
}
