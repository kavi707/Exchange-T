package com.kavi.droid.exchange.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.kavi.droid.exchange.activities.SignInActivity;
import com.kavi.droid.exchange.activities.SplashActivity;
import com.kavi.droid.exchange.activities.TicketRequestDetailActivity;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReqDate;
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
    private LinearLayout filterView;

    private TextView ticketFromDateTextView;
    private TextView ticketToDateTextView;

    private Spinner destinationSpinner;
    private Spinner typeSelectSpinner;
    private Spinner ticketCountSpinner;

    private Button filterActionButton;

    private AdView ticketListAdView;

    private ProgressDialog progress;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> ticketRequestList = new ArrayList<>();
    private boolean isDown;
    private List<String> destinationNameList;
    private List<String> typeNameList;
    private List<String> qtyList;
    private int selectedYear, selectedMonth, selectedDay;
    private boolean isDateFilterSet = false;
    private static final String DEFAULT_SPACE_VIEW_ID = "DEFAULT:LAST_SPACE";

    private CommonUtils commonUtils = new CommonUtils();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        destinationNameList = commonUtils.initDestinationList();
        typeNameList = commonUtils.initTypeList();
        qtyList = commonUtils.initQtyList();
        setCurrentDateTime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpView(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

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
                if (isDown) {
                    slideUp(filterView);
                } else {
                    slideDown(filterView);
                }
                isDown = !isDown;
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void setUpView(View upView) {

        ticketRequestListView = (ListView) upView.findViewById(R.id.ticketRequestListView);
        noContentRelativeLayout = (RelativeLayout) upView.findViewById(R.id.noContentRelativeLayout);
        listErrorTextView = (TextView) upView.findViewById(R.id.listErrorTextView);
        filterView = (LinearLayout) upView.findViewById(R.id.filterView);

        ticketFromDateTextView = (TextView) upView.findViewById(R.id.ticketFromDateTextView);
        ticketToDateTextView = (TextView) upView.findViewById(R.id.ticketToDateTextView);
        ticketToDateTextView.setEnabled(false);
        ticketToDateTextView.setHintTextColor(getResources().getColor(R.color.light_red));

        filterActionButton = (Button) upView.findViewById(R.id.filterActionButton);

        // Google Ads
        ticketListAdView = (AdView) upView.findViewById(R.id.ticketListAdView);
        ticketListAdView.loadAd(new AdRequest.Builder().build());

        // initialize as invisible (could also do in xml)
        filterView.setVisibility(View.INVISIBLE);
        isDown = false;

        destinationSpinner = (Spinner) upView.findViewById(R.id.destinationSpinner);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_filter_item, destinationNameList);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);

        typeSelectSpinner = (Spinner) upView.findViewById(R.id.typeSelectSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_filter_item, typeNameList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSelectSpinner.setAdapter(typeAdapter);

        ticketCountSpinner = (Spinner) upView.findViewById(R.id.ticketCountSpinner);
        ArrayAdapter<String> qtyAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_filter_item, qtyList);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketCountSpinner.setAdapter(qtyAdapter);

        ticketRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ticketRequestList.get(position).getId().equals(DEFAULT_SPACE_VIEW_ID)) {
                    TicketRequestDetailActivity.setTicketRequest(ticketRequestList.get(position));
                    new NavigationUtil(getActivity())
                            .to(TicketRequestDetailActivity.class)
                            .setTransitionAnim(NavigationUtil.ANIM_LEFT_TO_RIGHT)
                            .go();
                }
            }
        });

        ticketFromDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketFromDateTextView.setText(commonUtils.orderDate(year, month + 1, day));

                        isDateFilterSet = true;

                        ticketToDateTextView.setEnabled(true);
                        ticketToDateTextView.setHintTextColor(getResources().getColor(R.color.light_green));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        ticketToDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketToDateTextView.setText(commonUtils.orderDate(year, month + 1, day));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        filterActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = typeSelectSpinner.getSelectedItem().toString();
                String qty = ticketCountSpinner.getSelectedItem().toString();
                String selectedDestination = destinationSpinner.getSelectedItem().toString();
                String ticketFromDateString = ticketFromDateTextView.getText().toString();
                String ticketToDateString = ticketToDateTextView.getText().toString();

                FilterTicketReq filterTicketReq = new FilterTicketReq();
                if (!qty.equals("Select Qty")) {
                    int ticketQty = Integer.parseInt(qty);
                    filterTicketReq.setQtyFilter(ticketQty);
                } else {
                    filterTicketReq.setQtyFilter(-1);
                }

                if (!type.equals("Select Type")) {
                    filterTicketReq.setTypeFilter(commonUtils.getTypeFromName(type));
                } else {
                    filterTicketReq.setTypeFilter(-1);
                }

                if (!selectedDestination.equals("Select Destination")) {
                    filterTicketReq.setDestinationFilter(commonUtils.getDestinationFromName(selectedDestination));
                } else {
                    filterTicketReq.setDestinationFilter(-1);
                }

                if (isDateFilterSet) {
                    FilterTicketReqDate filterTicketReqDate = new FilterTicketReqDate();

                    if (!ticketFromDateString.equals("Ticket Date")) {
                        filterTicketReqDate.setFromDateTimestamp(commonUtils.getTimestampFromDate(ticketFromDateString));
                    } else {
                        filterTicketReqDate.setToDateTimestamp(-1);
                    }

                    if (!ticketToDateString.equals("Ticket Date")) {
                        filterTicketReqDate.setToDateTimestamp(commonUtils.getTimestampFromDate(ticketToDateString));
                    } else {
                        filterTicketReqDate.setToDateTimestamp(-1);
                    }

                    filterTicketReq.setDateFilter(filterTicketReqDate);
                }

                // Hide the filtering window
                slideUp(filterView);
                isDown = false;

                filterTicketRequest(filterTicketReq);
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
                                        if (ticketRequestList != null && ticketRequestList.size() > 0) {

                                            // Keep space for last element
                                            TicketRequest lastSpace = new TicketRequest();
                                            lastSpace.setId(DEFAULT_SPACE_VIEW_ID);
                                            ticketRequestList.add(lastSpace);

                                            noContentRelativeLayout.setVisibility(View.INVISIBLE);
                                            requestItemAdapter = new RequestItemAdapter(ticketRequestList, getActivity());
                                            ticketRequestListView.setAdapter(requestItemAdapter);
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
                                ticketRequestList.clear();
                                ticketRequestList = commonUtils.getTicketRequestList(response);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        if (ticketRequestList != null && ticketRequestList.size() > 0) {
                                            noContentRelativeLayout.setVisibility(View.INVISIBLE);
                                            requestItemAdapter = new RequestItemAdapter(ticketRequestList, getActivity());
                                            ticketRequestListView.setAdapter(requestItemAdapter);
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

    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -(view.getHeight()));                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -(view.getHeight()),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
