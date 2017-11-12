package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.utils.CommonUtils;

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
    private TextView ticketDayTextView;
    private TextView ticketDateTextView;
    private TextView ticketTimeTextView;
    private TextView contactNumberTextView;
    private TextView emailTextView;

    private Context context = this;
    private ImageLoadingManager imageLoadingManager;
    private CommonUtils commonUtils = new CommonUtils();

    public static TicketRequest ticketRequest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_request_detail);

        setUpViews();
    }

    private void setUpViews() {
        imageLoadingManager = new ImageLoadingManager(context);

        requestedNameTextView = (TextView) findViewById(R.id.requestedNameTextView);
        reqUserImageView = (ImageView) findViewById(R.id.reqUserImageView);
        reqTypeTextView = (TextView) findViewById(R.id.reqTypeTextView);
        qtyTextView = (TextView) findViewById(R.id.qtyTextView);
        startToEndTextView = (TextView) findViewById(R.id.startToEndTextView);
        ticketDayTextView = (TextView) findViewById(R.id.ticketDayTextView);
        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
        contactNumberTextView = (TextView) findViewById(R.id.contactNumberTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);

        requestedNameTextView.setText("Me, " + ticketRequest.getName());
        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(),
                reqUserImageView, true);
        reqTypeTextView.setText(commonUtils.getTypeFromInt(ticketRequest.getReqType()));
        qtyTextView.setText(ticketRequest.getQty() + " tickets");
        startToEndTextView.setText(commonUtils.getDestinationFromInt(ticketRequest.getStartToEnd()));
        ticketDayTextView.setText(ticketRequest.getTicketDay());
        ticketDateTextView.setText(ticketRequest.getTicketDate());
        ticketTimeTextView.setText(ticketRequest.getTicketTime());
        contactNumberTextView.setText(ticketRequest.getPhoneNo());
        emailTextView.setText(ticketRequest.getEmail());
    }

    public static void setTicketRequest(TicketRequest getTicketRequest) {
        ticketRequest = getTicketRequest;
    }
}
