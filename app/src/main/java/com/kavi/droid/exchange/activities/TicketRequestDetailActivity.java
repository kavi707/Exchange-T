package com.kavi.droid.exchange.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

/**
 * Created by kavi707 on 10/8/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class TicketRequestDetailActivity extends ExchangeBaseActivity {

    private RelativeLayout ticketRequestDetailHolder;
    private RelativeLayout contactRelativeLayout;
    private RelativeLayout dataContentRelativeLayout;
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
        contactRelativeLayout = (RelativeLayout) findViewById(R.id.contactRelativeLayout);
        dataContentRelativeLayout = (RelativeLayout) findViewById(R.id.dataContentRelativeLayout);

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

        contactRelativeLayout.setVisibility(View.GONE);
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

        RelativeLayout.LayoutParams dataLayoutParams = (RelativeLayout.LayoutParams) dataContentRelativeLayout.getLayoutParams();
        if (isThisMyRequest()) {
            contactRelativeLayout.setVisibility(View.GONE);
            dataLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            dataLayoutParams.setMargins(0, 0, 0, 0);
            dataLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else {
            contactRelativeLayout.setVisibility(View.VISIBLE);
            dataLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            dataLayoutParams.setMargins(0, 40, 0, 0);
            dataLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 0);
        }
        dataContentRelativeLayout.setLayoutParams(dataLayoutParams);

        contactNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ticketRequest.getPhoneNo()));
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Need to set a proper email template.

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { ticketRequest.getEmail() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "ExchangeT: Some thing");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }

    public static void setTicketRequest(TicketRequest getTicketRequest) {
        ticketRequest = getTicketRequest;
    }

    private boolean isThisMyRequest() {
        if (ticketRequest.getFbId().equals(SharedPreferenceManager.getFBUserId(context))) {
            return true;
        } else {
            return false;
        }
    }
}
