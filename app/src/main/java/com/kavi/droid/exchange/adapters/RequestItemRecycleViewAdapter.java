package com.kavi.droid.exchange.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.activities.TicketRequestDetailActivity;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.kavi.droid.exchange.viewHolders.RequestItemViewHolder;

import java.util.List;

/**
 * Created by pl36586 on 12/22/17.
 */

public class RequestItemRecycleViewAdapter extends RecyclerView.Adapter<RequestItemViewHolder> {

    private List<TicketRequest> ticketRequestList;
    private Context context;
    private Activity currentActivity;
    private ImageLoadingManager imageLoadingManager;
    private static final String DEFAULT_SPACE_VIEW_ID = "DEFAULT:LAST_SPACE";
    private static final int DEFAULT_SPACE_VIEW_TYPE = -1;

    public RequestItemRecycleViewAdapter(List<TicketRequest> ticketRequestList, Activity currentActivity) {
        this.ticketRequestList = ticketRequestList;
        this.context = currentActivity;
        this.currentActivity = currentActivity;
        if (imageLoadingManager == null)
            imageLoadingManager = new ImageLoadingManager(context);
    }

    @Override
    public RequestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_item, parent, false);
        return new RequestItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RequestItemViewHolder holder, int position) {
        setTicketRequest(holder, ticketRequestList.get(position));
        final int selectedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ticketRequestList.get(selectedPosition).getId().equals(DEFAULT_SPACE_VIEW_ID)) {
                    TicketRequestDetailActivity.setTicketRequest(ticketRequestList.get(selectedPosition));
                    new NavigationUtil(currentActivity)
                            .to(TicketRequestDetailActivity.class)
                            .setTransitionAnim(NavigationUtil.ANIM_LEFT_TO_RIGHT)
                            .go();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketRequestList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setTicketRequest(RequestItemViewHolder requestItemViewHolder, TicketRequest ticketRequest) {

        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(), requestItemViewHolder.userPicImageView, true);
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            requestItemViewHolder.reqTypeTextView.setText("I Have");
            requestItemViewHolder.requestTileHolder.setBackgroundColor(context.getResources().getColor(R.color.i_have));
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            requestItemViewHolder.reqTypeTextView.setText("I Need");
            requestItemViewHolder.requestTileHolder.setBackgroundColor(context.getResources().getColor(R.color.i_need));
        }

        if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KANDY) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - KANDY");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KANDY_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("KANDY - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_BADULLA) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - BADULLA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.BADULLA_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("BADULLA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KURUNEGALA) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - KURUNEGALA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KURUNEGALA_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("KURUNEGALA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_ANURADHAPURA) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - ANURADHAPURA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.ANURADHAPURA_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("ANURADHAPURA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_VAUNIYA) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - VAUNIYA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.VAUNIYA_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("VAUNIYA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_JAFNA) {
            requestItemViewHolder.reqStartEndTextView.setText("COLOMBO - JAFNA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.JAFNA_COLOMBO) {
            requestItemViewHolder.reqStartEndTextView.setText("JAFNA - COLOMBO");
        }

        if (ticketRequest.getReqDescription() != null && !ticketRequest.getReqDescription().equals("")) {
            requestItemViewHolder.reqDescriptionTextView.setText(ticketRequest.getReqDescription());
        } else {
            requestItemViewHolder.reqDescriptionTextView.setVisibility(View.INVISIBLE);
        }
        requestItemViewHolder.ticketDateTextView.setText(ticketRequest.getTicketDate());
        requestItemViewHolder.ticketDayTextView.setText(ticketRequest.getTicketDay());
        requestItemViewHolder.ticketTimeTextView.setText(ticketRequest.getTicketTime());
        requestItemViewHolder.timeAgoTextView.setText(getTimeDiffString(ticketRequest.getReqDate()));
    }

    private String getTimeDiffString(long reqDateTime) {
        String timeDiffString = null;

        long currentUnixTime = System.currentTimeMillis() / 1000L;

        long timeDiff = currentUnixTime - reqDateTime;

        long validSeconds = timeDiff % 60;

        long hours = timeDiff / (60*60);
        long minutes = timeDiff / 60;
        long days = 0;
        if (hours != 0)
            days = hours / 24;

        if (days > 0) {
            timeDiffString = days + " days ago";
        } else if (days == 0 && hours > 0) {
            timeDiffString = hours + " hours ago";
        } else if (hours == 0 && minutes > 0) {
            timeDiffString = minutes + " minutes ago";
        } else if (minutes == 0 && validSeconds > 0){
            timeDiffString = validSeconds + " seconds ago";
        } else {
            timeDiffString = "now";
        }

        return timeDiffString;
    }
}
