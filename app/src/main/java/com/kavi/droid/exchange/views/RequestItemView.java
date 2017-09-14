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

    private ImageView userPicImageView;
    private TextView reqTypeTextView;
    private TextView reqStartEndTextView;
    private TextView reqDescriptionTextView;
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

        userPicImageView = (ImageView) findViewById(R.id.userImageView);
        reqTypeTextView = (TextView) findViewById(R.id.reqTypeTextView);
        reqStartEndTextView = (TextView) findViewById(R.id.reqStartEndTextView);
        reqDescriptionTextView = (TextView) findViewById(R.id.reqDescriptionTextView);
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

        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(), userPicImageView, false);
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            reqTypeTextView.setText("I Have");
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            reqTypeTextView.setText("I Need");
        }

        if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KANDY) {
            reqStartEndTextView.setText("COLOMBO - KANDY");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KANDY_COLOMBO) {
            reqStartEndTextView.setText("KANDY - COLOMBO");
        }

        reqDescriptionTextView.setText(ticketRequest.getReqDescription());
        ticketDateTextView.setText(ticketRequest.getTicketDate());
        ticketDayTextView.setText(ticketRequest.getTicketDay());
        ticketTimeTextView.setText(ticketRequest.getTicketTime());
        timeAgoTextView.setText(getTimeDiffString(ticketRequest.getReqDate()));
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
        }

        return timeDiffString;
    }
}
