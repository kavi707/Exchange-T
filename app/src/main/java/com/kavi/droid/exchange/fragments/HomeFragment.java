package com.kavi.droid.exchange.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.LandingActivity;
import com.kavi.droid.exchange.activities.SignInActivity;
import com.kavi.droid.exchange.activities.SplashActivity;
import com.kavi.droid.exchange.activities.TicketRequestDetailActivity;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
import com.kavi.droid.exchange.dialogs.FilteringDialog;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReqDate;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class HomeFragment extends Fragment {

    private View rootView;
    private ListView ticketRequestListView;
    private RelativeLayout noContentRelativeLayout;
    private TextView listErrorTextView;

    private Button iNeedBtn;
    private Button iHaveBtn;

    private AdView ticketListAdView;

    private ProgressDialog progress;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> ticketRequestList = new ArrayList<>();
    private List<TicketRequest> presentingTicketRequestList = new ArrayList<>();
    private List<TicketRequest> filteredTicketRequestList = new ArrayList<>();
    private static final String DEFAULT_SPACE_VIEW_ID = "DEFAULT:LAST_SPACE";
    private static final int DEFAULT_SPACE_VIEW_TYPE = -1;
    private boolean isINeedSelected;

    private CommonUtils commonUtils = new CommonUtils();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isINeedSelected = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpView(rootView);
        ((LandingActivity) getActivity()).showFloatingActionButton();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        isINeedSelected = SharedPreferenceManager.isINeedTypeSelected(getActivity());
        ticketRequestList.clear();
        getAllTicketRequest();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_icon_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                FilteringDialog filteringDialog = new FilteringDialog(getActivity(), isINeedSelected);
                filteringDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                filteringDialog.setFilterDialogResult(new FilteringDialog.OnFilteringDialogResult() {
                    @Override
                    public void filter(FilterTicketReq filterTicketReq) {
                        filterTicketRequest(filterTicketReq);
                    }
                });
                filteringDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void setUpView(View upView) {

        ticketRequestListView = (ListView) upView.findViewById(R.id.ticketRequestListView);
        noContentRelativeLayout = (RelativeLayout) upView.findViewById(R.id.noContentRelativeLayout);
        listErrorTextView = (TextView) upView.findViewById(R.id.listErrorTextView);
        iNeedBtn = upView.findViewById(R.id.iNeedBtn);
        iHaveBtn = upView.findViewById(R.id.iHaveBtn);

        // Google Ads
        ticketListAdView = (AdView) upView.findViewById(R.id.ticketListAdView);
        ticketListAdView.loadAd(new AdRequest.Builder().build());

        ticketRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!presentingTicketRequestList.get(position).getId().equals(DEFAULT_SPACE_VIEW_ID)) {
                    TicketRequestDetailActivity.setTicketRequest(presentingTicketRequestList.get(position));
                    new NavigationUtil(getActivity())
                            .to(TicketRequestDetailActivity.class)
                            .setTransitionAnim(NavigationUtil.ANIM_LEFT_TO_RIGHT)
                            .go();
                }
            }
        });

        iNeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isINeedSelected = true;
                SharedPreferenceManager.setIsINeedTypeSelected(getActivity(), true);
                iNeedBtn.setTextSize(18);
                iNeedBtn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                iHaveBtn.setTextSize(15);
                iHaveBtn.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));

                updateTiketList(commonUtils.getINeedTicketList(ticketRequestList));
            }
        });

        iHaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isINeedSelected = false;
                SharedPreferenceManager.setIsINeedTypeSelected(getActivity(), false);
                iNeedBtn.setTextSize(15);
                iNeedBtn.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                iHaveBtn.setTextSize(18);
                iHaveBtn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

                updateTiketList(commonUtils.getIHaveTicketList(ticketRequestList));
            }
        });
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

                    new ApiCalls().getTicketRequest(getActivity(), Constants.SYNC_METHOD, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            if (statusCode == 200) {
                                ticketRequestList = commonUtils.getTicketRequestList(response);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        if (isINeedSelected)
                                            updateTiketList(commonUtils.getINeedTicketList(ticketRequestList));
                                        else
                                            updateTiketList(commonUtils.getIHaveTicketList(ticketRequestList));
                                    }
                                });
                            } else {
                                noContentRelativeLayout.setVisibility(View.VISIBLE);
                                listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                            }
                        }

                        @Override
                        public void onFailure(final int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();

                                    if (statusCode == 401) {
                                        new NavigationUtil(getActivity())
                                                .to(SignInActivity.class)
                                                .finish()
                                                .go();
                                    } else {
                                        noContentRelativeLayout.setVisibility(View.VISIBLE);
                                        listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                                        Toast.makeText(getActivity(), "There was an error while making your request. Please try again from while.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();
        } else {
            noContentRelativeLayout.setVisibility(View.VISIBLE);
            listErrorTextView.setText(getResources().getString(R.string.list_msg_offline));
            Toast.makeText(getActivity(), "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTiketList(List<TicketRequest> ticketRequests) {

        if (ticketRequests != null && ticketRequests.size() > 0) {

            // Set the presenting ticket list
            presentingTicketRequestList = ticketRequests;

            // Keep space for last element
            TicketRequest lastSpace = new TicketRequest();
            lastSpace.setId(DEFAULT_SPACE_VIEW_ID);
            lastSpace.setReqType(DEFAULT_SPACE_VIEW_TYPE);
            ticketRequests.add(lastSpace);

            noContentRelativeLayout.setVisibility(View.INVISIBLE);
            requestItemAdapter = new RequestItemAdapter(ticketRequests, getActivity());
            ticketRequestListView.setAdapter(requestItemAdapter);
        } else {
            noContentRelativeLayout.setVisibility(View.VISIBLE);
            listErrorTextView.setText(getResources().getString(R.string.list_msg_empty));
        }
    }

    private void filterTicketRequest(final FilterTicketReq filterTicketReq) {

        if (commonUtils.isOnline(getActivity())) {
            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(getActivity());
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    new ApiCalls().filterTicketRequest(getActivity(), Constants.SYNC_METHOD, filterTicketReq, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            if (statusCode == 200) {
                                filteredTicketRequestList.clear();
                                filteredTicketRequestList = commonUtils.getTicketRequestList(response);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        if (isINeedSelected)
                                            updateTiketList(commonUtils.getINeedTicketList(filteredTicketRequestList));
                                        else
                                            updateTiketList(commonUtils.getIHaveTicketList(filteredTicketRequestList));
                                    }
                                });
                            } else {
                                noContentRelativeLayout.setVisibility(View.VISIBLE);
                                listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                            }
                        }

                        @Override
                        public void onFailure(final int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();

                                    if (statusCode == 401) {
                                        new NavigationUtil(getActivity())
                                                .to(SignInActivity.class)
                                                .finish()
                                                .go();
                                    } else {
                                        noContentRelativeLayout.setVisibility(View.VISIBLE);
                                        listErrorTextView.setText(getResources().getString(R.string.list_msg_issue));
                                        Toast.makeText(getActivity(), "There was an error while making your request. Please try again from while.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();
        } else {
            noContentRelativeLayout.setVisibility(View.VISIBLE);
            listErrorTextView.setText(getResources().getString(R.string.list_msg_offline));
            Toast.makeText(getActivity(), "Please check device Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
