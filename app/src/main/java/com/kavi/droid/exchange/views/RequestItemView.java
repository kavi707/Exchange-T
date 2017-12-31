package com.kavi.droid.exchange.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;

/**
 * Created by kavi707 on 9/13/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class RequestItemView extends RelativeLayout {

    private RelativeLayout requestTileHolder;
    private RelativeLayout requestDoneStatusOverlay;
    private ImageView userPicImageView;
    private TextView reqTypeTextView;
    private TextView reqStartEndTextView;
    private TextView reqQtyTextView;
    private TextView timeAgoTextView;
    private TextView ticketDateTextView;
    private TextView ticketDayTextView;
    private TextView ticketTimeTextView;

    private ImageLoadingManager imageLoadingManager;
    private TicketRequest ticketRequest;

    public RequestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoadingManager = new ImageLoadingManager(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        requestTileHolder = (RelativeLayout) findViewById(R.id.requestTileHolder);
        requestDoneStatusOverlay = (RelativeLayout) findViewById(R.id.requestDoneStatusOverlay);
        userPicImageView = (ImageView) findViewById(R.id.userImageView);
        reqTypeTextView = (TextView) findViewById(R.id.reqTypeTextView);
        reqStartEndTextView = (TextView) findViewById(R.id.reqStartEndTextView);
        reqQtyTextView = (TextView) findViewById(R.id.reqQtyTextView);
        timeAgoTextView = (TextView) findViewById(R.id.timeAgoTextView);
        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketDayTextView = (TextView) findViewById(R.id.ticketDayTextView);
        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
    }

    public TicketRequest getTicketRequest() {
        return ticketRequest;
    }

    public void setTicketRequest(TicketRequest ticketRequest) {
        this.ticketRequest = ticketRequest;

        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(), userPicImageView, true);
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            reqTypeTextView.setText("I Have");
            requestTileHolder.setBackgroundColor(getResources().getColor(R.color.i_have));
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            reqTypeTextView.setText("I Need");
            requestTileHolder.setBackgroundColor(getResources().getColor(R.color.i_need));
        }

        if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KANDY) {
            reqStartEndTextView.setText("COLOMBO - KANDY");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KANDY_COLOMBO) {
            reqStartEndTextView.setText("KANDY - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_BADULLA) {
            reqStartEndTextView.setText("COLOMBO - BADULLA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.BADULLA_COLOMBO) {
            reqStartEndTextView.setText("BADULLA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KURUNEGALA) {
            reqStartEndTextView.setText("COLOMBO - KURUNEGALA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KURUNEGALA_COLOMBO) {
            reqStartEndTextView.setText("KURUNEGALA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_ANURADHAPURA) {
            reqStartEndTextView.setText("COLOMBO - ANURADHAPURA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.ANURADHAPURA_COLOMBO) {
            reqStartEndTextView.setText("ANURADHAPURA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_VAUNIYA) {
            reqStartEndTextView.setText("COLOMBO - VAUNIYA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.VAUNIYA_COLOMBO) {
            reqStartEndTextView.setText("VAUNIYA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_JAFFNA) {
            reqStartEndTextView.setText("COLOMBO - JAFFNA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.JAFFNA_COLOMBO) {
            reqStartEndTextView.setText("JAFFNA - COLOMBO");
        }


        reqQtyTextView.setText(ticketRequest.getQty() + " Ticket(s)");
        ticketDateTextView.setText(ticketRequest.getTicketDate());
        ticketDayTextView.setText(ticketRequest.getTicketDay());
        ticketTimeTextView.setText(ticketRequest.getTicketTime());
        timeAgoTextView.setText(getTimeDiffString(ticketRequest.getReqDate()));

        if (ticketRequest.getTicketStatus() == TicketRequest.EXCHANGED) {
            requestDoneStatusOverlay.setVisibility(VISIBLE);
        } else {
            requestDoneStatusOverlay.setVisibility(INVISIBLE);
        }
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
