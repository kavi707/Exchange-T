package com.kavi.droid.exchange.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.SignInActivity;
import com.kavi.droid.exchange.activities.SplashActivity;
import com.kavi.droid.exchange.activities.TicketRequestDetailActivity;
import com.kavi.droid.exchange.adapters.RequestItemAdapter;
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
 * Created by kwijewardana on 9/14/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class HomeFragment extends Fragment {

    private View rootView;
    private ListView ticketRequestListView;
    private RelativeLayout noContentRelativeLayout;
    private TextView listErrorTextView;
    private LinearLayout filterView;

    private ProgressDialog progress;

    private RequestItemAdapter requestItemAdapter;
    private List<TicketRequest> ticketRequestList = new ArrayList<>();
    private boolean isDown;

    private CommonUtils commonUtils = new CommonUtils();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        // initialize as invisible (could also do in xml)
        filterView.setVisibility(View.INVISIBLE);
        isDown = false;

        ticketRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TicketRequestDetailActivity.setTicketRequest(ticketRequestList.get(position));
                new NavigationUtil(getActivity())
                        .to(TicketRequestDetailActivity.class)
                        .setTransitionAnim(NavigationUtil.ANIM_LEFT_TO_RIGHT)
                        .go();
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
}
