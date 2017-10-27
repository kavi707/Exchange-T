package com.kavi.droid.exchange.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kavi.droid.exchange.R;

/**
 * Created by kavi707 on 10/8/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class TicketRequestDetailActivity extends ExchangeBaseActivity {

    private TextView requestedNameTextView;
    private ImageView reqUserImageView;
    private TextView reqTypeTextView;
    private TextView qtyTextView;
    private TextView startToEndTextView;
    private TextView ticketDateTextView;
    private TextView ticketTimeTextView;
    private TextView contactNumberTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_request_detail);

        setUpViews();
    }

    private void setUpViews() {
        requestedNameTextView = (TextView) findViewById(R.id.requestedNameTextView);
        reqUserImageView = (ImageView) findViewById(R.id.reqUserImageView);
        reqTypeTextView = (TextView) findViewById(R.id.reqTypeTextView);
        qtyTextView = (TextView) findViewById(R.id.qtyTextView);
        startToEndTextView = (TextView) findViewById(R.id.startToEndTextView);
        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
        contactNumberTextView = (TextView) findViewById(R.id.contactNumberTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
    }
}
