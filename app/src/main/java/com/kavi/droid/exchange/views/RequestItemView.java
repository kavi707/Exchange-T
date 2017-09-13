package com.kavi.droid.exchange.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavi.droid.exchange.R;

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

    public RequestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
}
