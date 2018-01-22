package com.kavi.droid.exchange.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kavi.droid.exchange.Constants;
import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.dialogs.LoadingProgressBarDialog;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.ApiCallResponseHandler;
import com.kavi.droid.exchange.services.connections.ApiCalls;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonDialogBuilderUtil;
import com.kavi.droid.exchange.utils.CommonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kwijewardana on 9/15/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class AddRequestActivity extends ExchangeBaseActivity {

    private ImageView profilePicImageView;
    private RelativeLayout addNewTicketRequestContainer;

    private TextView nameTextView;
    private TextView ticketDateTextView;
    private TextView ticketTimeTextView;

    private EditText phoneNumEditText;
    private EditText emailEditText;
    private EditText reqNoteEditText;

    private Spinner typeSelectSpinner;
    private Spinner ticketCountSpinner;
    private Spinner destinationSpinner;

    private RelativeLayout spinnerRelativeLayout;
    private RelativeLayout dateTimeRelativeLayout;
    private RelativeLayout destinationsRelativeLayout;

    private Button submitReqBtn;
    private Button cancelReqBtn;

    private AdView addTicketRequestAdView;

    private ProgressDialog progress;

    private Context context = this;
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    private ImageLoadingManager imageLoadingManager;
    private List<String> destinationNameList;

    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        destinationNameList = commonUtils.initDestinationList();
        setCurrentDateTime();

        setUpViews();
    }

    private void setUpViews() {

        imageLoadingManager = new ImageLoadingManager(context);
        addNewTicketRequestContainer = (RelativeLayout) findViewById(R.id.addNewTicketRequestContainer);

        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);

        phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        reqNoteEditText = (EditText) findViewById(R.id.reqNoteEditText);

        typeSelectSpinner = (Spinner) findViewById(R.id.typeSelectSpinner);
        ticketCountSpinner = (Spinner) findViewById(R.id.ticketCountSpinner);
        destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);

        spinnerRelativeLayout = (RelativeLayout) findViewById(R.id.spinnerRelativeLayout);
        dateTimeRelativeLayout = (RelativeLayout) findViewById(R.id.dateTimeRelativeLayout);
        destinationsRelativeLayout = (RelativeLayout) findViewById(R.id.destinationsRelativeLayout);

        submitReqBtn = (Button) findViewById(R.id.submitReqBtn);
        cancelReqBtn = (Button) findViewById(R.id.cancelReqBtn);

        // Google Ads
        addTicketRequestAdView = (AdView) findViewById(R.id.addTicketRequestAdView);
        addTicketRequestAdView.loadAd(new AdRequest.Builder().build());

        typeSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        addNewTicketRequestContainer.setBackground(getResources().getDrawable(R.drawable.style_border_square_b));
                        break;
                    case 1:
                        addNewTicketRequestContainer.setBackground(getResources().getDrawable(R.drawable.style_border_square_ineed));
                        spinnerRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryGreen));
                        dateTimeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryGreen));
                        destinationsRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryGreen));
                        break;
                    case 2:
                        addNewTicketRequestContainer.setBackground(getResources().getDrawable(R.drawable.style_border_square_ihave));
                        spinnerRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryBlue));
                        dateTimeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryBlue));
                        destinationsRelativeLayout.setBackgroundColor(getResources().getColor(R.color.primaryBlue));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        if(SharedPreferenceManager.isUserLogIn(context)) {
            imageLoadingManager.loadRoundImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(context),
                    profilePicImageView);
            if (SharedPreferenceManager.getLoggedUserName(context) != null)
                nameTextView.setText(SharedPreferenceManager.getLoggedUserName(context));
            if (SharedPreferenceManager.getLoggedUserNumber(context) != null)
                phoneNumEditText.setText(SharedPreferenceManager.getLoggedUserNumber(context));
            if (SharedPreferenceManager.getLoggedUserEmail(context) != null)
                emailEditText.setText(SharedPreferenceManager.getLoggedUserEmail(context));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinationNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(dataAdapter);

        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketDateTextView.setKeyListener(null);
        ticketDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketDateTextView.setText(commonUtils.orderDate(year, month + 1, day));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
        ticketTimeTextView.setKeyListener(null);
        ticketTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String ampm, hourString, minuteString;

                        if(hour > 12) {
                            hour = hour - 12;
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }

                        if (hour < 10) {
                            hourString = "0" + hour;
                        } else {
                            hourString = String.valueOf(hour);
                        }

                        if (minute < 10) {
                            minuteString = "0" + minute;
                        } else {
                            minuteString = String.valueOf(minute);
                        }

                        ticketTimeTextView.setText(hourString + ":" + minuteString + " " + ampm);
                    }
                }, selectedHour, selectedMinute, true).show();
            }
        });

        cancelReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDirty()) {

                    String name = nameTextView.getText().toString();
                    String contactNum = phoneNumEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String type = typeSelectSpinner.getSelectedItem().toString();
                    String qty = ticketCountSpinner.getSelectedItem().toString();
                    String selectedDestination = destinationSpinner.getSelectedItem().toString();
                    String ticketDate = ticketDateTextView.getText().toString();
                    String ticketTime = ticketTimeTextView.getText().toString();
                    String reqNote = reqNoteEditText.getText().toString();

                    // Create default note
                    if (reqNote == null || reqNote.equals("")) {
                        if (commonUtils.getTypeFromName(type) == TicketRequest.I_NEED) {
                            reqNote = type + " " + qty + " ticket(s) for " + selectedDestination + " on " + ticketDate + " @ " +
                                    ticketTime + " Train. Is there anyone have tickets?";
                        } else {
                            reqNote = type + " " + qty + " ticket(s) for " + selectedDestination + " on " + ticketDate + " @ " +
                                    ticketTime + " Train. Is there anyone interested?";
                        }
                    }

                    String reqDay = null;
                    try {
                        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
                        Date dt = simpleDateformat.parse(ticketDate);
                        simpleDateformat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                        reqDay = simpleDateformat.format(dt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    final TicketRequest ticketRequest = new TicketRequest();
                    ticketRequest.setName(name);
                    ticketRequest.setUserPicUrl(SharedPreferenceManager.getLoggedUserImageUrl(context));
                    ticketRequest.setReqDescription(reqNote);
                    ticketRequest.setTicketDate(ticketDate);
                    ticketRequest.setTicketTime(ticketTime);
                    ticketRequest.setTicketDay(reqDay);
                    ticketRequest.setPhoneNo(contactNum);
                    ticketRequest.setEmail(email);
                    ticketRequest.setTicketStatus(TicketRequest.AVAILABLE);

                    if (!qty.equals("Select Quantity")) {
                        int ticketQty = Integer.parseInt(qty);
                        ticketRequest.setQty(ticketQty);
                    }

                    ticketRequest.setReqType(commonUtils.getTypeFromName(type));
                    ticketRequest.setStartToEnd(commonUtils.getDestinationFromName(selectedDestination));

                    if (commonUtils.isOnline(context)) {
                        if (progress == null) {
                            progress = LoadingProgressBarDialog.createProgressDialog(context);
                        }
                        progress.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                new ApiCalls().createTicketRequest(context, Constants.SYNC_METHOD, ticketRequest, new ApiCallResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {

                                        final int getStatusCode = statusCode;

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.dismiss();

                                                if (getStatusCode == 200) {
                                                    Toast.makeText(context, getResources().getString(R.string.e_toast_request_submit_success),
                                                            Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onNonSuccess(int statusCode, JSONObject response, Throwable throwable) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.dismiss();
                                                Toast.makeText(context, getResources().getString(R.string.e_toast_request_submit_error),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).start();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.e_toast_device_internet_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new CommonDialogBuilderUtil(context)
                            .title(getString(R.string.e_dialog_missing_info))
                            .content(getResources().getString(R.string.et_add_request_error_msg_1))
                            .setFirstActionListener(getString(R.string.e_label_ok), null).build().show();
                }
            }
        });
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        selectedHour = calendar.get(Calendar.HOUR);
        selectedMinute = calendar.get(Calendar.MINUTE);
    }

    private boolean isDirty() {

        String type = typeSelectSpinner.getSelectedItem().toString();
        String qty = ticketCountSpinner.getSelectedItem().toString();
        String selectedDestination = destinationSpinner.getSelectedItem().toString();
        String ticketDate = ticketDateTextView.getText().toString();
        String ticketTime = ticketTimeTextView.getText().toString();

        if (type.equals("Select Type")) {
            return false;
        }

        if (qty.equals("Select Quantity")) {
            return false;
        }

        if (selectedDestination.equals("Select Destination")) {
            return false;
        }

        if (ticketDate == null || ticketDate.equals("")) {
            return false;
        }

        if (ticketTime == null || ticketTime.equals("")) {
            return false;
        }

        return true;
    }
}
