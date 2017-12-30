package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;
import com.kavi.droid.exchange.activities.TicketRequestDetailActivity;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
import com.kavi.droid.exchange.utils.CommonDialogBuilderUtil;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kavi707 on 10/8/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class MyRequestsHistoryFragment extends Fragment {

    private View rootView;
    private ListView myTicketRequestListView;
    private RelativeLayout noContentRelativeLayout;
    private TextView listErrorTextView;
    private AdView myTicketListAdView;

    private ProgressDialog progress;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> myTicketRequestList = new ArrayList<>();
    private static final String DEFAULT_SPACE_VIEW_ID = "DEFAULT:LAST_SPACE";

    private CommonUtils commonUtils = new CommonUtils();

    public MyRequestsHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_request_history, container, false);
        setUpView(rootView);
        ((LandingActivity) getActivity()).showFloatingActionButton();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        myTicketRequestList.clear();
        getTicketRequestAsync();

        // Set Action bar title
        ((LandingActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.e_nav_my_requests));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpView(View upView) {

        myTicketRequestListView = (ListView) upView.findViewById(R.id.myTicketRequestListView);
        noContentRelativeLayout = (RelativeLayout) upView.findViewById(R.id.noContentRelativeLayout);
        listErrorTextView = (TextView) upView.findViewById(R.id.listErrorTextView);

        // Google Ads
        myTicketListAdView = (AdView) upView.findViewById(R.id.myTicketListAdView);
        myTicketListAdView.loadAd(new AdRequest.Builder().build());

        myTicketRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!myTicketRequestList.get(position).getId().equals(DEFAULT_SPACE_VIEW_ID)) {
                    TicketRequestDetailActivity.setTicketRequest(myTicketRequestList.get(position));

                    new NavigationUtil(getActivity())
                            .to(TicketRequestDetailActivity.class)
                            .setTransitionAnim(NavigationUtil.ANIM_LEFT_TO_RIGHT)
                            .go();
                }
            }
        });

        myTicketRequestListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new CommonDialogBuilderUtil(getActivity())
                        .title("Are you sure?")
                        .content("Your are going to delete the selected ticket request. " +
                                "\n\nAre you sure, you want to delete it?")
                        .setFirstActionListener("Delete", new CommonDialogBuilderUtil.FirstActionInterface() {
                            @Override
                            public void firstAction() {
                                deleteTicketRequestFromId(myTicketRequestList.get(position).getId());
                            }
                        })
                        .setSecondActionListener("Keep", null).build().show();
                return true;
            }
        });
    }

    private void getTicketRequestAsync() {

        if (commonUtils.isOnline(getActivity())) {
            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(getActivity());
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getMyTicketRequest();
                }
            }).start();
        } else {
            noContentRelativeLayout.setVisibility(View.VISIBLE);
            listErrorTextView.setText(getResources().getString(R.string.list_msg_offline));
            Toast.makeText(getActivity(), "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyTicketRequest() {
        new ApiCalls().getMyTicketRequests(getActivity(), Constants.SYNC_METHOD, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (statusCode == 200) {
                    myTicketRequestList = commonUtils.getTicketRequestList(response);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            if (myTicketRequestList != null && myTicketRequestList.size() > 0) {

                                // Keep space for last element
                                TicketRequest lastSpace = new TicketRequest();
                                lastSpace.setId(DEFAULT_SPACE_VIEW_ID);
                                myTicketRequestList.add(lastSpace);

                                noContentRelativeLayout.setVisibility(View.INVISIBLE);
                                requestItemAdapter = new RequestItemAdapter(myTicketRequestList, getActivity());
                                myTicketRequestListView.setAdapter(requestItemAdapter);
                            } else {
                                noContentRelativeLayout.setVisibility(View.VISIBLE);
                                listErrorTextView.setText(getResources().getString(R.string.list_msg_empty));
                            }
                        }
                    });
                } else {
                    noContentRelativeLayout.setVisibility(View.VISIBLE);
                    listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        noContentRelativeLayout.setVisibility(View.VISIBLE);
                        listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                        Toast.makeText(getActivity(), "There was an error while making your request. Please try again from while.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void deleteTicketRequestFromId(final String ticketReqId) {

        if (commonUtils.isOnline(getActivity())) {
            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(getActivity());
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    new ApiCalls().deleteMyTicketRequestFromId(getActivity(), Constants.SYNC_METHOD, ticketReqId,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Successfully deleted your ticket request.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    // Refresh the list
                                    getMyTicketRequest();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                            Toast.makeText(getActivity(), "There was an error while making your request. Please try again from while.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            }).start();
        } else {
            Toast.makeText(getActivity(), "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
