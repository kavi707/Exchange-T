package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    private RelativeLayout ticketRequestDetailHolder;
    private TextView requestedNameTextView;
    private ImageView reqUserImageView;
    private TextView reqTypeTextView;
    private TextView qtyTextView;
    private TextView startToEndTextView;
    private TextView ticketDayTextView;
    private TextView ticketDateTextView;
    private TextView ticketTimeTextView;
    private Button contactNumberButton;
    private Button emailButton;

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

        ticketRequestDetailHolder = (RelativeLayout) findViewById(R.id.ticketRequestDetailHolder);
        requestedNameTextView = (TextView) findViewById(R.id.requestedNameTextView);
        reqUserImageView = (ImageView) findViewById(R.id.reqUserImageView);
        reqTypeTextView = (TextView) findViewById(R.id.reqTypeTextView);
        qtyTextView = (TextView) findViewById(R.id.qtyTextView);
        startToEndTextView = (TextView) findViewById(R.id.startToEndTextView);
        ticketDayTextView = (TextView) findViewById(R.id.ticketDayTextView);
        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
        contactNumberButton = (Button) findViewById(R.id.contactNumberButton);
        emailButton = (Button) findViewById(R.id.emailButton);

        requestedNameTextView.setText("Me, " + ticketRequest.getName());
        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(),
                reqUserImageView, true);
        reqTypeTextView.setText(commonUtils.getTypeFromInt(ticketRequest.getReqType()));
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            ticketRequestDetailHolder.setBackgroundColor(getResources().getColor(R.color.i_have));
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            ticketRequestDetailHolder.setBackgroundColor(getResources().getColor(R.color.i_need));
        }
        qtyTextView.setText(ticketRequest.getQty() + " tickets");
        startToEndTextView.setText(commonUtils.getDestinationFromInt(ticketRequest.getStartToEnd()));
        ticketDayTextView.setText(ticketRequest.getTicketDay());
        ticketDateTextView.setText(ticketRequest.getTicketDate());
        ticketTimeTextView.setText(ticketRequest.getTicketTime());
        contactNumberButton.setText("Call me: " + ticketRequest.getPhoneNo());
        emailButton.setText("Email me: " + ticketRequest.getEmail());
    }

    public static void setTicketRequest(TicketRequest getTicketRequest) {
        ticketRequest = getTicketRequest;
    }
}
