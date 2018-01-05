package com.kavi.droid.exchange.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.EmailData;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.kavi.droid.exchange.utils.NavigationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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
    private TextView ticketReqNoteTextView;
    private Button contactNumberButton;
    private Button emailButton;
    private ImageButton fbShareButton;
    private AdView ticketDetailsAdView;
    private FloatingActionButton ticketStatusChangeFabIcon;
    private ProgressDialog progress;

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
        ticketReqNoteTextView = (TextView) findViewById(R.id.ticketReqNoteTextView);
        contactNumberButton = (Button) findViewById(R.id.contactNumberButton);
        emailButton = (Button) findViewById(R.id.emailButton);
        fbShareButton = (ImageButton) findViewById(R.id.fbShareButton);
        ticketStatusChangeFabIcon = (FloatingActionButton) findViewById(R.id.ticketStatusChangeFabIcon);

        // Google Ads
        ticketDetailsAdView = (AdView) findViewById(R.id.ticketDetailsAdView);
        ticketDetailsAdView.loadAd(new AdRequest.Builder().build());

        if (ticketRequest.getTicketStatus() == TicketRequest.AVAILABLE) {
            setFabToAvailable();
        } else if (ticketRequest.getTicketStatus() == TicketRequest.EXCHANGED) {
            setFabToExchanged();
        } else if (ticketRequest.getTicketStatus() == TicketRequest.DRAFTED) {
            // Todo - This will coming soon
        } else {
            setFabToAvailable();
        }

        ticketStatusChangeFabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ticketRequest.getTicketStatus() == TicketRequest.AVAILABLE) {
                    setFabToExchanged();
                    updateTicketStatus(TicketRequest.EXCHANGED);
                } else  if (ticketRequest.getTicketStatus() == TicketRequest.EXCHANGED) {
                    setFabToAvailable();
                    updateTicketStatus(TicketRequest.AVAILABLE);
                }
                SharedPreferenceManager.setIsTicketStatusUpdated(context, true);
            }
        });

        contactRelativeLayout.setVisibility(View.GONE);
        requestedNameTextView.setText("Me, " + ticketRequest.getName());
        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(),
                reqUserImageView, true);
        reqTypeTextView.setText(commonUtils.getTypeFromInt(ticketRequest.getReqType()));
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            ticketRequestDetailHolder.setBackgroundColor(getResources().getColor(R.color.i_have));
            contactNumberButton.setTextColor(getResources().getColor(R.color.i_have));
            contactNumberButton.setBackground(getResources().getDrawable(R.drawable.style_border_button_ihave));
            emailButton.setTextColor(getResources().getColor(R.color.i_have));
            emailButton.setBackground(getResources().getDrawable(R.drawable.style_border_button_ihave));
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            ticketRequestDetailHolder.setBackgroundColor(getResources().getColor(R.color.i_need));
            contactNumberButton.setTextColor(getResources().getColor(R.color.i_need));
            contactNumberButton.setBackground(getResources().getDrawable(R.drawable.style_border_button_ineed));
            emailButton.setTextColor(getResources().getColor(R.color.i_need));
            emailButton.setBackground(getResources().getDrawable(R.drawable.style_border_button_ineed));
        }
        qtyTextView.setText(ticketRequest.getQty() + " ticket(s)");
        startToEndTextView.setText(commonUtils.getDestinationFromInt(ticketRequest.getStartToEnd()));
        ticketDayTextView.setText(ticketRequest.getTicketDay());
        ticketDateTextView.setText(ticketRequest.getTicketDate());
        ticketTimeTextView.setText(ticketRequest.getTicketTime());
        ticketReqNoteTextView.setText(ticketRequest.getReqDescription());
        if (ticketRequest.getPhoneNo().equals(null) || ticketRequest.getPhoneNo().equals("")) {
            contactNumberButton.setVisibility(View.GONE);
        } else {
            contactNumberButton.setVisibility(View.VISIBLE);
            contactNumberButton.setText("Call me: " + ticketRequest.getPhoneNo());
        }
        emailButton.setText("Email me: " + ticketRequest.getEmail());

        RelativeLayout.LayoutParams dataLayoutParams = (RelativeLayout.LayoutParams) dataContentRelativeLayout.getLayoutParams();
        if (isThisMyRequest()) {
            // Floating Btn
            ticketStatusChangeFabIcon.setVisibility(View.VISIBLE);

            // FB share Btn
            fbShareButton.setVisibility(View.VISIBLE);

            // Contact Details
            contactRelativeLayout.setVisibility(View.GONE);
            dataLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            dataLayoutParams.setMargins(0, 0, 0, 0);
            dataLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else {
            // Floating Btn
            ticketStatusChangeFabIcon.setVisibility(View.GONE);

            // FB share Btn
            fbShareButton.setVisibility(View.GONE);

            // Contact Details
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

                EmailData emailTemplate = getMailObject();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailTemplate.getEmailAdd() });
                intent.putExtra(Intent.EXTRA_SUBJECT, emailTemplate.getSubject());
                intent.putExtra(Intent.EXTRA_TEXT, emailTemplate.getBody());
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        fbShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnFb();
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

    private EmailData getMailObject() {

        EmailData sendMailData = new EmailData();
        String subject = null;
        String body = null;
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            subject = "ExchangeT: Request to keep you tickets for me";
            body = "Hi " + ticketRequest.getName() + ",\n\n" + "I'm looking for " + Constants.EVENT_TRAIN +
                    " tickets in " + commonUtils.getDestinationFromInt(ticketRequest.getStartToEnd()) +" on " +
                    ticketRequest.getTicketDate() + " @ " + ticketRequest.getTicketTime() + ".\n\n" +
                    "If you can keep those tickets you for me, that would be grateful.\n\nThank you.";
        } else {
            subject = "ExchangeT: Do you need tickets?";
            body = "Hi " + ticketRequest.getName() + ",\n\n" + "I'm have " + ticketRequest.getQty() +
                    " ticket(s) for " + Constants.EVENT_TRAIN +
                    " in " + commonUtils.getDestinationFromInt(ticketRequest.getStartToEnd()) +" on " +
                    ticketRequest.getTicketDate() + " @ " + ticketRequest.getTicketTime() + ".\n\n" +
                    "If you are interested, then I can keep them for you.\n\nThank you.";
        }

        sendMailData.setEmailAdd(ticketRequest.getEmail());
        sendMailData.setSubject(subject);
        sendMailData.setBody(body);

        return sendMailData;
    }

    private Bitmap getScreenShot() {

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        View screenView = rootView.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void shareOnFb() {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(getScreenShot())
                .build();

        ShareHashtag shareHashTagAppName = new ShareHashtag.Builder()
                .setHashtag("#Exchanger")
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .setShareHashtag(shareHashTagAppName)
                .addPhoto(photo)
                .build();

        ShareDialog.show(TicketRequestDetailActivity.this, sharePhotoContent);
    }

    private void setFabToAvailable() {

        //copy it in a new one
        Drawable greenDone = getResources().getDrawable(R.drawable.e_done_icon).getConstantState().newDrawable();

        ticketStatusChangeFabIcon.setImageDrawable(greenDone);
        ticketStatusChangeFabIcon.setBackgroundTintList(getResources().getColorStateList(R.color.bright_green));
    }

    private void setFabToExchanged() {

        //copy it in a new one
        Drawable pinkCross = getResources().getDrawable(R.drawable.e_cross_icon);

        ticketStatusChangeFabIcon.setImageDrawable(pinkCross);
        ticketStatusChangeFabIcon.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
    }

    private void updateTicketStatus(final int newTicketStatus) {

        if (commonUtils.isOnline(context)) {

            if (progress == null) {
                progress = LoadingProgressBarDialog.createProgressDialog(context);
            }
            progress.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ApiCalls().updateTicketStatus(context, Constants.SYNC_METHOD, ticketRequest.getId(),
                            newTicketStatus, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                        }
                                    });
                                }
                            });
                }
            }).start();
        } else {
            Toast.makeText(context, getResources().getString(R.string.e_toast_device_internet_error), Toast.LENGTH_SHORT).show();
        }
    }
}
