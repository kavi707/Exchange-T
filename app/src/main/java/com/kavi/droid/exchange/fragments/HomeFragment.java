package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.ApiClientResponse;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.apiConnection.ApiClient;
import com.kavi.droid.exchange.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class HomeFragment extends Fragment {

    private View rootView;
    private ListView ticketRequestListView;

    private ProgressDialog progress;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> ticketRequestList = new ArrayList<>();

    private CommonUtils commonUtils = new CommonUtils();
    private ApiClient apiClient;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        apiClient = new ApiClient(getActivity());
        setUpView(rootView);

        return rootView;
    }

    public void setUpView(View upView) {

        ticketRequestListView = (ListView) upView.findViewById(R.id.ticketRequestListView);

        getAllTicketRequest();
    }

    private void getAllTicketRequest() {

        if (commonUtils.isOnline(getActivity())) {
            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(getActivity());
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApiClientResponse response = apiClient.getTicketRequest(Constants.SYNC_METHOD);
                    if (response != null) {

                        if (response.getHttpStatusCode() == 200) {
                            ticketRequestList = commonUtils.getTicketRequestList(response.getResponseObj());
                        } else {
                            Toast.makeText(getActivity(), "There was an error while making your request. Please try again from while.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                requestItemAdapter = new RequestItemAdapter(ticketRequestList, getActivity());
                                ticketRequestListView.setAdapter(requestItemAdapter);
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(getActivity(), "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
